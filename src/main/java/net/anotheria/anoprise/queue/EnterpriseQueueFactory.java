package net.anotheria.anoprise.queue;

/**
 * The factory interface for a queue.
 *
 * @author lrosenberg
 * @param <T>
 * @version $Id: $Id
 */
public interface EnterpriseQueueFactory<T> {
	/**
	 * Creates a new queue of the given size.
	 *
	 * @param size a int.
	 * @return a {@link net.anotheria.anoprise.queue.EnterpriseQueue} object.
	 */
	EnterpriseQueue<T> createQueue(int size);
}
