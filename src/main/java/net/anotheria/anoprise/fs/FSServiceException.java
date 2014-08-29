package net.anotheria.anoprise.fs;

/**
 * Basic exception of the {@link FSService} interface.
 * 
 * @author abolbat
 * @version 1.0, 2010/02/11
 */
public class FSServiceException extends Exception {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -8250719745502463566L;

	/**
	 * Creates a new {@link FSServiceException} with a message.
	 * 
	 * @param message
	 *            - exception message
	 */
	public FSServiceException(String message) {
		super(message);
	}

	/**
	 * Creates a new {@link FSServiceException} with a message and a cause.
	 * 
	 * @param message
	 *            - exception message
	 * @param cause
	 *            - exception cause
	 */
	public FSServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
