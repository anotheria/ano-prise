package net.anotheria.anoprise.eventservice.remote;

import net.anotheria.anoprise.eventservice.EventChannelPushSupplierProxy;
import net.anotheria.anoprise.eventservice.RemoteEventChannelSupplierProxy;

public class RemoteEventChannelPushSupplierProxyImpl extends EventChannelPushSupplierProxy implements RemoteEventChannelSupplierProxy{

	public RemoteEventChannelPushSupplierProxyImpl(String name){
		super(name);
	}
	
	@Override
	public void deliverEvent(byte[] eventData) {
		System.out.println("Called deliver event: "+new String(eventData));
	}

}
