package net.anotheria.anoprise.sessiondistributor;

import java.io.Serializable;

public class SessionAttribute implements Serializable{
	/**
	 * Name of the attribute.
	 */
	private String name;
	/**
	 * Stored serialized data.
	 */
	private byte[] data;
	
	public SessionAttribute(String aName, byte[] someData){
		name = aName;
		data = someData;
	}
	
	@Override public String toString(){
		return getName()+" with "+getDataSize()+" bytes.";
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

	private int getDataSize(){
		return data == null ? 0 : data.length;
	}
	
}
