package net.anotheria.anoprise.inmemorymirror;

import java.util.Collection;

public interface InMemorySupport<K,V extends Mirrorable<K>> {
	Collection<V> readAll();
	
	V create(V element);
	
	V update(V element);
	
	V remove (K key);
}
