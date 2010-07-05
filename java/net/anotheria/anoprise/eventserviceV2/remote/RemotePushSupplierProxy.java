package net.anotheria.anoprise.eventserviceV2.remote;

import net.anotheria.anoprise.eventserviceV2.EventChannelForRemotePushConsumer;
import net.anotheria.anoprise.eventserviceV2.EventChannelParticipant;

/**
 * Interface for remote push supplier proxy
 * 
 * EventChannelForRemotePushConsumer extended here, instead of RemoteParticipantsCollector<LocalPushConsumer>
 * to support RemotePushSupplierProxy->EventChannelForRemotePushConsumer class cast 
 * 
 * @author vkazhdan
 */
public interface RemotePushSupplierProxy 
 extends EventChannelParticipant,
 		EventChannelForRemotePushConsumer,  
		RemotePushSupporter,
		RemoteProxy {
	
}
