package net.anotheria.anoprise.eventserviceV2.remote;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.anotheria.anoprise.eventserviceV2.Event;
import net.anotheria.anoprise.eventserviceV2.EventService;

import org.apache.log4j.Logger;


public class RemotePushConsumerProxyImpl extends AbstractRemoteEventChannel
 implements	RemotePushConsumerProxy {

	private transient static final long serialVersionUID = 2462748225750279040L;
	private transient static Logger log = Logger.getLogger(RemotePushConsumerProxyImpl.class);
	
	private transient final EventService parentEventService;
	private transient List<RemotePushSupplierProxy> remoteSupplierProxys;
	
	public RemotePushConsumerProxyImpl(String channelName, EventService parentEventService) {
		super(channelName);
		this.parentEventService = parentEventService;
		this.remoteSupplierProxys = new CopyOnWriteArrayList<RemotePushSupplierProxy>(); 
	}
		
	@Override
	public void remotePush(Event e) throws RemoteException {
		log.debug("Pushing event for all remote supplier proxys. channel: " + getChannelName() + " eventData: " + e.getData());
		for (RemotePushSupplierProxy remoteSupplierProxy : remoteSupplierProxys) {
			try {
				remoteSupplierProxy.remotePush(e);
			} catch (RemoteException ex) {
				// Double check and then notify proxy unavailable
				log.warn("Error was during remotely pushing event to remote supplier proxy. Try to repeat call in 2 sec. proxy: " + remoteSupplierProxy 
						+ ". Cause: " + ex.getMessage());				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ignored) { }
				
				try {					
					remoteSupplierProxy.remotePush(e);
				} catch (RemoteException ex2) {
					log.warn("Error was during second remotely pushing event to remote supplier proxy. Notify proxy unavailable. proxy: " + remoteSupplierProxy 
							+ ". Cause: " + ex.getMessage());
					parentEventService.notifyEventChannelUnavailable(remoteSupplierProxy, getChannelName());
				}				
				
			}
		}
	}

	@Override
	public void remoteAdd(RemotePushSupplierProxy remotePushSupplierProxy)
			throws RemoteException {
		((CopyOnWriteArrayList<RemotePushSupplierProxy>)remoteSupplierProxys).addIfAbsent(remotePushSupplierProxy);
	}

	@Override
	public void remoteRemove(RemotePushSupplierProxy remotePushSupplierProxy)
			throws RemoteException {
		remoteSupplierProxys.remove(remotePushSupplierProxy);
	}

}
