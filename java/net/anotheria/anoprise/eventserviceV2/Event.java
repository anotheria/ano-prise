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
		this.data = data; 
	}
	
	public Event(String originator, Serializable data){
		this.timestamp = System.currentTimeMillis();
		this.originator = originator;
		this.data = data;
		this.nonExistent = false;
	}
	
	/**
	 * Get event data
	 */
	public Serializable getData() {
		return data;
	}

	/**
	 * Get originator of the event, i.e. the creator or sender.
	 */
	public String getOriginator() {
		return originator;
	}

	/**
	 * Get event createion timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Set event data
	 */
	public void setData(Serializable serializable) {
		data = serializable;
	}

	/**
	 * Set event originator
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
