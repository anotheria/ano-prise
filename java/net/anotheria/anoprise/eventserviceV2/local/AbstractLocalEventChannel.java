package net.anotheria.anoprise.eventserviceV2.local;


/**
 * Base implementation of the LocalEventChannel.
 * 
 * @author vkazhdan
 */
public abstract class AbstractLocalEventChannel implements LocalEventChannel {
	
	private final String channelName;
	
	public AbstractLocalEventChannel(String channelName) {
		this.channelName = channelName;
	}
	
	@Override
	public final String getChannelName() {		
		return channelName;
	}

}
