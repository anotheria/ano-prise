package net.anotheria.anoprise.inmemorymirror;

import java.util.Collection;
/**
 * This support interface backs the InMemoryMirror instance and provides crud operations on the underlying data set.
 * @author lrosenberg
 *
 * @param <K>
 * @param <V>
 */
public interface InMemorySupport<K,V extends Mirrorable<K>> {
	Collection<V> readAll();
	
	V create(V element);
	
	V update(V element) throws InMemoryMirrorException;
	
	V remove (K key);
}
