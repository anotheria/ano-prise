package net.anotheria.anoprise.metafactory;


/**
 * Factory definition for service factory.
 * @author lrosenberg
 */
public interface ServiceFactory<T extends Service> {
	T create();
}
