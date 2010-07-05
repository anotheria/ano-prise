package net.anotheria.anoprise.eventserviceV2.remote;

import java.rmi.RemoteException;

/**
 * Base implementation of the Remote EventChannel.
 * 
 * @author vkazhdan
 */
public abstract class AbstractRemoteEventChannel implements RemoteEventChannel {
		
	private static final long serialVersionUID = 3110050908569249259L;
	
	private final String channelName;
	
	public AbstractRemoteEventChannel(String channelName) {
		this.channelName = channelName;
	}
	
	@Override
	public final String getChannelName() throws RemoteException {		
		return channelName;
	}

}
