package net.anotheria.anoprise.metafactory;

/**
 * Alias resolver based on system properties.
 *
 * @author lrosenberg.
 * @version $Id: $Id
 */
public class SystemPropertyResolver implements AliasResolver{
	
	/**
	 * Prefix for system properties understood by this resolver.
	 */
	public static final String PROPERTY_PREFIX = "ano.doc.mf-alias";

	/** {@inheritDoc} */
	@Override
	public int getPriority() {
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public String resolveAlias(String alias) {
		return System.getProperty(PROPERTY_PREFIX+alias);
	}
}
