package net.anotheria.anoprise.metafactory;

public class FactoryNotFoundException extends MetaFactoryException{
	public FactoryNotFoundException(String name){
		super("No factory for service "+name+" found");
	}
}
