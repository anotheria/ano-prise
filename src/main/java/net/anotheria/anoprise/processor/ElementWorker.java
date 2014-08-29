package net.anotheria.anoprise.processor;

/**
 * Interface for a worker class which performs operation on single element.
 * @author another
 *
 * @param <E>
 */
public interface ElementWorker<E> {
	/**
	 * Called to perform some work.
	 * @param workingElement
	 * @throws Exception
	 */
	void doWork(E workingElement) throws Exception;
	
}
