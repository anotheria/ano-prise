package net.anotheria.anoprise.eventservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

public class RemoteEventServicePushConsumerProxy implements EventServicePushConsumer {

	private RemoteEventServicePushConsumer consumer;

	private Logger logger = LoggerFactory.getLogger(RemoteEventServicePushConsumerProxy.class.getName());

	public RemoteEventServicePushConsumerProxy(RemoteEventServicePushConsumer aConsumer) {
		this.consumer = aConsumer;
	}

	@Override
	public void push(Event e) {
		if (consumer == null)
			return;

		try {
			consumer.push(e);
		} catch (RemoteException re) {
			logger.warn("Delivering event service event to remote consumer fail.", re);
		}
	}

}
