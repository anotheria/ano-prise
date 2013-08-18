package net.anotheria.anoprise.eventservice.remote;

import net.anotheria.anoprise.eventservice.AbstractEventChannel;
import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventServiceConsumer;
import net.anotheria.anoprise.eventservice.EventTransportShell;
import net.anotheria.anoprise.eventservice.RemoteEventChannelConsumerProxy;
import net.anotheria.anoprise.eventservice.RemoteEventServiceConsumer;
import net.anotheria.net.util.ByteArraySerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RemoteEventChannelPushConsumerProxyImpl extends AbstractEventChannel implements RemoteEventChannelConsumerProxy{
	
	private List<RemoteEventServiceConsumer> consumers;
	
	private static Logger log = LoggerFactory.getLogger(RemoteEventChannelPushConsumerProxyImpl.class);
	
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
		EventTransportShell shell = new EventTransportShell();
		shell.setChannelName(getName());
		try{
			shell.setData(ByteArraySerializer.serializeObject(e));
		}catch(Exception ignored){}
		for (RemoteEventServiceConsumer c  :consumers){
			c.deliverEvent(shell);
		}
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
