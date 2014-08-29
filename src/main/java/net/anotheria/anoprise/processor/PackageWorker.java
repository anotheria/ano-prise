package net.anotheria.anoprise.processor;

import java.util.List;

/**
 * Interface for a worker class which performs operation on package from elements.
 * @author dmetelin
 *
 * @param <E>
 */
public interface PackageWorker<E> {
	/**
	 * Called to perform some work.
	 * @param workingElement
	 * @throws Exception
	 */
	void doWork(List<E> workingPackage) throws Exception;

	/**
	 * Returns the maximal possible capacity of the package that can be worked. This
	 * doesn't guarantee the exact size of the package.
	 * 
	 * @return the maximal possible capacity of the package to be worked
	 */
	int packageCapacity();
	
}
