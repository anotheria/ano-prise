package net.anotheria.anoprise.eventservice;

/**
 * TODO please remined another to comment this class
 *
 * @author another
 * @version $Id: $Id
 */
public interface EventServiceListener {
	/**
	 * <p>channelCreated.</p>
	 *
	 * @param channelName a {@link java.lang.String} object.
	 * @param type a {@link net.anotheria.anoprise.eventservice.ProxyType} object.
	 */
	void channelCreated(String channelName, ProxyType type);
	
	/**
	 * <p>channelDestroyed.</p>
	 *
	 * @param channelName a {@link java.lang.String} object.
	 * @param type a {@link net.anotheria.anoprise.eventservice.ProxyType} object.
	 */
	void channelDestroyed(String channelName, ProxyType type);
}
