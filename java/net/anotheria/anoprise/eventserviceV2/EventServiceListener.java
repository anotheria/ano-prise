package net.anotheria.anoprise.eventserviceV2;

/**
 * Event service listener interface.
 */
public interface EventServiceListener {
	void channelCreated(String channelName, ProxyType type);
	
	void channelDestroyed(String channelName, ProxyType type);
}
