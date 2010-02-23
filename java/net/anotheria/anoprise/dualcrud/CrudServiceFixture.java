package net.anotheria.anoprise.dualcrud;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CrudServiceFixture implements CrudService<CrudSaveable> {

	private final ConcurrentHashMap<String, CrudSaveable> holder;

	public CrudServiceFixture() {
		holder = new ConcurrentHashMap<String, CrudSaveable>();
	}

	@Override
	public CrudSaveable create(CrudSaveable t) throws CrudServiceException {
		if (exists(t))
			throw new CrudServiceException("Object already exist. Owner id: " + t.getOwnerId());

		return holder.put(t.getOwnerId(), t);
	}

	@Override
	public CrudSaveable read(String ownerId) throws CrudServiceException, ItemNotFoundException {
		if (!exist(ownerId))
			throw new ItemNotFoundException(ownerId);

		return holder.get(ownerId);
	}

	@Override
	public CrudSaveable update(CrudSaveable t) throws CrudServiceException {
		if (!exists(t))
			throw new ItemNotFoundException(t.getOwnerId());

		return holder.put(t.getOwnerId(), t);
	}

	@Override
	public void delete(CrudSaveable t) throws CrudServiceException {
		holder.remove(t.getOwnerId());
	}

	@Override
	public CrudSaveable save(CrudSaveable t) throws CrudServiceException {
		return holder.put(t.getOwnerId(), t);
	}

	@Override
	public boolean exists(CrudSaveable t) throws CrudServiceException {
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
	public List<CrudSaveable> query(Query q) throws CrudServiceException {
		return new ArrayList<CrudSaveable>();
	}

}
