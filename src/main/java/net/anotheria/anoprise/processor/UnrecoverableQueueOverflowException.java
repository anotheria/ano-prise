package net.anotheria.anoprise.processor;

/**
 * Thrown when an element was failed to add to a queued processor.
 * 
 * @author another
 */
public class UnrecoverableQueueOverflowException extends Exception {

	private static final long serialVersionUID = 124031897562769830L;

	public UnrecoverableQueueOverflowException(String message) {
		super(message);
	}

}
