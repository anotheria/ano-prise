package net.anotheria.anoprise.metafactory;

/**
 * Thrown if there is no configured factory for the given name.
 * 
 * @author lrosenberg
 * @version 1.0, 2010/02/02
 */
public class FactoryNotFoundException extends MetaFactoryException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 6531612155374284127L;

	/**
	 * Default constructor.
	 * 
	 * @param name
	 *            - service name
	 */
	public FactoryNotFoundException(String name) {
		super("No factory for service " + name + " found");
	}

}
