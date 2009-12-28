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
@ConfigureMe(name="factories",allfields=false,watch=true)
public class ConfigurableResolver implements AliasResolver{
	private Map<String,String> aliasMap;
	private int priority;
	
	private static Logger log = Logger.getLogger(ConfigurableResolver.class);
	
	public static final ConfigurableResolver create(){
		ConfigurableResolver resolver = new ConfigurableResolver();
		try{
			ConfigurationManager.INSTANCE.configure(resolver);
		}catch(IllegalArgumentException e){
			log.error("create() - no factory config found, configurable resolver remains unused", e);
		}
		return resolver;
	}
	
	private ConfigurableResolver(){
		priority = 50;
		aliasMap = new ConcurrentHashMap<String, String>();
	}

	@SetAll
	public void addFactory(String name, String value) {
		aliasMap.put(name,value);
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public String resolveAlias(String alias) {
		return aliasMap.get(alias);
	}
}
