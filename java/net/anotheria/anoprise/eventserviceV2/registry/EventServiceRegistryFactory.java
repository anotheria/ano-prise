package net.anotheria.anoprise.eventserviceV2.registry;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * EventService registry factory
 * 
 * @author vkazhdan
 */
public class EventServiceRegistryFactory implements
		ServiceFactory<EventServiceRegistry> {

	public EventServiceRegistry create() {
		return new EventServiceRegistryImpl();
	}

}
