package net.anotheria.anoprise.queue;

/**
 * Means that at the moment of trying to dequeue an element from a queue it was
 * empty
 * 
 * @author dmetelin
 */
public class QueueEmptyException extends RuntimeException {

	private static final long serialVersionUID = 603271155696840387L;

	public QueueEmptyException() {
		super("Queue is empty!");
	}
}
