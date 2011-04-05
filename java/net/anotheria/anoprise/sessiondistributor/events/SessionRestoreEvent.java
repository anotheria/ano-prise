package net.anotheria.anoprise.sessiondistributor.events;

/**
 * SessionRestoreEvent.
 *
 * @author h3ll
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

	public String getServiceId() {
		return serviceId;
	}

	public String getSessionId() {
		return sessionId;
	}


	@Override
	public String toString() {
		return super.toString() +
				", referenceId='" + serviceId + '\'' +
				", sessionId='" + sessionId + '\'';

	}
}
