package net.anotheria.anoprise.metafactory;

/**
 * Alias resolver based on system properties.
 * @author lrosenberg.
 *
 */
public class SystemPropertyResolver implements AliasResolver{
	
	/**
	 * Prefix for system properties understood by this resolver.
	 */
	public static final String PROPERTY_PREFIX = "ano.doc.mf-alias";

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String resolveAlias(String alias) {
		return System.getProperty(PROPERTY_PREFIX+alias);
	}
}
