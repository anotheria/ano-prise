package net.anotheria.anoprise.processor;

import java.util.List;

/**
 * Listener of  
 * @author dmetelin
 *
 * @param <E>
 */
public interface WorkProcessingListener <E> {

	public void workStarted(List<E>  workingPackage);
	
	public void workFinished(List<E>  workingPackage, long workDuration);
	
	public void workInterrupted(List<E>  workingPackage);
	
}
