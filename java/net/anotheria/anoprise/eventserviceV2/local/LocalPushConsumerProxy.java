package net.anotheria.anoprise.eventserviceV2.local;

import net.anotheria.anoprise.eventserviceV2.EventChannelForLocalPushConsumer;
import net.anotheria.anoprise.eventserviceV2.EventChannelParticipant;


/**
 * Interface for local push consumer proxy.
 * 
 * EventChannelForLocalPushConsumer extended here, Instead of LocalParticipantsCollector<LocalPushConsumer>,
 * to support LocalPushConsumerProxy -> EventChannelForLocalPushConsumer class cast
 * 
 * @author vkazhdan
 */
public interface LocalPushConsumerProxy
 extends EventChannelParticipant,
 		EventChannelForLocalPushConsumer,
		LocalPushSupporter {	
	
}
