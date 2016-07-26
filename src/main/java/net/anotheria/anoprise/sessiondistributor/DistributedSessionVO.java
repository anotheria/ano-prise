package net.anotheria.anoprise.sessiondistributor;

import net.anotheria.util.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DistributedSession value object used in SessionDistributorService.
 *
 * @author lrosenberg
 * @version 1.0, 2010/01/03
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
		distributedAttributes = new ConcurrentHashMap<>();
		lastChangeTime = System.currentTimeMillis();
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getLastChangeTime() {
		return lastChangeTime;
	}

	public void setLastChangeTime(long lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEditorId() {
		return editorId;
	}

	public void setEditorId(String editorId) {
		this.editorId = editorId;
	}



	public Map<String, DistributedSessionAttribute> getDistributedAttributes() {
		return distributedAttributes;
	}


	public void setDistributedAttributes(Map<String, DistributedSessionAttribute> aDistributedAttributes) {
		if (aDistributedAttributes == null)
			throw new IllegalArgumentException("distributedAttributes can't be null");
		this.distributedAttributes = aDistributedAttributes;
	}

	public void addDistributedAttribute(DistributedSessionAttribute attribute) {
		if (attribute == null)
			throw new IllegalArgumentException("Distributed attribute can't be null");
		distributedAttributes.put(attribute.getName(), attribute);

	}

	public void removeDistributedAttribute(String attributeName) {
		if (StringUtils.isEmpty(attributeName))
			throw new IllegalArgumentException("Distributed attribute name is illegal");
		if (distributedAttributes.containsKey(attributeName))
			distributedAttributes.remove(attributeName);
	}

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
        return System.currentTimeMillis() - lastChangeTime > serviceConfig.getDistributedSessionMaxAge();
	}
}
