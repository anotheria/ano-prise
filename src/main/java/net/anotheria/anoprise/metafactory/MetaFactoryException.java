package net.anotheria.anoprise.metafactory;

/**
 * Exception type thrown by the meta factory.
 * 
 * @author lrosenberg
 * @version 1.0, 2010/02/02
 */
public class MetaFactoryException extends Exception {

	public MetaFactoryException(String message) {
		super(message);
	}

	public MetaFactoryException(String message, Throwable cause) {
		super(message, cause);
	}
}
