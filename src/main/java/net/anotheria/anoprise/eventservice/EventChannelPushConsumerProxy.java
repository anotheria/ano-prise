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
		
		consumers = new ArrayList<EventServiceConsumer>(10);	
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
		for (int i=0, n=consumers.size(); i<n; i++){
			EventServicePushConsumer consumer = (EventServicePushConsumer) consumers.get(i);
			try{
				consumer.push(e);
			}catch(Exception ex){
				log.error("Pushing to consumer "+consumer+" caused an error:", ex);
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
