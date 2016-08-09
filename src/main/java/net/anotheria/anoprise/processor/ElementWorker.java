package net.anotheria.anoprise.processor;

/**
 * Interface for a worker class which performs operation on single element.
 * @author another
 *
 */
public interface ElementWorker<E> {
	/**
	 * Called to perform some work.
	 */
	void doWork(E workingElement) throws Exception;
	
}
