package net.anotheria.anoprise.inmemorymirror;

import java.util.Collection;

/**
 * The InMemoryMirror allows to hold a copy of some small portion of data in memory.
 * @author lrosenberg
 *
 */
public interface InMemoryMirror<K,V extends Mirrorable<K>> {
	/**
	 * Returns all elements as a collection.
	 */
	Collection<V> getAll() throws InMemoryMirrorException;
	/**
	 * Returns the element with given key.
	 */
	V get(K id) throws InMemoryMirrorException;
	/**
	 * Removes the element with given key, returns the element previously associated with the key if present, null otherwise.
	 */
	V remove(K id) throws InMemoryMirrorException;
	/**
	 * Removes the element with given key local only (without InMemorySupport call), returns the element previously associated with the key if present, null otherwise.
	 */
	V removeLocalOnly(K id) throws InMemoryMirrorException;
	/**
	 * Updates the given element.
	 */
	void update(V element) throws InMemoryMirrorException;
	/**
	* Updates the given element local only (without InMemorySupport call).
	*/
	void updateLocalOnly(V element) throws InMemoryMirrorException;
	/**
	 * Creates a new element out of parameter element, returns the new element as its stored in underlying storage.
	 */
	V create(V element) throws InMemoryMirrorException;
	/**
	 * Creates a new element out of parameter element local only (without InMemorySupport call), returns the new element as its stored in underlying storage.
	 */
	V createLocalOnly(V element) throws InMemoryMirrorException;
}
