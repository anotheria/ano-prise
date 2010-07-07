package net.anotheria.anoprise.eventserviceV2.local;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.anotheria.anoprise.eventserviceV2.Event;

import org.apache.log4j.Logger;

public class LocalPushConsumerProxyImpl extends AbstractLocalEventChannel 
	implements LocalPushConsumerProxy {

	private static final Logger log = Logger.getLogger(LocalPushConsumerProxyImpl.class);
	
	private List<LocalPushConsumer> pushConsumers;
	
	public LocalPushConsumerProxyImpl(String channelName) {
		super(channelName);
		this.pushConsumers = new CopyOnWriteArrayList<LocalPushConsumer>();
	}

	@Override
	public void add(LocalPushConsumer consumer) {
		((CopyOnWriteArrayList<LocalPushConsumer>)pushConsumers).addIfAbsent(consumer);
	}

	@Override
	public void remove(LocalPushConsumer consumer) {
		pushConsumers.remove(consumer);
	}

	@Override
	public void push(Event e) {
		log.debug("Get event push. Deliver it for all local pushConsumers. Event Data: " + e.getData());
		for (LocalPushConsumer consumer : pushConsumers) {
			try {
				consumer.push(e);
			} catch (Exception ex) {
				log.warn("Error was during pushing event to local consumer: " + consumer + ". Cause: " + ex.getMessage() );
			}
		}
	}

}
