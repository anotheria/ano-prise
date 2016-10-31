package net.anotheria.anoprise.sessiondistributor.events;

/**
 * SessionRestoreEvent.
 *
 * @author h3ll
 * @version $Id: $Id
 */
public class SessionRestoreEvent extends SessionDistributorEvent {

	/**
	 * Basic serial version uid.
	 */
	private static final long serialVersionUID = 3238743326936257297L;
	/**
	 * SessionRestoreEvent 'sessionId'.
	 */
	private String sessionId;
	/**
	 * SessionRestoreEvent 'serviceId'.
	 */
	private String serviceId;

	/**
	 * constructor.
	 *
	 * @param aSessionId restored session id
	 * @param aCallerId caller service id
	 */
	public SessionRestoreEvent(String aSessionId, String aCallerId) {
		super(SessionDistributorESOperations.SESSION_RESTORE);
		this.sessionId = aSessionId;
		this.serviceId = aCallerId;
	}

	/**
	 * <p>Getter for the field <code>serviceId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * <p>Getter for the field <code>sessionId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSessionId() {
		return sessionId;
	}


	/** {@inheritDoc} */
	@Override
	public String toString() {
		return super.toString() +
				", referenceId='" + serviceId + '\'' +
				", sessionId='" + sessionId + '\'';

	}
}
