package net.anotheria.anoprise.dualcrud;

/**
 * Basic exception of the {@link CrudService} interface.
 * 
 * @author lrosenberg
 * @version 1.0, 2010/02/02
 */
public class CrudServiceException extends Exception {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -1846792906505930875L;

	/**
	 * Creates a new CrudServiceException with a message.
	 * 
	 * @param message
	 *            - exception message
	 */
	public CrudServiceException(String message) {
		super(message);
	}

	/**
	 * Creates a new CrudServiceException with a message and a cause.
	 * 
	 * @param message
	 *            - exception message
	 * @param cause
	 *            - exception cause
	 */
	public CrudServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
