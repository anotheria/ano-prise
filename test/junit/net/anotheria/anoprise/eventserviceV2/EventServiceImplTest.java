package net.anotheria.anoprise.eventserviceV2;

import java.rmi.RemoteException;

import net.anotheria.anoprise.eventserviceV2.local.LocalPushConsumer;
import net.anotheria.anoprise.eventserviceV2.registry.EventServiceRegistry;
import net.anotheria.anoprise.eventserviceV2.registry.EventServiceRegistryFixtureFactory;
import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;

import org.junit.BeforeClass;
import org.junit.Test;


public class EventServiceImplTest {

	@BeforeClass
	public static void initClass() throws Exception {
		// BasicConfigurator.configure();
	}

	@Test
	public void testSimpleRemoteUsing() {
		//
		// Init remote consumer
		//
		EventService esForConsumer = new EventServiceImpl();
		// Overwrite remote event service registry to local one
		MetaFactory.reset();
		MetaFactory.addFactoryClass(EventServiceRegistry.class, Extension.REMOTE, EventServiceRegistryFixtureFactory.class);		
		
		// Get ting event channel
		EventChannelForRemotePushConsumer ecForConsumer;		
		try {
			ecForConsumer = esForConsumer.obtainEventChannelForRemotePushConsumer("TestEC");
		} catch (EventServiceException e) {
			System.err.println("Can't obtain event channel for remote push consumer. Cause: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		// Add push consumer
		try {
			ecForConsumer.remoteAdd(new TestPushConsumer("PushConsumer_1"));
		} catch (RemoteException ignored) {	}
				
		//
		// Init remote supplier
		//
		EventService esForSupplier = new EventServiceImpl();
		// Overwrite remote event service registry to local one
		MetaFactory.reset();
		MetaFactory.addFactoryClass(EventServiceRegistry.class, Extension.REMOTE, EventServiceRegistryFixtureFactory.class);		
		
		// Getting eventChannel
		EventChannelForRemotePushSupplier ecForSupplier;
		try {
			ecForSupplier = esForSupplier.obtainEventChannelForRemotePushSupplier("TestEC");
		} catch (EventServiceException e) {
			System.err.println("Can't obtain event channel for remote push supplier. Cause: " + e.getMessage());
			return;
		}
			
		try {
			ecForSupplier.remotePush(new Event("TestEvent_1 from Supplier1"));
			ecForSupplier.remotePush(new Event("TestEvent_2 from Supplier1"));
		} catch (RemoteException e) {			
			System.err.println("Can't push event to remote push supplier channel. Cause: " + e.getMessage());
			return;
		}
	}

	
	/**
	 * Push consumer for test
	 */
	private static class TestPushConsumer implements LocalPushConsumer {
		
		private final String name;
				
		public TestPushConsumer(String name) {
			this.name = name;
		}

		@Override
		public void push(Event e) {
			System.out.println("==== PushConsumer get event push(). consumerName: " + this.name + " event data: " + e.getData());
		}
		
	}
}
