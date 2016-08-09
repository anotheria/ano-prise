package net.anotheria.anoprise.eventservice;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class LocalEventsTest {
	
	private String CHANNEL_NAME = "localTestPush";
	
	@Test public void testPush(){
		EventService service = EventServiceFactory.createEventService();
		
		TestConsumer t1 = new TestConsumer();
		TestConsumer t2 = new TestConsumer();
		
		EventChannel testPushForSupplier = service.obtainEventChannel(CHANNEL_NAME, ProxyType.PUSH_SUPPLIER_PROXY);
		for (int i=0; i<10; i++){
			Event e = new Event(String.valueOf(i));
			testPushForSupplier.push(e);
		}
		
		assertEquals(0, t1.getEventCount());
		assertEquals(0, t2.getEventCount());
		
		service.obtainEventChannel(CHANNEL_NAME, t1).addConsumer(t1);
		
		for (int i=0; i<10; i++){
			Event e = new Event(String.valueOf(i));
			testPushForSupplier.push(e);
		}
		
		assertEquals(10, t1.getEventCount());
		assertEquals(0, t2.getEventCount());

		service.obtainEventChannel(CHANNEL_NAME, t2).addConsumer(t2);
		
		for (int i=0; i<10; i++){
			Event e = new Event(String.valueOf(i));
			testPushForSupplier.push(e);
		}
		
		assertEquals(20, t1.getEventCount());
		assertEquals(10, t2.getEventCount());
		
		
	}
	
	@Test public void testMultipleSuppliers() throws InterruptedException{
		EventService service = EventServiceFactory.createEventService();

        int parallelThreadCount = 10;

		TestConsumer t1 = new TestConsumer();
		TestConsumer t2 = new TestConsumer();

        service.obtainEventChannel(CHANNEL_NAME, t1).addConsumer(t1);
		service.obtainEventChannel(CHANNEL_NAME, t2).addConsumer(t2);
		
		final CountDownLatch startLatch = new CountDownLatch(1);
		final CountDownLatch stopLatch = new CountDownLatch(parallelThreadCount);

        final TestSupplier[] suppliers = new TestSupplier[parallelThreadCount];
        int numberOfEvents = 10000;
        for (int i = 0; i<parallelThreadCount; i++){
			final TestSupplier ts = new TestSupplier(service, CHANNEL_NAME, numberOfEvents);; 
			suppliers[i] = ts;
			Thread t = new Thread(){
				public void run(){
					try{
						ts.sendEvents(startLatch, stopLatch);
					}catch(InterruptedException e){
						interrupt();
					}
				}
			};
			t.start();
		}
		
		startLatch.countDown();
		
		
		
		stopLatch.await();
		
		
		assertEquals(suppliers.length*numberOfEvents, t1.getEventCount());
		assertEquals(suppliers.length*numberOfEvents, t2.getEventCount());
		
	}
	
	public static class TestConsumer implements EventServicePushConsumer{
		
		private AtomicInteger eventCount = new AtomicInteger();
		
		@Override
		public void push(Event e) {
			eventCount.incrementAndGet();
		}
		
		public int getEventCount(){
			return eventCount.get();
		}
		
	}
	
	public static class TestSupplier implements EventServicePushSupplier{
		private int numberOfEvents;
		private EventChannel channel;
		
		public TestSupplier(EventService service, String channelName, int aNumberOfEvents){
			channel = service.obtainEventChannel(channelName, this);
			numberOfEvents = aNumberOfEvents;
		}
		
		public void sendEvents(CountDownLatch start, CountDownLatch stop) throws InterruptedException{
			start.await();
			for (int i=0; i<numberOfEvents; i++){
				Event e = new Event(String.valueOf(i));
				e.setOriginator(this.toString());
				channel.push(e);
			}
			stop.countDown();
		}
	}
}
