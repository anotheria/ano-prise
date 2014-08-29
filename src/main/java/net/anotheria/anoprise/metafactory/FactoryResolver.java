package net.anotheria.anoprise.metafactory;

/**
 * Interface for an resolver used by MetaFactory.
 * @author vitaliy
 *
 */
public interface FactoryResolver {

	/**
	 * Resolves an factory. If the factory can't be resolved, returns null. Otherwise the resolved factory, which can be an alias himself, is returned.
	 * @param serviceClass serviceClass
	 * @return resolved factory class
	 */
	Class<? extends ServiceFactory<? extends Service>> resolveFactory(String serviceClass);

	/**
	 * Returns the priority of this resolver. The
	 * @return resolver priority
	 */
	int getPriority();
}