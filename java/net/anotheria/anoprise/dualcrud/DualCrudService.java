package net.anotheria.anoprise.dualcrud;

public interface DualCrudService<T extends CrudSaveable> {
	void create(T t) throws CrudServiceException;

	T read(String ownerId)  throws CrudServiceException;
	
	void update(T t) throws CrudServiceException;
	
	void delete(T t) throws CrudServiceException;
	
	void save(T t) throws CrudServiceException;

	void migrate(String ownerId) throws CrudServiceException;
	
	boolean exists(T t) throws CrudServiceException;
	
}
