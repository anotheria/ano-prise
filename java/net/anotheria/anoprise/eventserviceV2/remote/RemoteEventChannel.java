package net.anotheria.anoprise.eventserviceV2.remote;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Base interface for remote event channels.
 * 
 * @author vkazhdan
 */
public interface RemoteEventChannel extends Remote, Serializable {

	/**
	 * Get channel name
	 * 
	 * @return Channel name
	 * @throws RemoteException
	 */
	String getChannelName() throws RemoteException;
	
}
