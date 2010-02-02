package net.anotheria.anoprise.eventservice;

/**
 * TODO Please remind lrosenberg to comment this class.
 * @author lrosenberg
 * Created on 22.09.2004
 */
public class EventTransportShell {
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

}
