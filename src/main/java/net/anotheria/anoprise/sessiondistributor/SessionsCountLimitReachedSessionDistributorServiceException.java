package net.anotheria.anoprise.sessiondistributor;

/**
 * SessionDistributorServiceException - which notifies that max session amount - which was   configured, is  already reached, and further distributed - session creation
 * impossible...
 *
 * @author h3llka
 */
public class SessionsCountLimitReachedSessionDistributorServiceException extends SessionDistributorServiceException {
	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = 6434749424462331680L;

	/**
	 * Constructor.
	 *
	 * @param maxSessionsCount currently configured limit
	 */
	public SessionsCountLimitReachedSessionDistributorServiceException(int maxSessionsCount) {
		super("Max DistributedSessions count :" + maxSessionsCount + ":  reached");
	}
}
