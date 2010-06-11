package net.anotheria.anoprise.dualcrud;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CrudServiceFixture<T extends CrudSaveable> implements CrudService<T> {

	private final ConcurrentHashMap<String, T> holder;

	public CrudServiceFixture() {
		holder = new ConcurrentHashMap<String, T>();
	}

	@Override
	public T create(T t) throws CrudServiceException {
		if (exists(t))
			throw new CrudServiceException("Object already exist. Owner id: " + t.getOwnerId());

		return holder.put(t.getOwnerId(), t);
	}

	@Override
	public T read(String ownerId) throws CrudServiceException, ItemNotFoundException {
		if (!exist(ownerId))
			throw new ItemNotFoundException(ownerId);

		return holder.get(ownerId);
	}

	@Override
	public T update(T t) throws CrudServiceException {
		if (!exists(t))
			throw new ItemNotFoundException(t.getOwnerId());

		return holder.put(t.getOwnerId(), t);
	}

	@Override
	public void delete(T t) throws CrudServiceException {
		holder.remove(t.getOwnerId());
	}

	@Override
	public T save(T t) throws CrudServiceException {
		return holder.put(t.getOwnerId(), t);
	}

	@Override
	public boolean exists(T t) throws CrudServiceException {
		return exist(t.getOwnerId());
	}

	/**
	 * Object existence check.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @return <code>true</code> if object exist or <code>false</code>
	 */
	private boolean exist(String ownerId) {
		return holder.containsKey(ownerId);
	}

	@Override
	public List<T> query(Query q) throws CrudServiceException {
		return new ArrayList<T>();
	}

}
