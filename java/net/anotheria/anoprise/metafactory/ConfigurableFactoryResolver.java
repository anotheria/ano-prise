package net.anotheria.anoprise.metafactory;

import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.SetAll;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory resolver that works with factories.json via configureme framework.
 *
 * @author vitaliy
 */
@ConfigureMe(name = "factories", allfields = false, watch = true)
public final class ConfigurableFactoryResolver implements FactoryResolver {
	/**
	 * Factory class dictionary, used service class name as key.
	 */
	private Map<String, Class<? extends ServiceFactory<? extends Service>>> factoryMap;

	/**
	 * Resolver priority among resolver list.
	 */
	private int priority;

	/**
	 * Resolver logger.
	 */
	private static final Logger LOG = Logger.getLogger(ConfigurableFactoryResolver.class);

	/**
	 * Static factory creation method.
	 *
	 * @return resolver instance configured
	 */
	public static ConfigurableFactoryResolver create() {
		ConfigurableFactoryResolver resolver = new ConfigurableFactoryResolver();
		try {
			ConfigurationManager.INSTANCE.configure(resolver);
		} catch (IllegalArgumentException e) {
			LOG.error("create() - no factory config found, configurable resolver remains unused", e);
		}
		return resolver;
	}

	/**
	 * Constructor.
	 */
	private ConfigurableFactoryResolver() {
		priority = 50;
		factoryMap = new ConcurrentHashMap<String, Class<? extends ServiceFactory<? extends Service>>>();
	}

	/**
	 * Add configured factory.
	 *
	 * @param name service class name
	 * @param value service factory class name
	 */
	@SuppressWarnings("unchecked")
	@SetAll
	public void addFactory(final String name, final String value) {
		try {
			factoryMap.put(name, (Class<? extends ServiceFactory<? extends Service>>) Class.forName(value));
		} catch (ClassNotFoundException e) {
			LOG.error(e);
		}
	}

	/**
	 * Looking for configured factory by service class name.
	 *
	 * @param serviceClass serviceClass
	 * @return service factory class
	 */
	@Override
	public Class<? extends ServiceFactory<? extends Service>> resolveFactory(final String serviceClass) {
		return factoryMap.get(serviceClass);
	}

	@Override
	public int getPriority() {
		return priority;
	}
}