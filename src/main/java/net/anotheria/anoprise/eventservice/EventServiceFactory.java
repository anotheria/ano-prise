package net.anotheria.anoprise.eventservice;

/**
 * TODO Please remind lrosenberg to comment this class.
 *
 * @author lrosenberg
 * Created on 22.09.2004
 * @version $Id: $Id
 */
public class EventServiceFactory {
	
	/**
	 * <p>createEventService.</p>
	 *
	 * @return a {@link net.anotheria.anoprise.eventservice.EventService} object.
	 */
	public static final EventService createEventService(){
		return EventServiceImpl.getInstance();
	}
	
	private EventServiceFactory(){
		
	}
}
