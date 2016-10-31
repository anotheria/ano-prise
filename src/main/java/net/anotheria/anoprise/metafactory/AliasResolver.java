package net.anotheria.anoprise.metafactory;

/**
 * Interface for an resolver used by MetaFactory.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public interface AliasResolver {
	
	/**
	 * Resolves an alias. If the alias can't be resolved, returns null. Otherwise the resolved alias, which can be an alias himself, is returned.
	 *
	 * @param alias a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	String resolveAlias(String alias);
	
	/**
	 * Returns the priority of this resolver. The
	 *
	 * @return a int.
	 */
	int getPriority();
}
 
