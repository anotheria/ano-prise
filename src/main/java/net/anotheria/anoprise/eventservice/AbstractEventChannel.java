package net.anotheria.anoprise.eventservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for an event channel.
 * @author lrosenberg
 * Created on 22.09.2004
 */
public abstract class AbstractEventChannel implements EventChannel{

	/**
	 * Name of the channel.
	 */
	private String name;
	/**
	 * Logger.
	 */
	protected Logger log;
	/**
	 * Creates a new AbstractEventChannel.
	 * @param aName
	 */
	protected AbstractEventChannel(String aName){
		setName(aName);
		log = LoggerFactory.getLogger(this.getClass());
	}
	
	/**
	 * @return the name of the channel
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param string
	 */
	protected void setName(String string) {
		name = string;
	}
	
	/**
	 * Produces a debug message.
	 * @param msg
	 */
	protected void out(String msg){
		log.debug("["+name+"] "+msg);
	}

}
