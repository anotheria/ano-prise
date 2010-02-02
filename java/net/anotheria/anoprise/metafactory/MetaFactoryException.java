package net.anotheria.anoprise.metafactory;

/**
 * Exception type thrown by the meta factory.
 * 
 * @author lrosenberg
 * @version 1.0, 2010/02/02
 */
public class MetaFactoryException extends Exception {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -682699140633349261L;

	/**
	 * Default constructor.
	 * 
	 * @param name
	 *            - exception message
	 */
	public MetaFactoryException(String message) {
		super(message);
	}
}
