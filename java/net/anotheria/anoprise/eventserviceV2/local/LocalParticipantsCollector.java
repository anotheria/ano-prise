package net.anotheria.anoprise.eventserviceV2.local;

import net.anotheria.anoprise.eventserviceV2.EventChannelParticipant;

/**
 * Interface for objects, that can collect event channel participants
 * 
 * @author vkazhdan
 */
public interface LocalParticipantsCollector<T extends EventChannelParticipant> {

	/**
	 * Collect participant
	 */
	void add(T participant);
	
	/**
	 * Remove participant
	 */
	void remove(T participant);
	
}
