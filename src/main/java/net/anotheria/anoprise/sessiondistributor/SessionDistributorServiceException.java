package net.anotheria.anoprise.sessiondistributor;

/**
 * SessionDistributorServiceException exception.
 * 
 * @author lrosenberg
 * @version 1.0, 2010/01/03
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
