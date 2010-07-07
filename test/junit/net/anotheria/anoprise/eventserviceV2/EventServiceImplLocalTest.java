package net.anotheria.anoprise.eventserviceV2;

import static org.junit.Assert.assertEquals;
import net.anotheria.anoprise.eventserviceV2.local.LocalPushConsumer;

import org.junit.BeforeClass;
import org.junit.Test;


public class EventServiceImplLocalTest {

	@BeforeClass
	public static void initClass() throws Exception {
		// BasicConfigurator.configure();
	}

	@Test
	public void testSimpleLocalUsing() {
		//
		// Init local consumer
		//
		EventService esForConsumer = EventServiceFactory.getEventServiceInstance();
		
		// Getting event channel
		EventChannelForLocalPushConsumer ecForConsumer = esForConsumer.obtainEventChannelForLocalPushConsumer("TestEC");		
		
		// Add push consumer
		TestPushConsumer consumer1 = new TestPushConsumer("PushConsumer_1");
		ecForConsumer.add(consumer1);
				
		//
		// Init local supplier
		//
		EventService esForSupplier = EventServiceFactory.getEventServiceInstance();
		
		// Getting eventChannel
		EventChannelForLocalPushSupplier ecForSupplier = esForSupplier.obtainEventChannelForLocalPushSupplier("TestEC");
		
		//
		// Test pushes
		//
		ecForSupplier.push(new Event("TestEvent_1 from Supplier1"));
		ecForSupplier.push(new Event("TestEvent_2 from Supplier1"));
		assertEquals(2, consumer1.getPushCount());
		
		ecForSupplier.push(new Event("TestEvent_3 from Supplier1"));
		ecForSupplier.push(new Event("TestEvent_4 from Supplier1"));
		ecForSupplier.push(new Event("TestEvent_5 from Supplier1"));
		assertEquals(5, consumer1.getPushCount());
		
		// Test after remove consumer
		ecForConsumer.remove(consumer1);
		ecForSupplier.push(new Event("TestEvent_6 from Supplier1"));
		ecForSupplier.push(new Event("TestEvent_7 from Supplier1"));
		ecForSupplier.push(new Event("TestEvent_8 from Supplier1"));
		assertEquals(5, consumer1.getPushCount());
		
		
	}

	
	/**
	 * Push consumer for test
	 */
	private static class TestPushConsumer implements LocalPushConsumer {
		
		private final String name;
		private int pushCount = 0;
		
		public TestPushConsumer(String name) {
			this.name = name;
		}

		@Override
		public void push(Event e) {
			System.out.println("==== PushConsumer get event push(). consumerName: " + this.name + " event data: " + e.getData());
			pushCount++;
		}
		
		public int getPushCount() {
			return pushCount;
		}
		
	}
}
