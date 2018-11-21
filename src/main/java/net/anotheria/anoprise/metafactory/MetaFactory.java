package net.anotheria.anoprise.metafactory;

import net.anotheria.moskito.core.util.storage.Storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Utility class for dynamic instance creation of multiple possible instance types.
 * 
 * @author lrosenberg
 * @author dmetelin
 */
public class MetaFactory {

	/**
	 * Storage for instances.
	 */
	private static Map<String, Service> instances;
	/**
	 * Storage for aliases.
	 */
	private static Map<String, String> aliases;
	/**
	 * Storage for factory classes.
	 */
	@SuppressWarnings("rawtypes")
	private static Map<String, FactoryHolder> factoryClasses;
	/**
	 * Storage for factories.
	 */
	private static Map<String, ServiceFactory<? extends Service>> factories;

	private static List<OnTheFlyConflictResolver> otfConflictResolvers;

	/**
	 * List of additional resolvers for aliases.
	 */
	private static List<AliasResolver> resolverList;

	/**
	 * Factory resolver by service class name
	 */
	private static FactoryResolver factoryResolver;

	static {
		reset();
	}

	/**
	 * Performs a complete reset of the inner state. Useful for unit testing to call @AfterClass or @After.
	 */
	public static void reset() {
		resolverList = new CopyOnWriteArrayList<AliasResolver>();
		resolverList.add(new SystemPropertyResolver());
		resolverList.add(ConfigurableResolver.create());
		factoryResolver = ConfigurableFactoryResolver.create();
		factoryClasses = Storage.createConcurrentHashMapStorage("mf-factoryClasses");
		factories = Storage.createConcurrentHashMapStorage("mf-factories");
		aliases = Storage.createConcurrentHashMapStorage("mf-aliases");
		instances = Storage.createConcurrentHashMapStorage("mf-instances");
		otfConflictResolvers = new CopyOnWriteArrayList<OnTheFlyConflictResolver>();
	}

	/**
	 * Performs reset of created instances for some service.
	 */
	public static <T extends Service> void resetInstances(Class<T> pattern) {
		String className = pattern.getName();
		for (String key : instances.keySet())
			if (key.contains(className))
				instances.remove(key);
	}

	public static <T extends Service> T create(Class<T> pattern, Extension extension) throws MetaFactoryException {
		return pattern.cast(_create(pattern, extension, null));
	}

	public static <T extends Service> T create(Class<T> pattern) throws MetaFactoryException {
		return pattern.cast(create(pattern, Extension.NONE));
	}

	@SuppressWarnings("unchecked")
	private static <T extends Service> T _create(Class<T> pattern, Extension extension, String name) throws MetaFactoryException {
		if (name==null)
			name = extension.toName(pattern);
		ServiceFactory<T> factory = (ServiceFactory<T>) factories.get(name);
		if (factory != null)
			return factory.create();		

		FactoryHolder<T> factoryConfiguration = factoryClasses.get(name);		
		Class<? extends ServiceFactory<T>> clazz = factoryConfiguration != null ? factoryConfiguration.getFactoryClass() : null;
		if (clazz == null) {
			clazz = (Class<? extends ServiceFactory<T>>) factoryResolver.resolveFactory(name);
			if (clazz != null)
				addFactoryClass(name, clazz);
		}

		if (clazz == null){
			if (extension!=Extension.NONE)
				throw new FactoryNotFoundException(name);
			//attempt to dynamically load from reflections.
			Collection<Class<? extends T>> implementations = OnTheFlyResolver.resolveOnTheFly(pattern);
			if (implementations==null)
				throw new FactoryNotFoundException(name);
			if (implementations.size()>1){
				for (OnTheFlyConflictResolver conflictResolver : otfConflictResolvers){
					Class<? extends T> resolved = conflictResolver.resolveConflict(implementations);
					if (resolved!=null){
						try{
							return resolved.newInstance();
						}catch(Exception e){
							throw new MetaFactoryException("Found implementations and picked ("+resolved+" by "+conflictResolver+"  but couldn't instantiate it "+e.getMessage(), e);
						}
					}
				}
				throw new MetaFactoryException("No configured factory and multiple subclasses found: "+implementations);
			}

			try{
				return implementations.iterator().next().newInstance();
			}catch(Exception e){
				throw new MetaFactoryException("Found implementation but couldn't instantiate it "+e.getMessage(), e);
			}
		}

		synchronized (factories) {
			factory = (ServiceFactory<T>) factories.get(name);
			if (factory == null) {
				try {
					factory = clazz.newInstance();
					if (factory instanceof ParameterizedServiceFactory<?>) {
						ParameterizedServiceFactory<T> parameterizedFactory = (ParameterizedServiceFactory<T>) factory;
						parameterizedFactory.setParameters(factoryConfiguration.getParameters());
					}
					
					factories.put(name, factory);
				} catch (IllegalAccessException e) {
					throw new FactoryInstantiationError(clazz, name, e.getMessage());
				} catch (InstantiationException e) {
					e.printStackTrace();
					throw new FactoryInstantiationError(clazz, name, e.getMessage());
				}
			}

		}
		return factory.create();
	}
	
	private static <T extends Service> T _create(final Class<T> service, final String serviceKey) throws MetaFactoryException {
		@SuppressWarnings("unchecked")
		FactoryHolder<T> factoryConfiguration = factoryClasses.get(serviceKey);
		if (factoryConfiguration == null)
			throw new FactoryNotFoundException(serviceKey);

		try {
			ServiceFactory<T> factory = factoryConfiguration.getFactoryClass().newInstance();
			if (factory instanceof ParameterizedServiceFactory<?>) {
				ParameterizedServiceFactory<T> parameterizedFactory = (ParameterizedServiceFactory<T>) factory;
				parameterizedFactory.setParameters(factoryConfiguration.getParameters());
			}

			return factory.create();
		} catch (IllegalAccessException e) {
			throw new FactoryInstantiationError(factoryConfiguration.getFactoryClass(), service.getName(), e.getMessage());
		} catch (InstantiationException e) {
			throw new FactoryInstantiationError(factoryConfiguration.getFactoryClass(), service.getName(), e.getMessage());
		}
	}

	public static <T extends Service> T get(Class<T> pattern) throws MetaFactoryException {
		return get(pattern, Extension.NONE);
	}

	public static <T extends Service> T get(Class<T> pattern, Extension extension) throws MetaFactoryException {

		out("get called, pattern: " + pattern + ", extension: " + extension);

		if (extension == null)
			extension = Extension.NONE;
		String name = extension.toName(pattern);
		out("name is " + name);

		name = resolveAlias(name);
		out("resolved alias to " + name);

		T instance = pattern.cast(instances.get(name));

		out("instance of " + name + " is: " + instance);

		if (instance != null)
			return instance;

		synchronized (instances) {
			instance = pattern.cast(instances.get(name));
			if (instance == null) {
				out("creating new instance of " + name);
				instance = pattern.cast(_create(pattern, extension, name));
				out("created new instance of " + name + " ---> " + instance);
				instances.put(name, instance);
			}
		}

		return instance;
	}
	
	public static <T extends Service> T get(final Class<T> service, final String extension) throws MetaFactoryException {
		if (service == null)
			throw new IllegalArgumentException("service argument is null.");

		String innerExtension = extension != null ? "." + extension.replaceAll("\\.", "_").toLowerCase() : ".null";
		String serviceKey = service.getName() + innerExtension;

		T instance = service.cast(instances.get(serviceKey));
		if (instance != null)
			return instance;

		synchronized (instances) {
			instance = service.cast(instances.get(serviceKey));
			if (instance == null) {
				instance = service.cast(_create(service, serviceKey));
				instances.put(serviceKey, instance);
			}
		}

		return instance;
	}

	public static String resolveAlias(String name) {

		// first check resolvers
		synchronized (resolverList) {
			for (AliasResolver resolver : resolverList) {
				String resolved = resolver.resolveAlias(name);
				if (resolved != null)
					return resolveAlias(resolved);
			}
		}

		String alias = aliases.get(name);
		return alias == null ? name : resolveAlias(alias);
	}

	public static String resolveAlias(Class<? extends Service> clazz) {
		return resolveAlias(clazz.getName());
	}

	public static void addAlias(String name, String alias) {
		aliases.put(alias, name);
	}

	public static <T extends Service> void addAlias(Class<T> pattern, Extension nameExtension) {
		addAlias(pattern, nameExtension, null);
	}

	public static <T extends Service> void addAlias(Class<T> pattern, Extension nameExt, Extension aliasExtension) {
		if (nameExt == null)
			nameExt = Extension.NONE;
		if (aliasExtension == null)
			aliasExtension = Extension.NONE;
		addAlias(nameExt.toName(pattern), aliasExtension.toName(pattern));
	}

	public static <T extends Service> void createOnTheFlyFactory(Class<T> service, Extension extension, T serviceInstance) {
		OnTheFlyFactory<T> factory = new OnTheFlyFactory<T>(serviceInstance);
		factories.put(extension.toName(service), factory);
	}

	public static <T extends Service> void addFactoryClass(Class<T> service, Extension extension, Class<? extends ServiceFactory<T>> factoryClass) {
		addFactoryClass(extension.toName(service), factoryClass);
	}

	public static <T extends Service> void addFactoryClass(String name, Class<? extends ServiceFactory<T>> factoryClass) {
		factoryClasses.put(name, new FactoryHolder<T>(factoryClass, null));
	} 
	
	public static <T extends Service, V extends ParameterizedServiceFactory<T>> void addParameterizedFactoryClass(final Class<T> service, final String extension, final Class<V> factoryClass, final Map<String, Serializable> parameters) {
		if (service == null)
			throw new IllegalArgumentException("service argument is null.");
		if (factoryClass == null)
			throw new IllegalArgumentException("factoryClass argument is null.");
		
		String innerExtension = extension != null ? "." + extension.replaceAll("\\.", "_").toLowerCase() : ".null";
		factoryClasses.put(service.getName() + innerExtension, new FactoryHolder<T>(factoryClass, parameters));
	}

	/**
	 * Used for debug output.
	 * 
	 * @param o
	 *            output.
	 */
	private static void out(Object o) {
		// System.out.println("[MetaFactory] "+o);
	}

	public static void debugDumpAliasMap() {
		Set<String> keys = aliases.keySet();
		for (String key : keys) {
			System.out.println(key + " = " + aliases.get(key));
		}
	}

	public static void addAliasResolver(AliasResolver resolver) {
		synchronized (resolverList) {
			for (int i = 0; i < resolverList.size(); i++) {
				AliasResolver someResolver = resolverList.get(i);
				if (resolver.getPriority() < someResolver.getPriority()) {
					resolverList.add(i, resolver);
					return;
				}
			}
			resolverList.add(resolver);
		}
	}

	public static List<AliasResolver> getAliasResolverList() {
		return new ArrayList<AliasResolver>(resolverList);
	}

	/**
	 * Adds a new conflict resolver to be used whenever onthefly impl lookup returns multiple candidates.
	 * @param otfCR
	 */
	public static void addOnTheFlyConflictResolver(OnTheFlyConflictResolver otfCR){
		otfConflictResolvers.add(otfCR);
	}
	
	/**
	 * Factory configuration holder.
	 * 
	 * @author Alexandr Bolbat
	 *
	 * @param <T>
	 */
	private static class FactoryHolder<T extends Service> {

		/**
		 * Factory class.
		 */
		private final Class<? extends ServiceFactory<T>> factoryClass;

		/**
		 * Factory parameters.
		 */
		private final Map<String, Serializable> parameters;

		/**
		 * Default constructor.
		 * 
		 * @param aFactoryClass
		 *            factory class
		 * @param aParameters
		 *            factory parameters, can be <code>null</code>
		 */
		protected FactoryHolder(final Class<? extends ServiceFactory<T>> aFactoryClass, final Map<String, Serializable> aParameters) {
			if (aFactoryClass == null)
				throw new IllegalArgumentException("aFactoryClass argument is null.");

			this.factoryClass = aFactoryClass;
			this.parameters = aParameters;
		}

		public Class<? extends ServiceFactory<T>> getFactoryClass() {
			return factoryClass;
		}

		public Map<String, Serializable> getParameters() {
			return parameters;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("FactoryHolder [factoryClass=");
			builder.append(factoryClass);
			builder.append(", parameters=");
			builder.append(parameters);
			builder.append("]");
			return builder.toString();
		}

	}

}
