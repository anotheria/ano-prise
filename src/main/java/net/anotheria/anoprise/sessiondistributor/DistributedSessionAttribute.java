package net.anotheria.anoprise.sessiondistributor;

import java.io.Serializable;

/**
 * DistributedSessionAttribute used in SessionDistributorService.
 *
 * @author lrosenberg
 * @version 1.0, 2010/01/03
 */
public class DistributedSessionAttribute implements Serializable {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 3161203681531772457L;

	/**
	 * DistributedSessionAttribute 'name'.
	 * Name of the attribute.
	 */
	private String name;

	/**
	 * DistributedSessionAttribute 'data'.
	 * Stored serialized data.
	 */
	private byte[] data;

	/**
	 * Default constructor.
	 *
	 * @param aName	- name
	 * @param someData - data
	 */
	public DistributedSessionAttribute(String aName, byte[] someData) {
		name = aName;
		data = someData;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	private int getDataSize() {
		return data == null ? 0 : data.length;
	}

	@Override
	public String toString() {
        return name + ",  with " + getDataSize() + " bytes.";
	}

	@Override
	public boolean equals(Object o) {
        return o == this || ((o instanceof DistributedSessionAttribute) && ((DistributedSessionAttribute) o).name.equals(name));
	}

	@Override
	public int hashCode() {
        return name != null ? name.hashCode() : 0;
	}
}
