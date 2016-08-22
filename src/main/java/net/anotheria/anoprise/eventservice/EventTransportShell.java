package net.anotheria.anoprise.eventservice;

import java.io.Serializable;

/**
 * TODO Please remind lrosenberg to comment this class.
 *
 * @author lrosenberg
 * Created on 22.09.2004
 * @version $Id: $Id
 */
public class EventTransportShell implements Serializable{
	private static final long serialVersionUID = 42L;

	private String channelName;
	private byte[] data;
	/**
	 * <p>Getter for the field <code>channelName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * <p>Getter for the field <code>data</code>.</p>
	 *
	 * @return an array of byte.
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * <p>Setter for the field <code>channelName</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setChannelName(String string) {
		channelName = string;
	}

	/**
	 * <p>Setter for the field <code>data</code>.</p>
	 *
	 * @param bs an array of byte.
	 */
	public void setData(byte[] bs) {
		data = bs;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return (data == null ? "no" : ""+data.length) + " bytes for "+channelName;
	}

}
