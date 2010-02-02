package net.anotheria.anoprise.sessiondistributor;

import java.io.Serializable;
import java.util.List;

/**
 * SessionHolder used in SessionDestributorService.
 * 
 * @author lrosenberg
 * @version 1.0, 2010/01/03
 */
public class SessionHolder implements Serializable {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -2764699143075615769L;
	
	private static final long MAX_AGE_IN_MILLIS = 1000L*60*10; //10 min.

	/**
	 * Name of the associatted session.
	 */
	private String name;

	/**
	 * Timestamp of the object creation.
	 */
	private long timestamp;

	/**
	 * Stored attributes.
	 */
	private List<SessionAttribute> attributes;

	/**
	 * Default constructor.
	 * 
	 * @param aName
	 *            - holder name
	 * @param someAttributes
	 *            - attributes
	 */
	public SessionHolder(String aName, List<SessionAttribute> someAttributes) {
		name = aName;
		attributes = someAttributes;
		timestamp = System.currentTimeMillis();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public List<SessionAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<SessionAttribute> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return getName() + " with " + getAttributes().size() + " attributes, " + ((System.currentTimeMillis() - timestamp) / 1000) + " seconds old.";
	}
	
	public boolean isExpired(){
		return isExpiredForAge(MAX_AGE_IN_MILLIS);
	}
	
	/**
	 * This method is separate for testability.
	 * @param age
	 * @return
	 */
	boolean isExpiredForAge(long age){
		return System.currentTimeMillis() - getTimestamp() > age; 
	}
}
