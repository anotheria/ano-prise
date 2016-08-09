package net.anotheria.anoprise.fs;

/**
 * Basic exception of the {@link FSService} interface.
 * 
 * @author abolbat
 * @version 1.0, 2010/02/11
 */
public class FSServiceException extends Exception {

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

	public FSServiceException(Throwable cause) {
		super(cause);
	}
}
