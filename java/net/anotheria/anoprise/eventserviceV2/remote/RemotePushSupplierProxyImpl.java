package net.anotheria.anoprise.eventserviceV2.remote;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.anotheria.anoprise.eventserviceV2.Event;
import net.anotheria.anoprise.eventserviceV2.local.LocalPushConsumer;

import org.apache.log4j.Logger;


public class RemotePushSupplierProxyImpl extends AbstractRemoteEventChannel 
	implements RemotePushSupplierProxy {
		
	private static final long serialVersionUID = 7004504828142357460L;
	private transient static Logger log = Logger.getLogger(RemotePushConsumerProxyImpl.class);
	
	private transient List<LocalPushConsumer> pushConsumers;
	
	public RemotePushSupplierProxyImpl(String channelName) {
		super(channelName);
		this.pushConsumers = new CopyOnWriteArrayList<LocalPushConsumer>();
	}
		
	@Override
	public void remotePush(Event e) throws RemoteException {
		log.debug("Get remote push. Deliver it for all local pushConsumers. Event Data: " + e.getData());
		for (LocalPushConsumer consumer : pushConsumers) {
			try {
				consumer.push(e);
			} catch (Exception ex) {
				log.warn("Error was during pushing event to local consumer: " + consumer + ". Cause: " + ex.getMessage() );
			}
		}
	}

	@Override
	public void remoteAdd(LocalPushConsumer consumer) throws RemoteException {
		((CopyOnWriteArrayList<LocalPushConsumer>)pushConsumers).addIfAbsent(consumer);
	}

	@Override
	public void remoteRemove(LocalPushConsumer consumer) throws RemoteException {
		pushConsumers.remove(consumer);
	}
	
}
