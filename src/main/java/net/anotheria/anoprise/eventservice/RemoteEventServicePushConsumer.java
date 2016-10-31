package net.anotheria.anoprise.eventservice;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * <p>RemoteEventServicePushConsumer interface.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public interface RemoteEventServicePushConsumer extends Remote {

	/**
	 * <p>push.</p>
	 *
	 * @param e a {@link net.anotheria.anoprise.eventservice.Event} object.
	 * @throws java.rmi.RemoteException if any.
	 */
	void push(Event e) throws RemoteException;

}
