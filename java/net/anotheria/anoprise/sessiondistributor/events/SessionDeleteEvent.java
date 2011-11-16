package net.anotheria.anoprise.sessiondistributor.events;

/**
 * SessionDeleteEvent.
 *
 * @author h3ll
 */

public class SessionDeleteEvent extends SessionDistributorEvent {
	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * SessionDeleteEvent 'sessionId'.
	 */
	private String sessionId;

	/**
	 * Constructor.
	 *
	 * @param aSessionId id of the  session
	 */
	public SessionDeleteEvent(String aSessionId) {
		super(SessionDistributorESOperations.SESSION_DELETE);
		this.sessionId = aSessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	@Override
	public String toString() {
		return super.toString() +
				", sessionId='" + sessionId + '\'' +
				'}';
	}
}
