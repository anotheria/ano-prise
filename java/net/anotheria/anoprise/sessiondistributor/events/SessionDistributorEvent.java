package net.anotheria.anoprise.sessiondistributor.events;

import java.io.Serializable;
import java.util.List;

/**
 * SessionDistributorService base event with factory methods.
 *
 * @author h3ll
 */
public abstract class SessionDistributorEvent implements Serializable {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = -5430148567160429537L;

	/**
	 * SessionDistributorEvent 'operation'.
	 */
	private SessionDistributorESOperations operation;

	/**
	 * Constructor.
	 *
	 * @param aOperation {@link SessionDistributorESOperations}
	 */
	protected SessionDistributorEvent(final SessionDistributorESOperations aOperation) {
		this.operation = aOperation;
	}

	/**
	 * Return event data operation.
	 *
	 * @return {@link SessionDistributorESOperations}
	 */
	public final SessionDistributorESOperations getOperation() {
		return operation;
	}

	/**
	 * Creates session restore event.
	 *
	 * @param sessionId id of the session
	 * @param callerId  some unique caller service id
	 * @return {@link SessionRestoreEvent}
	 */
	public static SessionDistributorEvent restore(final String sessionId, final String callerId) {
		return new SessionRestoreEvent(sessionId, callerId);
	}

	/**
	 * Creates session delete event.
	 *
	 * @param sessionId id of the session
	 * @return {@link SessionDeleteEvent}
	 */
	public static SessionDistributorEvent delete(final String sessionId) {
		return new SessionDeleteEvent(sessionId);
	}

	/**
	 * Creates session clenUp event.
	 *
	 * @param sessionIds ids collection
	 * @return {@link SessionDeleteEvent}
	 */
	public static SessionDistributorEvent cleanUp(final List<String> sessionIds) {
		return new SessionCleanUpEvent(sessionIds);
	}


	@Override
	public String toString() {
		return "operation=" + operation;

	}
}
