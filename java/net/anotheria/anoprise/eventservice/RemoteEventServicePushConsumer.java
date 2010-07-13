package net.anotheria.anoprise.eventservice;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteEventServicePushConsumer extends Remote {

	void push(Event e) throws RemoteException;

}
