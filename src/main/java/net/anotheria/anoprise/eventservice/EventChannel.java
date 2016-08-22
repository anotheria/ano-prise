package net.anotheria.anoprise.eventservice;


/**
 * This interface describes an event channel - a channel to send over events.
 *
 * @author lrosenberg
 * Created on 22.09.2004
 * @version $Id: $Id
 */
public interface EventChannel {
	/**
	 * Pushes a new event in the channel.
	 *
	 * @param e a {@link net.anotheria.anoprise.eventservice.Event} object.
	 */
	public void push(Event e);
	
	/**
	 * Adds a consumer to this channel.
	 *
	 * @param consumer consumer to add.
	 */
	public void addConsumer(EventServiceConsumer consumer);
	
	/**
	 * Removes a consumer from this channel.
	 *
	 * @param consumer to remove.
	 */
	public void removeConsumer(EventServiceConsumer consumer);
	
	/**
	 * Returns the name of this channel.
	 *
	 * @return the name of the channel.
	 */
	public String getName(); 
}
