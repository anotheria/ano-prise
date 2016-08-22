package net.anotheria.anoprise.sessiondistributor;

/**
 * SessionDistributorServiceException exception.
 *
 * @author lrosenberg
 */
public class SessionDistributorServiceException extends Exception {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 91803490370090261L;

	/**
	 * Default constructor.
	 *
	 * @param message
	 *            - exception message
	 */
	public SessionDistributorServiceException(String message) {
		super(message);
	}
}
