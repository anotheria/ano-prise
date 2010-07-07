package net.anotheria.anoprise.eventserviceV2.test;

import java.rmi.RemoteException;

import net.anotheria.anoprise.eventserviceV2.Event;
import net.anotheria.anoprise.eventserviceV2.EventChannelForRemotePushConsumer;
import net.anotheria.anoprise.eventserviceV2.EventService;
import net.anotheria.anoprise.eventserviceV2.EventServiceException;
import net.anotheria.anoprise.eventserviceV2.EventServiceFactory;
import net.anotheria.anoprise.eventserviceV2.local.LocalPushConsumer;

import org.apache.log4j.BasicConfigurator;


/**
 * Test event channel consumer
 * 
 * @author vkazhdan
 */
public class TestRemoteECConsumer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		// ============ Consumer ================
		EventService es = EventServiceFactory.getEventServiceInstance();
		
		EventChannelForRemotePushConsumer eventChannel;
		
		try {
			eventChannel = es.obtainEventChannelForRemotePushConsumer("TestEC");
		} catch (EventServiceException e) {
			System.err.println("Can't obtain event channel for remote push consumer. Cause: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		long consumerId = System.currentTimeMillis();
		
		try {
			eventChannel.remoteAdd(new TestEventServicePushConsumer("PushConsumer_" + consumerId));
		} catch (RemoteException ignored) { }			
		
		
	}
	
	
	private static class TestEventServicePushConsumer implements LocalPushConsumer {
		
		private final String name;
				
		public TestEventServicePushConsumer(String name) {
			this.name = name;
		}

		@Override
		public void push(Event e) {
			System.out.println("==== PushConsumer get event push(). consumerName: " + this.name + " event data: " + e.getData());
		}
		
	}

}
