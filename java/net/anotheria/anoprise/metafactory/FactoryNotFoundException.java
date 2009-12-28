package net.anotheria.anoprise.metafactory;

/**
 * Thrown if there is no configured factory for the given name.
 * @author lrosenberg.
 *
 */
public class FactoryNotFoundException extends MetaFactoryException{
	public FactoryNotFoundException(String name){
		super("No factory for service "+name+" found");
	}
}
