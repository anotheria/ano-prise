package net.anotheria.anoprise.eventserviceV2.local;



/**
 * Local support factory, to create local proxy objects
 * 
 * @author vkazhdan
 */
public class LocalSupportFactory {
		
	private LocalSupportFactory() {
	}

	/**
	 * Create new LocalPushConsumerProxy implementation for given channel
	 * @param channelName
	 */
	public static final LocalPushConsumerProxy createLocalPushConsumerProxy(String channelName) {
		return new LocalPushConsumerProxyImpl(channelName);
	}
	
	/**
	 * Create new LocalPushSupplierProxy implementation for given channel
	 * @param channelName
	 */
	public static final LocalPushSupplierProxy createLocalPushSupplierProxy(String channelName) {
		return new LocalPushSupplierProxyImpl(channelName);
	}
	
}
