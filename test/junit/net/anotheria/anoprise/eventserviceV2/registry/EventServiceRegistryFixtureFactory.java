package net.anotheria.anoprise.eventserviceV2.registry;

import net.anotheria.anoprise.eventserviceV2.registry.EventServiceRegistry;
import net.anotheria.anoprise.eventserviceV2.registry.EventServiceRegistryImpl;
import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * EventService registry fixture factory. 
 * Return registry singleton instance, instead of new instance.
 * 
 * @author vkazhdan
 */
public class EventServiceRegistryFixtureFactory implements
		ServiceFactory<EventServiceRegistry> {

	private static EventServiceRegistry instance = null;
	
	public EventServiceRegistry create() {
		if (instance == null) {
			instance = new EventServiceRegistryImpl();
		}
		return instance;
	}
	
	/**
	 * Reset singleton instance
	 */
	public void resetInstance() {
		instance = null;
	}

}
