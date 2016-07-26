package net.anotheria.anoprise.metafactory;

/**
 * Thrown if a factory for a service can't be loaded.
 * 
 * @author lrosenberg
 * @version 1.0, 2010/02/02
 */
public class FactoryInstantiationError extends MetaFactoryException {

	public FactoryInstantiationError(Class<? extends ServiceFactory<? extends Service>> clazz, String serviceName, String reason, Throwable cause) {
		super("Couldn't load factory of class " + clazz + " for service: " + serviceName + " because: " + reason, cause);
	}

}
