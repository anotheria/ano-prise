package net.anotheria.anoprise.eventservice.remote;

import net.anotheria.anoprise.eventservice.RemoteEventChannelConsumerProxy;
import net.anotheria.anoprise.eventservice.RemoteEventChannelSupplierProxy;
import net.anotheria.anoprise.eventservice.RemoteEventChannelSupportFactory;

public class RemoteSupplierFactory implements RemoteEventChannelSupportFactory{

	@Override
	public RemoteEventChannelConsumerProxy createRemoteEventChannelConsumerProxy(
			String channelName) {
		return new RemoteEventChannelPushConsumerProxyImpl(channelName);
	}

	@Override
	public RemoteEventChannelSupplierProxy createRemoteEventChannelSupplierProxy(
			String channelName) {
		return new RemoteEventChannelPushSupplierProxyImpl(channelName);
	}
	
}
