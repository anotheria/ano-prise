package net.anotheria.anoprise.dualcrud;

/**
 * Basic exception of the crud service interface.
 * @author lrosenberg
 *
 */
public class CrudServiceException extends Exception {
	/**
	 * Creates a new CrudServiceException with a message.
	 * @param message
	 */
	public CrudServiceException(String message){
		super(message);
	}
	
	/**
	 * Creates a new CrudServiceException with a message and a cause.
	 * @param message
	 * @param cause
	 */
	public CrudServiceException(String message, Throwable cause){
		super(message, cause);
	}
}
