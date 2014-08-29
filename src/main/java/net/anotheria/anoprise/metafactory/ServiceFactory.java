package net.anotheria.anoprise.metafactory;


/**
 * Factory definition for service factory.
 * @author lrosenberg
 */
public interface ServiceFactory<T extends Service> {
	/**
	 * Creates a new instance of T.
	 * @return newly created instance.
	 */
	T create();
}
