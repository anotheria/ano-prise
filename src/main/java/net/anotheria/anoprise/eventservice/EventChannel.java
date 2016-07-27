package net.anotheria.anoprise.eventservice;


/**
 * This interface describes an event channel - a channel to send over events.
 * @author lrosenberg
 * Created on 22.09.2004
 */
public interface EventChannel {
	/**
	 * Pushes a new event in the channel.
	 * @param e
	 */
    void push(Event e);
	
	/**
	 * Adds a consumer to this channel.
	 * @param consumer consumer to add.
	 */
    void addConsumer(EventServiceConsumer consumer);
	
	/**
	 * Removes a consumer from this channel.
	 * @param consumer to remove.
	 */
    void removeConsumer(EventServiceConsumer consumer);
	
	/**
	 * Returns the name of this channel.
	 * @return the name of the channel.
	 */
    String getName();
}
