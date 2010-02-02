package net.anotheria.anoprise.eventservice.remote;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import net.anotheria.anoprise.eventservice.AbstractEventChannel;
import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventServiceConsumer;
import net.anotheria.anoprise.eventservice.RemoteEventChannelConsumerProxy;
import net.anotheria.anoprise.eventservice.RemoteEventServiceConsumer;

public class RemoteEventChannelPushConsumerProxyImpl extends AbstractEventChannel implements RemoteEventChannelConsumerProxy{
	
	private List<RemoteEventServiceConsumer> consumers;
	
	private static Logger log = Logger.getLogger(RemoteEventChannelPushConsumerProxyImpl.class);
	
	public RemoteEventChannelPushConsumerProxyImpl(String aName){
		super(aName);
		consumers = new CopyOnWriteArrayList<RemoteEventServiceConsumer>();
	}

	@Override
	public void addRemoteConsumer(RemoteEventServiceConsumer consumer) {
		if (consumers.indexOf(consumer)!=-1){
			log.info("Consumer already registered, skipping "+consumer);
		}
		consumers.add(consumer);
			
	}

	@Override
	public void removeRemoteConsumer(RemoteEventServiceConsumer consumer) {
		consumers.remove(consumer);
	}

	@Override
	public void pushEvent(Event e) {
		System.out.println("PushEvent: "+e);
	}

	@Override
	public boolean isLocal() {
		return false;
	}

	@Override
	public void addConsumer(EventServiceConsumer consumer) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public void push(Event e) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public void removeConsumer(EventServiceConsumer consumer) {
		throw new UnsupportedOperationException("Not supported");
	}

}
