package net.anotheria.anoprise.metafactory;

public class OnTheFlyFactory<T extends Service> implements ServiceFactory<T> {

	private T instance;
	
	public OnTheFlyFactory(T anInstance){
		instance = anInstance;
	}
	
	@Override
	public T create() {
		return instance;
	}
	
}
