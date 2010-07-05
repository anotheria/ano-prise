package net.anotheria.anoprise.eventserviceV2;

/**
 * Event service factory.
 * 
 * @author vkazhdan
 */
public class EventServiceFactory {
	
	private static EventService instance = null;
	
	/**
	 * Uninstantiated class.
	 */
	private EventServiceFactory() {		
	}
	
	/**
	 * Getting event service singleton instance.
	 */
	public static final EventService getEventServiceInstance(){
		if (instance == null) {
			instance = new EventServiceImpl();
		}
		return instance;
	}
	
	
}
