package net.anotheria.anoprise.fs;

/**
 * Exception used in {@link FSServiceConfig}.
 * 
 * @author abolbat
 * @version 1.0, 2010/02/11
 */
public class FSServiceConfigException extends FSServiceException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 5134391442868864394L;

	/**
	 * Creates a new {@link FSServiceConfigException} with a message.
	 * 
	 * @param message
	 *            - exception message
	 */
	public FSServiceConfigException(String message) {
		super(message);
	}

	/**
	 * Creates a new {@link FSServiceConfigException} with a message and a cause.
	 * 
	 * @param message
	 *            - exception message
	 * @param cause
	 *            - exception cause
	 */
	public FSServiceConfigException(String message, Throwable cause) {
		super(message, cause);
	}

}
