package net.anotheria.anoprise.sessiondistributor;

/**
 * NoSuchDistributedSessionException exception.
 * 
 * @author lrosenberg
 * @version 1.0, 2010/01/03
 */
public class NoSuchDistributedSessionException extends SessionDistributorServiceException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -5351100238219718490L;

	/**
	 * Default constructor.
	 * 
	 * @param name
	 *            - exception message
	 */
	public NoSuchDistributedSessionException(String name) {
		super("No such distributed session: " + name);
	}
}
