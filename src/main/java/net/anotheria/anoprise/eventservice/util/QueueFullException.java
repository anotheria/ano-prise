package net.anotheria.anoprise.eventservice.util;

import net.anotheria.anoprise.eventservice.Event;

public class QueueFullException extends Exception{
	
	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	public QueueFullException(Event target, String message){
		super("Thrown away target event ("+target+"), additional info:  "+message);
	}
}
