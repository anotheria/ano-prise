package net.anotheria.anoprise.eventserviceV2;

/**
 * Event service listener interface.
 */
public interface EventServiceListener {
	
	/**
	 * Called on channel creation.
	 * 
	 * @param channelName
	 * @param type
	 */
	void channelCreated(String channelName, ProxyType type);
	
	/**
	 * Called on channel destruction.
	 * 
	 * @param channelName
	 * @param type
	 */
	void channelDestroyed(String channelName, ProxyType type);
}
