package net.anotheria.anoprise.sessiondistributor;

import java.io.Serializable;

/**
 * SessionAttribute used in SessionDestributorService.
 * 
 * @author lrosenberg
 * @version 1.0, 2010/01/03
 */
public class SessionAttribute implements Serializable {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 3161203681531772457L;

	/**
	 * Name of the attribute.
	 */
	private String name;

	/**
	 * Stored serialized data.
	 */
	private byte[] data;

	/**
	 * Default constructor.
	 * 
	 * @param aName
	 *            - name
	 * @param someData
	 *            - data
	 */
	public SessionAttribute(String aName, byte[] someData) {
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
		return getName() + " with " + getDataSize() + " bytes.";
	}

}
