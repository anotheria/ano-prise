package net.anotheria.anoprise.eventserviceV2.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import net.anotheria.anoprise.eventserviceV2.Event;


/**
 * Interface for remote event pushing supporters
 * 
 * @author vkazhdan
 */
public interface RemotePushSupporter extends Remote {

	/**
	 * Push remotely event to the channel. All registered consumers will get it to process.
	 */
	void remotePush(Event e) throws RemoteException;
	
}
