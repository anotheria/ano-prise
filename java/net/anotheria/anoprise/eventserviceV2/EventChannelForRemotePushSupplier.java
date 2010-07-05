package net.anotheria.anoprise.eventserviceV2;

import net.anotheria.anoprise.eventserviceV2.remote.RemoteEventChannel;
import net.anotheria.anoprise.eventserviceV2.remote.RemotePushSupporter;

/**
 * Event channel for using by remote push supplier.
 * Actually, it is remote consumer proxy.
 * 
 * @author vkazhdan
 */
public interface EventChannelForRemotePushSupplier
 extends RemoteEventChannel,
 		RemotePushSupporter {

}
