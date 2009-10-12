package net.anotheria.anoprise.cache;

public class CachedObjectWrapper<T> {
	private T obj;
	private long timestamp;
	
	public CachedObjectWrapper(T anObject){
		obj = anObject;
		timestamp = System.currentTimeMillis();
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T anObj) {
		obj = anObj;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
