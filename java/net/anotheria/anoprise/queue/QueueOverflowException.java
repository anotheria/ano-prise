package net.anotheria.anoprise.queue;

/**
 * Means that at the moment of trying to enqueue an element to a queue it was
 * full.
 * 
 * @author another
 */
public class QueueOverflowException extends RuntimeException {

	private static final long serialVersionUID = 603271155696840387L;

	public QueueOverflowException() {
		super("Queue is full!");
	}

}
