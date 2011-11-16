package net.anotheria.anoprise.sessiondistributor.events;

import java.util.List;

/**
 * SessionCleanUpEvent.
 *
 * @author h3ll
 */

public class SessionCleanUpEvent extends SessionDistributorEvent {

	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * SessionCleanUpEvent 'sessionIds'.
	 */
	private List<String> sessionIds;

	/**
	 * Constructor.
	 *
	 * @param aSessionIds expired ids collection
	 */
	public SessionCleanUpEvent(List<String> aSessionIds) {
		super(SessionDistributorESOperations.SESSION_CLEAN_UP);

		this.sessionIds = aSessionIds;
	}

	public List<String> getSessionIds() {
		return sessionIds;
	}

	@Override
	public String toString() {
		return super.toString() +
				"sessionIds=" + sessionIds.size();
	}
}
