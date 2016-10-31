package net.anotheria.anoprise.sessiondistributor;

/**
 * NoSuchDistributedSessionException exception.
 *
 * @author lrosenberg
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
