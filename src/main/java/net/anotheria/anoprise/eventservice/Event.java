package net.anotheria.anoprise.eventservice;

import java.io.Serializable;

import net.anotheria.util.Date;

/**
 * An event. Something which can somehow occur and is worth being sent over an event channel.
 * @author lrosenberg
 * Created on 22.09.2004
 */
public class Event implements Serializable, Cloneable{
	
	/**
	 * Serial versionuid.
	 */
	private static final long serialVersionUID = -956595007030617361L;
	/**
	 * Default originator.
	 */
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
		this(EventServiceConstants.NO_EVENT_ORIGINATOR, null);
	}
	
	public Event(Serializable data){
		this(EventServiceConstants.NO_EVENT_ORIGINATOR, data);
	}
	
	public Event(String anOriginator, Serializable aData){
		timestamp = System.currentTimeMillis();
		originator = anOriginator;
		data      = aData;
		nonExistent = false;
	}
	
	/**
	 */
	public Serializable getData() {
		return data;
	}

	/**
	 */
	public String getOriginator() {
		return originator;
	}

	/**
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 */
	public void setData(Serializable serializable) {
		data = serializable;
	}

	/**
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
