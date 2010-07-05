package net.anotheria.anoprise.eventserviceV2.test;

import java.rmi.RemoteException;

import net.anotheria.anoprise.eventserviceV2.Event;
import net.anotheria.anoprise.eventserviceV2.EventChannelForRemotePushSupplier;
import net.anotheria.anoprise.eventserviceV2.EventService;
import net.anotheria.anoprise.eventserviceV2.EventServiceException;
import net.anotheria.anoprise.eventserviceV2.EventServiceFactory;

import org.apache.log4j.BasicConfigurator;


/**
 * Test event channel supplier
 * 
 * @author vkazhdan
 */
public class TestECSupplier {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		// ============ Supplier ================
		EventService esForSupplier = EventServiceFactory.getEventServiceInstance();

		EventChannelForRemotePushSupplier ecForSupplier;
		try {
			ecForSupplier = esForSupplier.obtainEventChannelForRemotePushSupplier("TestEC");
		} catch (EventServiceException e) {
			System.err.println("Can't obtain event channel for remote push supplier. Cause: " + e.getMessage());
			return;
		}
			
		long supplierId = System.currentTimeMillis();
		
		for (int i = 0; i < 80; i++) {
			try {
				ecForSupplier.remotePush(new Event("TestEvent: " + i + " from Supplier_" + supplierId));
				Thread.sleep(8000);
			} catch (RemoteException e) {			
				System.err.println("Can't push event to remote push supplier channel. Cause: " + e.getMessage());
				return;
			} catch (InterruptedException e) {
				System.err.println("Interrupted exception. Cause: " + e.getMessage());
			}
		}
		
	}

}
