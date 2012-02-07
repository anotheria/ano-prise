package net.anotheria.anoprise.sessiondistributor.cache.events;

import net.anotheria.anoprise.sessiondistributor.DistributedSessionVO;

import java.io.Serializable;

/**
 * Session distribution cache event.
 *
 * @author h3ll
 */
public class SDCacheEvent implements Serializable {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = -4549050652142432692L;
	/**
	 * {@link SDCacheESOperations} instance.
	 */
	private SDCacheESOperations operation;
	/**
	 * {@link DistributedSessionVO} session.
	 */
	private DistributedSessionVO session;
	/**
	 * Id of the node in cluster. ( some generated str).
	 */
	private String clusterNodeId;

	/**
	 * Constructor.
	 *
	 * @param aOperation	 {@link SDCacheEvent}
	 * @param aSession	   {@link DistributedSessionVO}
	 * @param aClusterNodeId id of the sender node
	 */
	private SDCacheEvent(SDCacheESOperations aOperation, DistributedSessionVO aSession, String aClusterNodeId) {
		this.operation = aOperation;
		this.session = aSession;
		this.clusterNodeId = aClusterNodeId;
	}

	public SDCacheESOperations getOperation() {
		return operation;
	}

	public DistributedSessionVO getSession() {
		return session;
	}

	public String getClusterNodeId() {
		return clusterNodeId;
	}

	@Override
	public String toString() {
		return "SDCacheEvent{" +
				"operation=" + operation +
				", session=" + session +
				", clusterNodeId='" + clusterNodeId + '\'' +
				'}';
	}

	/**
	 * Creates new Save event.
	 *
	 * @param nodeId	id of the sender node
	 * @param sessionVO {@link DistributedSessionVO}
	 * @return save event
	 */
	public static SDCacheEvent save(String nodeId, DistributedSessionVO sessionVO) {
		return new SDCacheEvent(SDCacheESOperations.CACHE_SESSION_SAVE, sessionVO, nodeId);
	}

	/**
	 * Creates new Save event.
	 *
	 * @param nodeId	id of the sender node
	 * @param sessionVO {@link DistributedSessionVO}
	 * @return save event
	 */
	public static SDCacheEvent delete(String nodeId, DistributedSessionVO sessionVO) {
		return new SDCacheEvent(SDCacheESOperations.CACHE_SESSION_SAVE, sessionVO, nodeId);
	}
}
