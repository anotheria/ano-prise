package net.anotheria.anoprise.eventserviceV2.local;

import net.anotheria.anoprise.eventserviceV2.EventChannelForLocalPushSupplier;
import net.anotheria.anoprise.eventserviceV2.EventChannelParticipant;


/**
 * Interface for local push supplier proxy
 * 
 * EventChannelForLocalPushSupplier extended here, Instead of LocalPushSupporter,
 * to support LocalPushSupplierProxy -> EventChannelForLocalPushSupplier class cast
 * 
 * @author vkazhdan
 */
public interface LocalPushSupplierProxy
 extends EventChannelParticipant,
 		EventChannelForLocalPushSupplier,
		LocalParticipantsCollector<LocalPushConsumerProxy> {	
	
}
