package net.anotheria.anoprise.eventserviceV2.remote;

import net.anotheria.anoprise.eventserviceV2.EventChannelForRemotePushSupplier;
import net.anotheria.anoprise.eventserviceV2.EventChannelParticipant;


/**
 * Interface for remote push consumer proxy
 * 
 * EventChannelForRemotePushSupplier extended here, Instead of RemotePushSupporter,
 * to support RemotePushConsumerProxy -> EventChannelForRemotePushSupplier class cast
 * 
 * @author vkazhdan
 */
public interface RemotePushConsumerProxy
 extends EventChannelParticipant,
		RemoteParticipantsCollector<RemotePushSupplierProxy>,
		EventChannelForRemotePushSupplier, 
		RemoteProxy {	
	
}
