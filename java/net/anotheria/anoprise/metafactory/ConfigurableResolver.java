package net.anotheria.anoprise.metafactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.SetAll;

/**
 * Alias resolver that works with factories.json via configureme framework.
 * @author lrosenberg.
 *
 */
@ConfigureMe(name = "aliases", allfields = false, watch = true)
public class ConfigurableResolver implements AliasResolver {
	/**
	 * Factory class dictionary, used service class name as key.
	 */
	private Map<String,String> aliasMap;

	/**
	 * Resolver priority among resolver list.
	 */
	private int priority;

	/**
	 * Resolver logger.
	 */
	private static final Logger log = Logger.getLogger(ConfigurableResolver.class);

	/**
	 * Static factory creation method.
	 *
	 * @return resolver instance configured
	 */
	public static ConfigurableResolver create(){
		ConfigurableResolver resolver = new ConfigurableResolver();
		try{
			ConfigurationManager.INSTANCE.configure(resolver);
		}catch(IllegalArgumentException e){
			log.error("create() - no factory config found, configurable resolver remains unused", e);
		}
		return resolver;
	}

	/**
	 * Constructor.
	 */
	private ConfigurableResolver(){
		priority = 50;
		aliasMap = new ConcurrentHashMap<String, String>();
	}

	/**
	 * Add configured factory.
	 *
	 * @param name service class name
	 * @param value service factory class name
	 */
	@SetAll
	public void addFactory(String name, String value) {
		aliasMap.put(name,value);
	}

	@Override
	public int getPriority() {
		return priority;
	}

	/**
	 * Resolve factory by alias.
	 *
	 * @param alias service alias
	 * @return factory class name
	 */
	@Override
	public String resolveAlias(String alias) {
		return aliasMap.get(alias);
	}
}
