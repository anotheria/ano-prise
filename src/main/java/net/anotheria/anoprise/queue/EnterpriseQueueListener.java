package net.anotheria.anoprise.queue;

/**
 * A listener for queue state change notifications.
 *
 * @author lrosenberg
 * Created on 22.06.2004
 * @version $Id: $Id
 */
public interface EnterpriseQueueListener {
	/**
	 * Called if the queue is emptied.
	 */
	void queueEmpty();
	/**
	 * Called if an element put into the queue.
	 */
	void queueElementAdded();
	/**
	 * Called if the queue is full.
	 */
	void queueFull();
	 
}
