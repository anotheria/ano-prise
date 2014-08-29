package net.anotheria.anoprise.dataspace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.anotheria.anoprise.dataspace.attribute.Attribute;
import net.anotheria.util.BasicComparable;

/**
 * Dataspace used in DataspaceService.
 * 
 * @author lrosenberg
 */
public class Dataspace implements Serializable, Cloneable {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 8228635784687134917L;

	/**
	 * User id.
	 */
	private String userId;

	/**
	 * Dataspace type.
	 */
	private DataspaceType dataspaceType;

	/**
	 * Dataspace attributes.
	 */
	private Map<String, Attribute> attributes = new HashMap<String, Attribute>();

	/**
	 * Default constructor.
	 * 
	 * @param aUserId
	 *            - user id
	 * @param aDataspaceType
	 *            - dataspace type
	 */
	public Dataspace(String aUserId, DataspaceType aDataspaceType) {
		this.userId = aUserId;
		this.dataspaceType = aDataspaceType;
	}

	public void setUserId(String aUserId) {
		this.userId = aUserId;
	}

	public String getUserId() {
		return userId;
	}

	public void setDataspaceType(DataspaceType aDataspaceType) {
		this.dataspaceType = aDataspaceType;
	}

	public DataspaceType getDataspaceType() {
		return dataspaceType;
	}

	/**
	 * Get attribute by given name from dataspace.
	 * 
	 * @param attributeName
	 *            - attribute name
	 * @return - attribute or null if no attribute with given name
	 */
	public Attribute getAttribute(String attributeName) {
		return attributes.get(attributeName);
	}

	/**
	 * Get all attributes.
	 * 
	 * @return {@link List} with all attributes
	 */
	public List<Attribute> getAttributes() {
		return new ArrayList<Attribute>(attributes.values());
	}

	/**
	 * Add new attribute to dataspace.
	 * 
	 * @param attributeName
	 *            - new attribute name
	 * @param attribute
	 *            - new attribute
	 */
	public void addAttribute(String attributeName, Attribute attribute) {
		attributes.put(attributeName, attribute);
	}

	/**
	 * Remove attribute by given name from dataspace.
	 * 
	 * @param attributeName
	 *            - given attribute name
	 */
	public void removeAttribute(String attributeName) {
		attributes.remove(attributeName);
	}

	@Override
	public String toString() {
		return "Dataspace [attributes=" + attributes + ", dataspaceTypeId=" + dataspaceType.getId() + ", userId=" + userId + "]";
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Dataspace ? BasicComparable.compareString(userId, ((Dataspace) o).userId) == 0
				&& dataspaceType.getId() == ((Dataspace) o).dataspaceType.getId() : false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dataspaceType.getId();
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
