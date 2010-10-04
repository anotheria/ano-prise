package net.anotheria.anoprise.eventservice.util;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventChannel;
import net.anotheria.anoprise.eventservice.EventServiceFactory;
import net.anotheria.anoprise.eventservice.EventServicePushConsumer;
import net.anotheria.anoprise.eventservice.ProxyType;
import net.anotheria.util.IdCodeGenerator;

public class QueuedEventSenderTest {
	static class QueuedEventSenderConsumer implements EventServicePushConsumer{
		long sum = 0;
		
		@Override
		public void push(Event e) {
			Integer i = (Integer)e.getData();
			sum += i;
		}
	}
	
	@Test public void testSimplePushOperations() throws QueueFullException, InterruptedException{
		//init consumer
		String channelName = IdCodeGenerator.generateCode();
		QueuedEventSenderConsumer consumer = new QueuedEventSenderConsumer();
		EventChannel channel = EventServiceFactory.createEventService().obtainEventChannel(channelName, consumer);
		channel.addConsumer(consumer);
		
		QueuedEventSender sender = new QueuedEventSender("TEST", EventServiceFactory.createEventService().obtainEventChannel(channelName, ProxyType.PUSH_SUPPLIER_PROXY));
		sender.start();
		long sum = 0;
		for (int i=0; i<5000; i++){
			sender.push(new Event(i));
			sum += i;
		}
		
		while(sender.hasUnsentElements()){
			System.out.println("Sender "+sender.getStatsString());
			Thread.sleep(100);
		}
		
		assertEquals(sum, consumer.sum);
		 
	}
	
	@Ignore @Test public void testForUnrunningQueueOverflow() throws QueueFullException{
		int sleep = 500;
		QueuedEventSender sender = new QueuedEventSender("TEST", IdCodeGenerator.generateCode(), 100, sleep, null);
		//first put 100 elements
		for (int i=0; i<100; i++){
			sender.push(new Event(i));
		}

		long start = System.currentTimeMillis();
		try{
			sender.push(new Event("FAIL!"));
			fail("QueueFullException expected!");
		}catch(QueueFullException e){
			
		}
		long end = System.currentTimeMillis();
		long duration = end - start;
		assertTrue("should have slept longer than sleep time", duration>sleep);
				
	}
	
}
