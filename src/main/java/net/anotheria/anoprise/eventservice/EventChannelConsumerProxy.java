package net.anotheria.anoprise.eventservice;

/**
 * Proxy for EventChannel Consumers.
 * @author lrosenberg
 * Created on 22.09.2004
 */
public interface EventChannelConsumerProxy extends EventChannelProxy{
	/**
	 * Called by a connected supplier proxy.
	 */
    void pushEvent(Event e);
}
