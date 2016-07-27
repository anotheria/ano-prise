package net.anotheria.anoprise.metafactory;

import java.util.Collection;

/**
 * This helper class is used by the MetaFactory to resolve conflicts.
 *
 * @author lrosenberg
 * @since 07.01.13 23:36
 */
public interface OnTheFlyConflictResolver {
	/**
	 * Returns one class of the list, if the conflict resolver knows which to pick, or null.
	 */
	<T> Class<? extends T> resolveConflict(Collection<Class<? extends T>> candidates);
}
