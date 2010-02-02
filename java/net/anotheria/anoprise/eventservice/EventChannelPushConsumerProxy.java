package net.anotheria.anoprise.eventservice;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Please remind lrosenberg to comment this class.
 * @author lrosenberg
 * Created on 22.09.2004
 */
public class EventChannelPushConsumerProxy extends AbstractEventChannel implements EventChannelConsumerProxy{

	private List<EventServiceConsumer> consumers;

	public EventChannelPushConsumerProxy(String name){
		super(name);
		
		consumers = new ArrayList<EventServiceConsumer>(10);	
	}
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

	public boolean isLocal() {
		return true;
	}

	public void push(Event e) {
		throw new UnsupportedOperationException("push");

	}

}
