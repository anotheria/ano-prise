package net.anotheria.anoprise.dualcrud;

public final class DualCrudServiceFactory {
	public static final <T extends CrudSaveable> DualCrudService<T> createDualCrudService(CrudService<T> left, CrudService<T> right, DualCrudConfig config){
		
		DualCrudServiceImpl<T> instance = new DualCrudServiceImpl<T>(config, left, right);
		
		return instance;
	}
	
	private DualCrudServiceFactory(){
		//prevent initialization.
	}
}
