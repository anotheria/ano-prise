package net.anotheria.anoprise.eventservice;

/**
 * TODO please remined another to comment this class
 * @author another
 */
public interface EventService {
	
	/** 
	 * Returns the event channel for the given participant. If the channel is not yet available it will be created on the fly.
	 * @param channelName name of the channel.
	 */
	EventChannel obtainEventChannel(String channelName, EventServiceParticipant participant);
	
	/**
	 * This method is used to obtain an event channel of the specified type. 
	 */
	EventChannel obtainEventChannel(String channelName, ProxyType proxyType);  		

	
	void addListener(EventServiceListener listener);
	
	void removeListener(EventServiceListener listener);

}
