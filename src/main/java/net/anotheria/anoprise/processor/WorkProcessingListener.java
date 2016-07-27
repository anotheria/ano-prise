package net.anotheria.anoprise.processor;

import java.util.List;

/**
 * Listener of  
 * @author dmetelin
 *
 * @param <E>
 */
public interface WorkProcessingListener <E> {

	void workStarted(List<E> workingPackage);
	
	void workFinished(List<E> workingPackage, long workDuration);
	
	void workInterrupted(List<E> workingPackage);
	
}
