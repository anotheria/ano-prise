package net.anotheria.anoprise.inmemorymirror;

import java.util.Collection;

/**
 * The InMemoryMirror allows to hold a copy of some small portion of data in memory.
 * @author lrosenberg
 *
 * @param <K>
 * @param <V>
 */
public interface InMemoryMirror<K,V extends Mirrorable<K>> {
	/**
	 * Returns all elements as a collection.
	 * @return
	 */
	Collection<V> getAll() throws InMemoryMirrorException;
	/**
	 * Returns the element with given key.
	 * @param id
	 * @return
	 */
	V get(K id) throws InMemoryMirrorException;
	/**
	 * Removes the element with given key, returns the element previously associated with the key if present, null otherwise.
	 * @param id
	 * @return
	 */
	V remove(K id) throws InMemoryMirrorException;
	/**
	 * Removes the element with given key local only (without InMemorySupport call), returns the element previously associated with the key if present, null otherwise.
	 * @param id
	 * @return
	 */
	V removeLocalOnly(K id) throws InMemoryMirrorException;
	/**
	 * Updates the given element.
	 * @param element
	 */
	void update(V element) throws InMemoryMirrorException;
	/**
	* Updates the given element local only (without InMemorySupport call).
	* @param element
	*/
	void updateLocalOnly(V element) throws InMemoryMirrorException;
	/**
	 * Creates a new element out of parameter element, returns the new element as its stored in underlying storage.
	 * @param element
	 * @return
	 */
	V create(V element) throws InMemoryMirrorException;
	/**
	 * Creates a new element out of parameter element local only (without InMemorySupport call), returns the new element as its stored in underlying storage.
	 * @param element
	 * @return
	 */
	V createLocalOnly(V element) throws InMemoryMirrorException;
}
