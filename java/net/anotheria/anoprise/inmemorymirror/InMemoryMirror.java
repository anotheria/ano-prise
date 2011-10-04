package net.anotheria.anoprise.inmemorymirror;

import java.util.Collection;

public interface InMemoryMirror<K,V extends Mirrorable<K>> {
	Collection<V> getAll();
	
	V get(K id);
	
	V remove(K id);
	
	void update(V element);
	
	V create(V element);
}
