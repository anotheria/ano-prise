package net.anotheria.anoprise.dualcrud;

import org.apache.log4j.Logger;

/**
 * The implementation of the DualCrudService which supports two instances of a CrudService and a dual link config.
 * 
 * @author another
 * 
 * @param <T>
 */
public class DualCrudServiceImpl<T extends CrudSaveable> implements DualCrudService<T> {

	/**
	 * Left instance is considered old, i.e. its used as secondaryReader/writer except for back-migration.
	 */
	private CrudService<T> left;
	/**
	 * Right service instance is considered new.
	 */
	private CrudService<T> right;
	/**
	 * The config with the operation mode.
	 */
	private DualCrudConfig config;
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(DualCrudServiceImpl.class);

	protected DualCrudServiceImpl(DualCrudConfig aConfig, CrudService<T> aLeft, CrudService<T> aRight) {
		config = aConfig;
		left = aLeft;
		right = aRight;
	}

	@Override
	public T create(T t) throws CrudServiceException {
		T result = null;
		CrudService<T> primary = config.getPrimaryWriter(left, right);
		CrudService<T> secondary = config.getSecondaryWriter(left, right);
		result = primary.create(t);
		if (config.writeToBoth() && !(secondary == primary))
			secondary.create(t);

		return result;
	}

	@Override
	public void delete(T t) throws CrudServiceException {
		CrudService<T> primary = config.getPrimaryWriter(left, right);
		CrudService<T> secondary = config.getSecondaryWriter(left, right);
		primary.delete(t);
		if (primary != secondary) {
			try {
				secondary.delete(t);
			} catch (CrudServiceException e) {
				log.warn("delete on secondary writer failed, ignored, delete(" + t + ")", e);
			}
		}
	}

	@Override
	public void migrate(String ownerId) throws CrudServiceException {
		CrudService<T> primaryWriter = config.getPrimaryWriter(left, right);
		CrudService<T> secondaryWriter = config.getSecondaryWriter(left, right);
		if (primaryWriter == secondaryWriter)
			throw new CrudServiceException("Noop migration request");
		T t = secondaryWriter.read(ownerId);
		primaryWriter.save(t);
		try {
			secondaryWriter.delete(t);
		} catch (CrudServiceException e) {
			log.warn("delete on secondary writer failed, ignored, migrate(" + t + ")", e);
		}

	}

	@Override
	public T read(String ownerId) throws CrudServiceException {
		CrudService<T> primary = config.getPrimaryReader(left, right);
		CrudService<T> secondary = config.getSecondaryReader(left, right);

		try {
			T t = primary.read(ownerId);
			return t;
		} catch (ItemNotFoundException e) {
			if (!config.readFromBoth())
				throw e;
		} catch (CrudServiceException e) {
			throw e;
		}

		// if we are here, first failed.
		T fromSecondary = secondary.read(ownerId);

		if (config.migrateOnRead()) {
			try {
				primary.create(fromSecondary);
				if (config.deleteUponMigration()) {
					secondary.delete(fromSecondary);
				}
			} catch (CrudServiceException e) {
				log.warn("migrate on the fly failed, ignored, read(" + ownerId + ")", e);
			}

		}

		return fromSecondary;

	}

	@Override
	public T save(T t) throws CrudServiceException {
		T result = null;
		CrudService<T> primary = config.getPrimaryWriter(left, right);
		CrudService<T> secondary = config.getSecondaryWriter(left, right);
		result = primary.save(t);
		if (config.migrateOnWrite()) {
			try {
				secondary.delete(t);
			} catch (CrudServiceException e) {
				log.warn("delete on secondary writer failed, ignored, save(" + t + ")", e);
			}
		}

		if (config.writeToBoth()) {
			secondary.save(t);
		}

		return result;
	}

	@Override
	public T update(T t) throws CrudServiceException {
		T result = null;

		CrudService<T> primary = config.getPrimaryWriter(left, right);
		CrudService<T> secondary = config.getSecondaryWriter(left, right);

		if (primary.exists(t)) {
			result = primary.update(t);
			if (config.migrateOnWrite()) {
				try {
					secondary.delete(t);
				} catch (CrudServiceException e) {
					log.warn("delete on secondary writer failed, ignored, update(" + t + ")", e);
				}
			}

			if (config.writeToBoth()) {
				secondary.update(t);
			}

			return result;
		}

		if (secondary.exists(t)) {
			if (!config.migrateOnWrite()) {
				return secondary.update(t);
			}
			// if we are here, it doesnt exist on the primary, but exists on the secondary and we need to migrate on write.
			result = primary.create(t);
			try {
				secondary.delete(t);
			} catch (CrudServiceException e) {
				log.warn("delete on secondary writer failed, ignored, save(" + t + ")", e);
			}
			return result;
		}

		throw new ItemNotFoundException(t.getOwnerId());

	}

	public boolean exists(T t) throws CrudServiceException {
		CrudService<T> primary = config.getPrimaryReader(left, right);
		CrudService<T> secondary = config.getSecondaryReader(left, right);
		if (primary.exists(t))
			return true;
		return secondary != primary && secondary.exists(t);
	}

	@Override
	public QueryResult<T> query(Query q) throws CrudServiceException {
		CrudService<T> primary = config.getPrimaryReader(left, right);
		CrudService<T> secondary = config.getSecondaryReader(left, right);

		return new QueryResult<T>(primary.query(q), secondary.query(q));
	}

}
