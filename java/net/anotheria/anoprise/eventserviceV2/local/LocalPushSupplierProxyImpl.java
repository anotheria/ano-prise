package net.anotheria.anoprise.eventserviceV2.local;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.anotheria.anoprise.eventserviceV2.Event;

import org.apache.log4j.Logger;

public class LocalPushSupplierProxyImpl extends AbstractLocalEventChannel 
	implements LocalPushSupplierProxy {

	private static final Logger log = Logger.getLogger(LocalPushSupplierProxyImpl.class);
	
	private List<LocalPushConsumerProxy> pushConsumerProxies;
	
	public LocalPushSupplierProxyImpl(String channelName) {
		super(channelName);
		this.pushConsumerProxies = new CopyOnWriteArrayList<LocalPushConsumerProxy>();
	}

	@Override
	public void push(Event e) {
		log.debug("Get event push. Deliver it for all local pushConsumerProxies. Event Data: " + e.getData());
		for (LocalPushConsumerProxy consumerProxy : pushConsumerProxies) {
			try {
				consumerProxy.push(e);
			} catch (Exception ex) {
				log.warn("Error was during pushing event to local consumer proxy: " + consumerProxy + ". Cause: " + ex.getMessage() );
			}
		}
	}
	
	@Override
	public void add(LocalPushConsumerProxy consumerProxy) {
		((CopyOnWriteArrayList<LocalPushConsumerProxy>)pushConsumerProxies).addIfAbsent(consumerProxy);
	}

	@Override
	public void remove(LocalPushConsumerProxy consumerProxy) {
		pushConsumerProxies.remove(consumerProxy);
	}
}
