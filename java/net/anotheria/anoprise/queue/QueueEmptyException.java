package net.anotheria.anoprise.queue;

/**
 * TODO please remind dmetelin to comment this class
 * 
 * @author dmetelin
 */
public class QueueEmptyException extends RuntimeException {

	private static final long serialVersionUID = 603271155696840387L;

	public QueueEmptyException() {
		super("Queue is empty!");
	}
}
