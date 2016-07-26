package net.anotheria.anoprise.eventservice;

import java.io.Serializable;

/**
 * TODO Please remind lrosenberg to comment this class.
 * @author lrosenberg
 * Created on 22.09.2004
 */
public class EventTransportShell implements Serializable{
	private static final long serialVersionUID = 42L;

	private String channelName;
	private byte[] data;
	/**
	 * @return
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * @return
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param string
	 */
	public void setChannelName(String string) {
		channelName = string;
	}

	/**
	 * @param bs
	 */
	public void setData(byte[] bs) {
		data = bs;
	}
	
	@Override public String toString(){
		return (data == null ? "no" : String.valueOf(data.length)) + " bytes for "+channelName;
	}

}
