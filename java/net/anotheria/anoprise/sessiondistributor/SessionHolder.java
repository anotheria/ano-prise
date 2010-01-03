package net.anotheria.anoprise.sessiondistributor;

import java.util.List;

public class SessionHolder {
	private String name;
	private long timestamp;
	private List<SessionAttribute> attributes;
	
	
	
	public SessionHolder(String aName, List<SessionAttribute> someAttributes){
		name = aName;
		attributes = someAttributes;
		timestamp = System.currentTimeMillis();
	}
	
	@Override public String toString(){
		return getName()+" with "+getAttributes().size()+" attributes, "+((System.currentTimeMillis()-timestamp)/1000)+" seconds old.";
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
}
