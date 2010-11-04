package net.anotheria.anoprise.eventservice;

import static org.junit.Assert.assertEquals;
import net.anotheria.anoprise.eventservice.LocalEventsTest.TestConsumer;
import net.anotheria.anoprise.eventservice.remote.RemoteSupplierFactory;

import org.junit.Test;

public class TestRemoteEvents {
	@Test public void testPush(){
		EventService service = EventServiceFactory.createEventService();
		((EventServiceImpl)service).setRemoteSupportFactory(new RemoteSupplierFactory());
		
		TestConsumer t1 = new TestConsumer();
		TestConsumer t2 = new TestConsumer();
		TestRemoteConsumer t3 = new TestRemoteConsumer();
		
		EventChannel testPushForSupplier = service.obtainEventChannel("testPush", ProxyType.PUSH_SUPPLIER_PROXY);
		for (int i=0; i<10; i++){
			Event e = new Event(""+i);
			testPushForSupplier.push(e);
		}
		
		assertEquals(0, t1.getEventCount());
		assertEquals(0, t2.getEventCount());
		assertEquals(0, t3.getEventCount());
		
		service.obtainEventChannel("testPush", t1).addConsumer(t1);
		((RemoteEventChannelConsumerProxy)service.obtainEventChannel("testPush", t3)).addRemoteConsumer(t3);
		
		for (int i=0; i<10; i++){
			Event e = new Event(""+i);
			testPushForSupplier.push(e);
		}
		
		assertEquals(10, t1.getEventCount());
		assertEquals(10, t3.getEventCount());
		assertEquals(0, t2.getEventCount());

		service.obtainEventChannel("testPush", t2).addConsumer(t2);
		
		for (int i=0; i<10; i++){
			Event e = new Event(""+i);
			testPushForSupplier.push(e);
		}
		
		assertEquals(20, t1.getEventCount());
		assertEquals(20, t3.getEventCount());
		assertEquals(10, t2.getEventCount());
		
		((RemoteEventChannelConsumerProxy)service.obtainEventChannel("testPush", t3)).removeRemoteConsumer(t3);
		for (int i=0; i<10; i++){
			Event e = new Event(""+i);
			testPushForSupplier.push(e);
		}
		
		assertEquals(30, t1.getEventCount());
		assertEquals(20, t3.getEventCount());
		assertEquals(20, t2.getEventCount());

		
	}
	
	public static class TestRemoteConsumer implements RemoteEventServiceConsumer{

		private int eventCount;
		
		@Override
		public void deliverEvent(EventTransportShell event) {
			eventCount++;
		}
		
		public int getEventCount(){
			return eventCount;
		}
		
	}

}
