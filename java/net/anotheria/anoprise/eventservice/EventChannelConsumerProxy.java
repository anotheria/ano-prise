package net.anotheria.anoprise.eventservice;

/**
 * TODO Please remind lrosenberg to comment this class.
 * @author lrosenberg
 * Created on 22.09.2004
 */
public interface EventChannelConsumerProxy extends EventChannelProxy{
	/**
	 * Called by a connected supplier proxy.
	 * @param e
	 */
	public void pushEvent(Event e);
}
