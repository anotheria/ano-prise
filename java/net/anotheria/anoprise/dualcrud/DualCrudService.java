package net.anotheria.anoprise.dualcrud;

public interface DualCrudService<T extends CrudSaveable> {

	T create(T t) throws CrudServiceException;

	T read(String ownerId) throws CrudServiceException;

	T update(T t) throws CrudServiceException;

	void delete(T t) throws CrudServiceException;

	T save(T t) throws CrudServiceException;

	void migrate(String ownerId) throws CrudServiceException;

	boolean exists(T t) throws CrudServiceException;

	QueryResult<T> query(Query q) throws CrudServiceException;

}
