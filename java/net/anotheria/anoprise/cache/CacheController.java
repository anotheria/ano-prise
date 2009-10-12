package net.anotheria.anoprise.cache;

import net.java.dev.moskito.core.predefined.CacheStats;

import org.apache.log4j.Logger;


/**
 * This class is a cache controller, which hides access to a configurable
 * cache, so the service itself do not need to check the configuration.
 * If the cache is on, the calls will be delegated to the cache, otherwise
 * they will be directly answered (returning null and puting nothing).
 * Currently RoundRobinCache is used as internal cache delegate.
 * @author lrosenberg
 * Created on 29.07.2004
 */
public class CacheController<K,V> implements Cache<K,V>{
	private boolean cacheOn;
	private int startSize;
	private int maxSize;
	 
	private boolean prevCacheOn;
	private int prevStartSize;
	private int prevMaxSize;
	
	private Cache<K,V> cache;
	
	private String configurationName;
	
	private int outOfMemoryErrors;
	
	public static final String PARAM_CACHE_ON = "cache.on";
	public static final String PARAM_START_SIZE = "cache.start.size";
	public static final String PARAM_MAX_SIZE = "cache.max.size";
	
	public static final String PARAM_FILTER   = "cache.filter";
	
	public static final boolean DEF_CACHE_ON = true;
	public static final int DEF_START_SIZE   = 1000;
	public static final int DEF_MAX_SIZE     = 5000;
	
	private CacheFactory<K,V> cacheFactory;
	
	private boolean oneTimeConfigured;
	
	private static Logger log;
	static {
		log = Logger.getLogger(CacheController.class);
	}
	
	public CacheController(String aConfigurationName){
		this(aConfigurationName, null);
		throw new AssertionError("This constructor is not yet complete");
	}

	public CacheController(String aConfigurationName, CacheFactory<K,V> factory){
		this.configurationName = aConfigurationName;
		cacheFactory = factory;
		oneTimeConfigured = false;
	}
	
	
	
	
	private void init(){
		if (!oneTimeConfigured){
			//enforce initial configuration.
			oneTimeConfigured = true;
			prevCacheOn = false;
			prevStartSize = -1;
			prevMaxSize = -1;
		}
		log.debug("reiniting cache for "+configurationName);
		if (!cacheOn){
			if (prevCacheOn){
				log.debug("switching cache off.");
				cache.clear();
				cache = null;
			}else{
				log.debug("cache remains off.");
			}
		}else{ 
			if (prevCacheOn){
				if (prevMaxSize == maxSize && prevStartSize == startSize){
					log.debug("Cache remains on, settings unchanged.");					
				}else{
					log.debug("Cache remains on, settings changed, cache will be renewed.");
					if (cache!=null)
						cache.clear();
					else
						log.warn("Cache is null, when it shouldn't be.");
					cache = cacheFactory.create(configurationName, startSize, maxSize);
				}
			}else{
				log.debug("switching cache on.");
				cache = createCache(startSize, maxSize);
			}
		}
	}
	
	protected Cache<K,V> createCache(int startSize, int maxSize){
		return cacheFactory.create(configurationName, startSize, maxSize);
	}
	
	@Override public void clear() {
		if (cacheOn)
			cache.clear();
	}

	@Override public V get(K id) {
		if (!cacheOn)
			return null;
		return cache.get(id);
	}

	@Override public void put(K id, V cacheable) {
		if (!cacheOn)
			return;
		try{
			cache.put(id, cacheable);
		}catch(OutOfMemoryError error){
			outOfMemoryErrors++;
			throw error;
		}
	}
	
	public void remove(K id){
		if (!cacheOn)
			return;
		cache.remove(id);
	}

	public String getConfigurationName() {
		return configurationName;
	}

	public void notifyConfigurationFinished() {
		log.info("configuration "+configurationName+" finished, settings are:");
		log.info("cacheOn "+prevCacheOn+" -> "+cacheOn);
		log.info("startSize "+prevStartSize+" -> "+startSize);
		log.info("maxSize "+prevMaxSize+" -> "+maxSize);
		init();
	}

	public void notifyConfigurationStarted() {
		prevCacheOn = cacheOn;
		prevMaxSize = maxSize;
		prevStartSize = startSize;
		cacheOn = DEF_CACHE_ON;
		startSize = DEF_START_SIZE;
		maxSize   = DEF_MAX_SIZE;
	}

	public void setProperty(String name, String value) {
		if (name.equals(PARAM_CACHE_ON))
			cacheOn = Boolean.valueOf(value).booleanValue();
		if (name.equals(PARAM_START_SIZE))
			startSize = Integer.parseInt(value);
		if (name.equals(PARAM_MAX_SIZE))
			maxSize = Integer.parseInt(value);
	}
	
	public String getStats(){
		String stats = cacheOn ? "On, "+startSize+", "+maxSize : "Off";
		if (cacheOn)
			stats += " " + cache.getCacheStats().toString()+", OOME: "+outOfMemoryErrors;
		return stats;
	}
	
	public String getDetails(){
		if (!cacheOn)
			return "off";
		return cache.toString();
	}

    
    protected Cache<K, V> getCache(){
    	return cache;
    }
    
    public CacheStats getCacheStats(){
    	if (!cacheOn)
    		return new CacheStats();
    	CacheStats stats = getCache().getCacheStats();
    	return stats;
    }

}
