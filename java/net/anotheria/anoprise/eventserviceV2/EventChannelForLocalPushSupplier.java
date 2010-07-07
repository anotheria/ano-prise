package net.anotheria.anoprise.eventserviceV2;

import net.anotheria.anoprise.eventserviceV2.local.LocalEventChannel;
import net.anotheria.anoprise.eventserviceV2.local.LocalPushSupporter;

/**
 * Event channel for using by local push supplier.
 * Actually, it is local supplier proxy.
 * 
 * @author vkazhdan
 */
public interface EventChannelForLocalPushSupplier
 extends LocalEventChannel,
 		LocalPushSupporter {

}
