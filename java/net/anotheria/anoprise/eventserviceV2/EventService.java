package net.anotheria.anoprise.eventserviceV2;


/**
 * Event service.
 * User to manage event channel for local and remote events support.
 * This is an extended observer, known as CORBA EventService.
 * 
 * Event channel is an abstraction for suppliers push out and consumers push in events
 * of any type with just Serialization support requirement.
 * 
 * Many remote supporters may push event for many remote consumers.
 * 
 * For remote events, service internally use distributed EventServiceRegistry, that SHOULD BE up and ready before any remote channel obtaining.
 * EventServiceRegistry is used only for new remote consumer or registration, after that, consumer and supplier virtual/physical machines communicates 
 * directly with each other, without registry mediator, that is important for traffic understanding.
 * 
 * To remove local consumer impl, simply remove it from obtained EventChannelForXxxPushConsumer.
 * 
 * Internal remote proxies will be removed from the registry automatically after the first time it's unavailability will be detected
 * (during remotePush or remote channel obtainment, depending on proxy type).  
 */
public interface EventService {
		
	/**
	 * Obtain event channel for using by local push supplier.
	 * 
	 * @param channelName Name for one type events channel, usually related to supplier service name.
	 */
	EventChannelForLocalPushSupplier obtainEventChannelForLocalPushSupplier(String channelName);
	
	/**
	 * Obtain event channel for using by local push consumer.
	 * 
	 * @param channelName Name for one type events channel, usually related to supplier service name.
	 */
	EventChannelForLocalPushConsumer obtainEventChannelForLocalPushConsumer(String channelName);
	
	/**
	 * Obtain event channel for using by remote push supplier.
	 * 
	 * @param channelName Name for one type events channel, usually related to supplier service name.
	 * @throws EventServiceException
	 */
	EventChannelForRemotePushSupplier obtainEventChannelForRemotePushSupplier(String channelName)
		throws EventServiceException;
	
	/**
	 * Obtain event channel for using by remote push consumer.
	 * 
	 * @param channelName Name for one type events channel, usually related to supplier service name.
	 * @throws EventServiceException
	 */
	EventChannelForRemotePushConsumer obtainEventChannelForRemotePushConsumer(String channelName)
		throws EventServiceException;
	
	/**
	 * Notify that event channel is unavailable anymore.
	 * 
	 * It is possible, but not really necessary for remote clients manually use this method to detach thyself,
	 * because it will be done automatically after the first time it's unavailability will be detected.  
	 * @param eventChannel EventChannelForRemotePushConsumer
	 */
	void notifyEventChannelUnavailable(EventChannelForRemotePushConsumer eventChannel, String channelName);
	
	/**
	 * Notify that event channel is unavailable anymore.
	 * 
	 * It is possible, but not really necessary for remote clients manually use this method to detach thyself,
	 * because it will be done automatically after the first time it's unavailability will be detected.
	 * @param eventChannel EventChannelForRemotePushSupplier
	 */
	void notifyEventChannelUnavailable(EventChannelForRemotePushSupplier eventChannel, String channelName);
	
	/**
	 * Add service listener.
	 * @param listener
	 */
	void addListener(EventServiceListener listener);
	
	/**
	 * Remove service listener.
	 * @param listener
	 */
	void removeListener(EventServiceListener listener);
	
	

}
