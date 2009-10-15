package net.anotheria.anoprise.dualcrud;

public interface DualCrudService<T extends CrudSaveable> {
	void create(T t);

	T read(String ownerId);
	
	void update(T t);
	
	void delete(T t);
	
	void migrate(String ownerId);
	
}
