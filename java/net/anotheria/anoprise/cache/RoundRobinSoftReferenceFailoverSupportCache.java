package net.anotheria.anoprise.cache;

/**
 * A simple RoundRobin implementation of the cache with failover support.
 *
 * @author ivanbatura
 */
public class RoundRobinSoftReferenceFailoverSupportCache<K, V> extends RoundRobinSoftReferenceCache<K, V> {
	/**
	 * Default which defines PropertyBasedRegistrationNameProvider parameter!
	 */
	public static final String DEF_REGISTRATION_NAME_PROVIDER = "extension";


	private int serviceAmount;

	private String registrationNameProvider;
	/**
	 * modable value calculator
	 */
	private ModableTypeHandler modableTypeHandler;

//	public RoundRobinSoftReferenceFailoverSupportCache(int serviceAmount, String registrationNameProvider) {
//		this(getUnnamedInstanceName(RoundRobinSoftReferenceFailoverSupportCache.class), super.DEF_START_SIZE, super.DEF_MAX_SIZE, super.DEF_INCREMENT, serviceAmount, registrationNameProvider, null);
//	}

	public RoundRobinSoftReferenceFailoverSupportCache(int aStartSize, int aMaxSize, int serviceAmount, String registrationNameProvider) {
		this(getUnnamedInstanceName(RoundRobinSoftReferenceFailoverSupportCache.class), aStartSize, aMaxSize, serviceAmount, registrationNameProvider, null);
	}

	public RoundRobinSoftReferenceFailoverSupportCache(String name, int aStartSize, int aMaxSize, int serviceAmount, String registrationNameProvider, ModableTypeHandler modableTypeHandler) {
		super(name, aStartSize, aMaxSize);
		this.serviceAmount = serviceAmount;
		this.registrationNameProvider = (registrationNameProvider == null) ? DEF_REGISTRATION_NAME_PROVIDER : registrationNameProvider;
		this.modableTypeHandler = (modableTypeHandler == null) ? new DefaultModableTypeHandler() : modableTypeHandler;
		init();
	}

	@Override
	public void remove(K id) {
		if (!isFailOverCall(id))
			super.remove(id);
	}

	@Override
	public V get(K id) {
		if (isFailOverCall(id))
			return null;
		return super.get(id);
	}

	@Override
	public void put(K id, V cacheable) {
		if (!isFailOverCall(id))
			super.put(id, cacheable);
	}

	@Override
	public void clear() {
		super.clear();
	}

	@Override
	public String toString() {
		return super.toString() + " , ServiceNodeAmount=" + serviceAmount;
	}

	/**
	 * Detect if this call was for failover or not
	 *
	 * @param id cache key to stored
	 * @return
	 */
	private boolean isFailOverCall(Object id) {
		return FailOverHelper.isFailOverCall(registrationNameProvider, serviceAmount, Math.abs(modableTypeHandler.getModableValue(id)));
	}

	/**
	 * cache init
	 */
	private void init() {
		clear();
	}
}