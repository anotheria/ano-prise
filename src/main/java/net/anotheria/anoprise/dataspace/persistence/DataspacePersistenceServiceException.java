package net.anotheria.anoprise.dataspace.persistence;

/**
 * Dataspace persistence exception used in DataspacePersistenceService. Throwed on any service exception.
 * 
 * @author lrosenberg
 */
public class DataspacePersistenceServiceException extends Exception {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -9057488151759805663L;

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            - exception message
	 */
	public DataspacePersistenceServiceException(String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            - exception message
	 * @param cause
	 *            - exception cause
	 */
	public DataspacePersistenceServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataspacePersistenceServiceException(Throwable cause) {
		super(cause);
	}
}
