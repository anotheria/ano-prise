package net.anotheria.anoprise.eventserviceV2;

import java.io.Serializable;

import net.anotheria.util.Date;

/**
 * An event. Something which can somehow occur and is worth being sent over an event channel.
 * 
 * @author lrosenberg, vkazhdan
 * 
 */
public class Event implements Serializable, Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -956595007030617361L;

	public static final String NO_ORIGINATOR = "Unknown";
	
	/**
	 * Creation timestamp of this event.
	 */
	private long timestamp;
	/**
	 * Originator of the event, i.e. the creator or sender.
	 */
	private String originator;
	/**
	 * Custom event data.
	 */
	private Serializable data;
	
	/**
	 * If true this event will not be passed to any 'real' consumers, but only used to check the communication path.
	 */
	private boolean nonExistent;
	
	public Event(){
	}
	
	public Event(Serializable data){
	}
	
	public Event(String anOriginator, Serializable aData){
		timestamp = System.currentTimeMillis();
		originator = anOriginator;
		data      = aData;
		nonExistent = false;
	}
	
	/**
	 * @return
	 */
	public Serializable getData() {
		return data;
	}

	/**
	 * @return
	 */
	public String getOriginator() {
		return originator;
	}

	/**
	 * @return
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param serializable
	 */
	public void setData(Serializable serializable) {
		data = serializable;
	}

	/**
	 * @param string
	 */
	public void setOriginator(String string) {
		originator = string;
	}
	
	@Override
	public String toString(){
		return "Event from "+originator+" at "+new Date(timestamp)+" with data: "+data;
	}

	public boolean isNonExistent() {
		return nonExistent;
	}

	public void setNonExistent(boolean nonExistent) {
		this.nonExistent = nonExistent;
	}
	
}
