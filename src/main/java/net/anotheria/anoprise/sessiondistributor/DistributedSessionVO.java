package net.anotheria.anoprise.sessiondistributor;

import net.anotheria.util.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DistributedSession value object used in SessionDistributorService.
 *
 * @author lrosenberg
 */
public class DistributedSessionVO implements Serializable {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -2764699143075615769L;

	/**
	 * SessionDistributorServiceConfig instance.
	 */
	private static SessionDistributorServiceConfig serviceConfig;

	/**
	 * Static initialization block.
	 */
	static {
		serviceConfig = SessionDistributorServiceConfig.getInstance();
	}

	/**
	 * DistributedSessionVO 'name'.
	 * Actually id of associated session.
	 */
	private String name;

	/**
	 * DistributedSessionVO 'lastChangeTime'.
	 * Last session change time.
	 * First will be initialized in constructor.
	 */
	private long lastChangeTime;

	/**
	 * DistributedSessionVO 'userId'.
	 */
	private String userId;
	/**
	 * DistributedSessionVO 'editorId'.
	 */
	private String editorId;


	/**
	 * DistributedSessionVO 'distributedAttributes'.
	 */
	private Map<String, DistributedSessionAttribute> distributedAttributes;

	/**
	 * Default constructor.
	 *
	 * @param aName - holder name
	 */
	public DistributedSessionVO(String aName) {
		name = aName;
		distributedAttributes = new ConcurrentHashMap<String, DistributedSessionAttribute>();
		lastChangeTime = System.currentTimeMillis();
	}


	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p>Getter for the field <code>lastChangeTime</code>.</p>
	 *
	 * @return a long.
	 */
	public long getLastChangeTime() {
		return lastChangeTime;
	}

	/**
	 * <p>Setter for the field <code>lastChangeTime</code>.</p>
	 *
	 * @param lastChangeTime a long.
	 */
	public void setLastChangeTime(long lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}

	/**
	 * <p>Getter for the field <code>userId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * <p>Setter for the field <code>userId</code>.</p>
	 *
	 * @param userId a {@link java.lang.String} object.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Getter for the field <code>editorId</code>.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getEditorId() {
		return editorId;
	}

	/**
	 * Setter for the field <code>editorId</code>.
	 *
	 * @param editorId a {@link java.lang.String} object.
	 */
	public void setEditorId(String editorId) {
		this.editorId = editorId;
	}



	/**
	 * Getter for the field <code>distributedAttributes</code>.
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, DistributedSessionAttribute> getDistributedAttributes() {
		return distributedAttributes;
	}


	/**
	 * Setter for the field <code>distributedAttributes</code>.
	 *
	 * @param aDistributedAttributes a {@link java.util.Map} object.
	 */
	public void setDistributedAttributes(Map<String, DistributedSessionAttribute> aDistributedAttributes) {
		if (aDistributedAttributes == null)
			throw new IllegalArgumentException("distributedAttributes can't be null");
		this.distributedAttributes = aDistributedAttributes;
	}

	/**
	 * <p>addDistributedAttribute.</p>
	 *
	 * @param attribute a {@link net.anotheria.anoprise.sessiondistributor.DistributedSessionAttribute} object.
	 */
	public void addDistributedAttribute(DistributedSessionAttribute attribute) {
		if (attribute == null)
			throw new IllegalArgumentException("Distributed attribute can't be null");
		distributedAttributes.put(attribute.getName(), attribute);

	}

	/**
	 * <p>removeDistributedAttribute.</p>
	 *
	 * @param attributeName a {@link java.lang.String} object.
	 */
	public void removeDistributedAttribute(String attributeName) {
		if (StringUtils.isEmpty(attributeName))
			throw new IllegalArgumentException("Distributed attribute name is illegal");
		if (distributedAttributes.containsKey(attributeName))
			distributedAttributes.remove(attributeName);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "DistributedSessionVO{" +
				"distributedAttributes size=" + distributedAttributes.size() +
				", name='" + name + '\'' +
				", lastChangeTime=" + ((System.currentTimeMillis() - lastChangeTime) / 1000) + " seconds ago" +
				", userId='" + userId + '\'' +
				", editorId='" + editorId + '\'' +
				'}';
	}

	/**
	 * Return true if session is expired. False otherwise.
	 *
	 * @return boolean value
	 */
	public boolean isExpired() {
		return System.currentTimeMillis() - getLastChangeTime() > serviceConfig.getDistributedSessionMaxAge();
	}
}
