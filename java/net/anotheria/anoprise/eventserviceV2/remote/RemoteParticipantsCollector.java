package net.anotheria.anoprise.eventserviceV2.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import net.anotheria.anoprise.eventserviceV2.EventChannelParticipant;


/**
 * Interface for objects, that can collect event channel participants remotely remote
 * 
 * @author vkazhdan
 */
public interface RemoteParticipantsCollector<T extends EventChannelParticipant> extends Remote {

	/**
	 * Collect participant
	 * @throws RemoteException
	 */
	void remoteAdd(T participant) throws RemoteException;
	
	/**
	 * Remove participant
	 * @throws RemoteException
	 */
	void remoteRemove(T participant) throws RemoteException;
	
}
