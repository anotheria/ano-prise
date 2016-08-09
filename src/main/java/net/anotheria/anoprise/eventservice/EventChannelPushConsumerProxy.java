package net.anotheria.anoprise.eventservice;

import java.util.ArrayList;
import java.util.List;

/**
 * Proxy implementation for Push Consumers.
 * @author lrosenberg
 * Created on 22.09.2004
 */
public class EventChannelPushConsumerProxy extends AbstractEventChannel implements EventChannelConsumerProxy{
	/**
	 * List of connected consumers.
	 */
	private List<EventServiceConsumer> consumers;

	public EventChannelPushConsumerProxy(String name){
		super(name);
		
		consumers = new ArrayList<>(10);
	}
	
	/**
	 * Adds a new consumer.
	 */
	public void addConsumer(EventServiceConsumer consumer) {
		if (! (consumer instanceof EventServicePushConsumer))
			throw new IllegalArgumentException("Only EventServicePushConsumer are supported.");
		consumers.add(consumer);
	}


	public void removeConsumer(EventServiceConsumer consumer) {
		consumers.remove(consumer);
	}

	public void pushEvent(Event e) {
        for (EventServiceConsumer consumer1 : consumers) {
            EventServicePushConsumer consumer = (EventServicePushConsumer) consumer1;
            try {
                consumer.push(e);
            } catch (RuntimeException ex) {
                log.error("Pushing to consumer '" + consumer + "' caused an error:", ex);
            }
        }
	}
	
	@Override
	public String toString(){
		return "PushConsumerProxy "+getName();
	}

	@Override public boolean isLocal() {
		return true;
	}

	@Override public void push(Event e) {
		throw new UnsupportedOperationException("push");

	}

}
