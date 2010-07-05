package net.anotheria.anoprise.eventserviceV2.local;

import net.anotheria.anoprise.eventserviceV2.Event;

/**
 * Interface for local push event supporters
 * 
 * @author vkazhdan
 */
public interface LocalPushSupporter {

	/**
	 * Push local event to the channel
	 */
	void push(Event e);
	
}
