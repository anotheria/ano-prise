package net.anotheria.anoprise.dualcrud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of the DualCrudService which supports two instances of a CrudService and a dual link config.
 * 
 * @author another
 * 
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
	private static Logger log = LoggerFactory.getLogger(DualCrudServiceImpl.class);

	protected DualCrudServiceImpl(DualCrudConfig aConfig, CrudService<T> aLeft, CrudService<T> aRight) {
		config = aConfig;
		left = aLeft;
		right = aRight;
	}

	@Override
	public T create(T t) throws CrudServiceException {
		CrudService<T> primary = config.getPrimaryWriter(left, right);
		CrudService<T> secondary = config.getSecondaryWriter(left, right);
		T result = primary.create(t);
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
			secondary.delete(t);
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
		secondaryWriter.delete(t);
	}

	@Override
	public T read(String ownerId) throws CrudServiceException {
		CrudService<T> primary = config.getPrimaryReader(left, right);
		CrudService<T> secondary = config.getSecondaryReader(left, right);

		try {
			return primary.read(ownerId);
		} catch (ItemNotFoundException e) {
			if (!config.readFromBoth())
				throw e;
		} catch (CrudServiceException e) {
			log.error("Could not read owner id '" + ownerId + '\'', e);
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
				log.warn("migrate on the fly failed, ignored, read("+ownerId+ ')', e);
			}

		}

		return fromSecondary;

	}

	@Override
	public T save(T t) throws CrudServiceException {
		CrudService<T> primary = config.getPrimaryWriter(left, right);
		CrudService<T> secondary = config.getSecondaryWriter(left, right);
		T result = primary.save(t);
		if (config.migrateOnWrite()) {
			secondary.delete(t);
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
				secondary.delete(t);
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
			secondary.delete(t);
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

		return new QueryResult<>(primary.query(q), secondary.query(q));
	}

}
