package net.anotheria.anoprise.eventservice;

/**
 * TODO please remined another to comment this class
 *
 * @author another
 * @version $Id: $Id
 */
public interface EventService {
	
	/**
	 * Returns the event channel for the given participant. If the channel is not yet available it will be created on the fly.
	 *
	 * @param channelName name of the channel.
	 * @param participant a {@link net.anotheria.anoprise.eventservice.EventServiceParticipant} object.
	 * @return a {@link net.anotheria.anoprise.eventservice.EventChannel} object.
	 */
	EventChannel obtainEventChannel(String channelName, EventServiceParticipant participant);
	
	/**
	 * This method is used to obtain an event channel of the specified type.
	 *
	 * @param channelName a {@link java.lang.String} object.
	 * @param proxyType a {@link net.anotheria.anoprise.eventservice.ProxyType} object.
	 * @return a {@link net.anotheria.anoprise.eventservice.EventChannel} object.
	 */
	EventChannel obtainEventChannel(String channelName, ProxyType proxyType);  		

	
	/**
	 * <p>addListener.</p>
	 *
	 * @param listener a {@link net.anotheria.anoprise.eventservice.EventServiceListener} object.
	 */
	void addListener(EventServiceListener listener);
	
	/**
	 * <p>removeListener.</p>
	 *
	 * @param listener a {@link net.anotheria.anoprise.eventservice.EventServiceListener} object.
	 */
	void removeListener(EventServiceListener listener);

}
