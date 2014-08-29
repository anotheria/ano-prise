package net.anotheria.anoprise.cache;

/**
 * Wrapper object. This object is used for expiration caches to add the timestamp of the object creation.
 * @author lrosenberg
 * @param <T> a cacheable that we wrap.
 */
public class CachedObjectWrapper<T> {
	/**
	 * The wrapped object.
	 */
	private T obj;
	/**
	 * Timestamp of wrapper creation.
	 */
	private long timestamp;
	
	public CachedObjectWrapper(T anObject){
		obj = anObject;
		timestamp = System.currentTimeMillis();
	}

	/**
	 * Returns the underlying object.
	 * @return
	 */
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
