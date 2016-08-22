package net.anotheria.anoprise.processor;

/**
 * Thrown when an element was failed to add to a queued processor.
 *
 * @author another
 * @version $Id: $Id
 */
public class UnrecoverableQueueOverflowException extends Exception {

	private static final long serialVersionUID = 124031897562769830L;

	/**
	 * <p>Constructor for UnrecoverableQueueOverflowException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public UnrecoverableQueueOverflowException(String message) {
		super(message);
	}

}
