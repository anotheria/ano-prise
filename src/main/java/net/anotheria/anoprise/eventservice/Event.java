package net.anotheria.anoprise.eventservice;

import java.io.Serializable;

import net.anotheria.util.Date;

/**
 * An event. Something which can somehow occur and is worth being sent over an event channel.
 *
 * @author lrosenberg
 * Created on 22.09.2004
 * @version $Id: $Id
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
	
	/**
	 * <p>Constructor for Event.</p>
	 */
	public Event(){
		this(EventServiceConstants.NO_EVENT_ORIGINATOR, null);
	}
	
	/**
	 * <p>Constructor for Event.</p>
	 *
	 * @param data a {@link java.io.Serializable} object.
	 */
	public Event(Serializable data){
		this(EventServiceConstants.NO_EVENT_ORIGINATOR, data);
	}
	
	/**
	 * <p>Constructor for Event.</p>
	 *
	 * @param anOriginator a {@link java.lang.String} object.
	 * @param aData a {@link java.io.Serializable} object.
	 */
	public Event(String anOriginator, Serializable aData){
		timestamp = System.currentTimeMillis();
		originator = anOriginator;
		data      = aData;
		nonExistent = false;
	}
	
	/**
	 * <p>Getter for the field <code>data</code>.</p>
	 *
	 * @return a {@link java.io.Serializable} object.
	 */
	public Serializable getData() {
		return data;
	}

	/**
	 * <p>Getter for the field <code>originator</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getOriginator() {
		return originator;
	}

	/**
	 * <p>Getter for the field <code>timestamp</code>.</p>
	 *
	 * @return a long.
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * <p>Setter for the field <code>data</code>.</p>
	 *
	 * @param serializable a {@link java.io.Serializable} object.
	 */
	public void setData(Serializable serializable) {
		data = serializable;
	}

	/**
	 * <p>Setter for the field <code>originator</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setOriginator(String string) {
		originator = string;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString(){
		return "Event from "+originator+" at "+new Date(timestamp)+" with data: "+data;
	}

	/**
	 * <p>isNonExistent.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isNonExistent() {
		return nonExistent;
	}

	/**
	 * <p>Setter for the field <code>nonExistent</code>.</p>
	 *
	 * @param nonExistent a boolean.
	 */
	public void setNonExistent(boolean nonExistent) {
		this.nonExistent = nonExistent;
	}
	
}
