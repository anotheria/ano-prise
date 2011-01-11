package net.anotheria.anoprise.queue;

import java.util.List;

import net.java.dev.moskito.core.predefined.QueueStats;

/**
 * This queue can be considered as such that repeats standard java queue with
 * the same data collecting logic and several enterprise extensions. Use this
 * queue whenever it is important to know about all or some queue changes'
 * events and obtaining queuing statistic.
 * 
 * @author dmetelin
 * 
 * @param <E>
 */
public interface EnterpriseQueue<E> {

	/**
	 * Inserts an element to the queue or throws QueueOverflowException if no
	 * left space at the moment in the queue due to capacity restrictions
	 * 
	 * @param element
	 *            to insert
	 * @throws QueueOverflowException
	 *             if no left space
	 */
	void add(E element) throws QueueOverflowException;

	/**
	 * Inserts an element to the queue if it possible due to capacity
	 * restriction.
	 * 
	 * @param element
	 *            to insert
	 * @return true if element was added
	 */
	boolean offer(E element);

	/**
	 * Retrieves and removes the head of this queue. This method differs from
	 * {@link #poll poll} only in that it throws an exception if this queue is
	 * empty.
	 * 
	 * @return the head of this queue
	 * @throws QueueEmptyException
	 *             if this queue is empty
	 */
	E remove() throws QueueEmptyException;

	/**
	 * Retrieves and removes the head of this queue, or returns <tt>null</tt> if
	 * this queue is empty.
	 * 
	 * @return the head of this queue, or <tt>null</tt> if this queue is empty
	 */
	E poll();

	/**
	 * Retrieves, but does not remove, the head of this queue, or returns
	 * <tt>null</tt> if this queue is empty.
	 * 
	 * @return the head of this queue, or <tt>null</tt> if this queue is empty
	 */
	E peek();

	/**
	 * Returns true if the queue doesn't contains elements.
	 * 
	 * @return
	 */
	boolean isEmpty();

	/**
	 * Returns the number of elements in the queue.
	 * 
	 * @return
	 */
	int size();

	/**
	 * Returns the capacity restriction of the queue.
	 * 
	 * @return
	 */
	int capacity();

	/**
	 * Drains the queue into a new list.
	 */
	List<E> drain();

	/**
	 * Adds a queue listener to the queue.
	 * 
	 * @param listener
	 */
	void addListener(EnterpriseQueueListener listener);

	/**
	 * Removes a queue listener from the queue.
	 * 
	 * @param listener
	 */
	void removeListener(EnterpriseQueueListener listener);

	/**
	 * Returns the size of the queue.
	 * 
	 * @return
	 */

	QueueStats getQueueStats();

}
