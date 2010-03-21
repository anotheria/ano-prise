package net.anotheria.anoprise.dataspace;

/**
 * Dataspace exception used in DataspaceService. Throwed on any service exception.
 * 
 * @author lrosenberg
 */
public class DataspaceServiceException extends Exception {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 9119716783761296666L;

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            - exception message
	 */
	public DataspaceServiceException(String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * 
	 * @param message
	 *            - exception message
	 * @param cause
	 *            - exception cause
	 */
	public DataspaceServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
