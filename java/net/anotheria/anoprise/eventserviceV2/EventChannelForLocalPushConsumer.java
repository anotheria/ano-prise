package net.anotheria.anoprise.eventserviceV2;

import net.anotheria.anoprise.eventserviceV2.local.LocalEventChannel;
import net.anotheria.anoprise.eventserviceV2.local.LocalParticipantsCollector;
import net.anotheria.anoprise.eventserviceV2.local.LocalPushConsumer;

/**
 * Event channel for using by local push consumers.
 * Actually, it is local consumer proxy.
 * 
 * @author vkazhdan
 */
public interface EventChannelForLocalPushConsumer
 extends LocalEventChannel,
 		LocalParticipantsCollector<LocalPushConsumer> {

}
