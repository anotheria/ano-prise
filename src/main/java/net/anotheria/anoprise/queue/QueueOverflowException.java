package net.anotheria.anoprise.queue;

/**
 * Means that at the moment of trying to enqueue an element to a queue it was
 * full.
 *
 * @author another
 * @version $Id: $Id
 */
public class QueueOverflowException extends RuntimeException {

	private static final long serialVersionUID = 603271155696840387L;

	/**
	 * <p>Constructor for QueueOverflowException.</p>
	 */
	public QueueOverflowException() {
		super("Queue is full!");
	}

}
