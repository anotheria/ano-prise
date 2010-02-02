package net.anotheria.anoprise.eventservice;

/**
 * TODO please remined another to comment this class
 * @author another
 */
public interface EventServiceListener {
	void channelCreated(String channelName, ProxyType type);
	
	void channelDestroyed(String channelName, ProxyType type);
}
