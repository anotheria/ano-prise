package net.anotheria.anoprise.metafactory;

import java.util.Collection;

/**
 * This helper class is used by the MetaFactory to resolve conflicts.
 *
 * @author lrosenberg
 * @since 07.01.13 23:36
 * @version $Id: $Id
 */
public interface OnTheFlyConflictResolver {
	/**
	 * Returns one class of the list, if the conflict resolver knows which to pick, or null.
	 *
	 * @param candidates a {@link java.util.Collection} object.
	 * @param <T> a T object.
	 * @return a {@link java.lang.Class} object.
	 */
	<T> Class<? extends T> resolveConflict(Collection<Class<? extends T>> candidates);
}
