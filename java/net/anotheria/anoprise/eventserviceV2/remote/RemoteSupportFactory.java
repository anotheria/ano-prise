package net.anotheria.anoprise.eventserviceV2.remote;

import net.anotheria.anoprise.eventserviceV2.EventService;


/**
 * Remote support factory, to create remote proxy objects.
 * 
 * @author vkazhdan
 */
public class RemoteSupportFactory {
		
	private RemoteSupportFactory() {
	}

	/**
	 * Create new RemotePushConsumerProxy implementation for given channel
	 * @param channelName
	 */
	public static final RemotePushConsumerProxy createRemotePushConsumerProxy(String channelName, EventService parentEventService) {
		return new RemotePushConsumerProxyImpl(channelName, parentEventService);
	}
	
	/**
	 * Create new RemotePushSupplierProxy implementation for given channel
	 * @param channelName
	 */
	public static final RemotePushSupplierProxy createRemotePushSupplierProxy(String channelName) {
		return new RemotePushSupplierProxyImpl(channelName);
	}
	
}
