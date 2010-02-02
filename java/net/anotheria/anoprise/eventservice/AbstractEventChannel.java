package net.anotheria.anoprise.eventservice;

import org.apache.log4j.Logger;

/**
 * Base class for an event channel.
 * @author lrosenberg
 * Created on 22.09.2004
 */
public abstract class AbstractEventChannel implements EventChannel{

	private String name;
	protected Logger log;
	
	protected AbstractEventChannel(String aName){
		setName(aName);
		log = Logger.getLogger(this.getClass());
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
	
	protected void out(String msg){
		log.debug("["+name+"] "+msg);
	}

}
