package net.anotheria.anoprise.eventserviceV2;

import net.anotheria.anoprise.eventserviceV2.local.LocalPushConsumer;
import net.anotheria.anoprise.eventserviceV2.remote.RemoteEventChannel;
import net.anotheria.anoprise.eventserviceV2.remote.RemoteParticipantsCollector;

/**
 * Event channel for using by remote push consumer.
 * Actually, it is remote supplier proxy.
 * 
 * RemoteException on remoteAdd() and remoteRemove() for remote push consumers may be undoubtedly ignored,
 * because RemoteException support here only for remote supplier proxy object exporting. 
 * 
 * @author vkazhdan
 */
public interface EventChannelForRemotePushConsumer 
 extends RemoteEventChannel, 
 		RemoteParticipantsCollector<LocalPushConsumer> {

}
