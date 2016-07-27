package net.anotheria.anoprise.dataspace;

import net.anotheria.anoprise.cache.Cache;
import net.anotheria.anoprise.cache.Caches;
import net.anotheria.anoprise.dataspace.persistence.DataspaceNotFoundException;
import net.anotheria.anoprise.dataspace.persistence.DataspacePersistenceService;
import net.anotheria.anoprise.dataspace.persistence.DataspacePersistenceServiceException;
import net.anotheria.anoprise.dataspace.persistence.DataspacePersistenceServiceFactory;
import net.anotheria.util.BasicComparable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DataspaceService implementation.
 * 
 * @author lrosenberg
 */
public class DataspaceServiceImpl implements DataspaceService {

	/**
	 * Internal service cache.
	 */
	private Cache<DataspaceKey, Dataspace> cache;

	/**
	 * Cache start size constant.
	 */
	private static final int CACHE_START_SIZE = 5000;

	/**
	 * Cache max size constant.
	 */
	private static final int CACHE_MAX_SIZE = 50000;

	/**
	 * Dataspace persistence service.
	 */
	private DataspacePersistenceService persistenceService;

	/**
	 * Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(DataspaceServiceImpl.class);

	/**
	 * Default constructor.
	 */
	public DataspaceServiceImpl() {
		persistenceService = DataspacePersistenceServiceFactory.getInstance();

		try {
			cache = Caches.createConfigurableHardwiredCache("ano-prise-dataspace-cache");
		} catch (IllegalArgumentException e) {
			log.warn("Can't find cache configuration for ano-prise-dataspace-cache, falling back to min cache.", e);
			cache = Caches.createHardwiredCache("ano-prise-dataspace-cache", CACHE_START_SIZE, CACHE_MAX_SIZE);
		}
	}

	@Override
	public Dataspace getDataspace(String userId, DataspaceType dataspaceType) throws DataspaceServiceException {
		if (userId == null)
			throw new IllegalArgumentException("User id null");
		if (dataspaceType == null)
			throw new IllegalArgumentException("Dataspace type null");

		DataspaceKey key = new DataspaceKey(userId, dataspaceType);
		Dataspace fromCache = cache.get(key);
		if (fromCache != null)
			return fromCache;

		Dataspace fromPersistence;
		try {
			fromPersistence = persistenceService.loadDataspace(userId, dataspaceType);
		} catch (DataspaceNotFoundException notFound) {
			Dataspace newInstance = new Dataspace(userId, dataspaceType);
			cache.put(key, newInstance);
			return newInstance;
		} catch (DataspacePersistenceServiceException e) {
			log.error("Persistence service filed.", e);
			throw new DataspacePersistenceFailedException(e);
		}

		cache.put(key, fromPersistence);
		return fromPersistence;
	}

	@Override
	public void saveDataspace(Dataspace dataspace) throws DataspaceServiceException {
		if (dataspace == null)
			throw new IllegalArgumentException("Dataspace null");
		if (dataspace.getUserId() == null)
			throw new IllegalArgumentException("User id null");
		if (dataspace.getDataspaceType() == null)
			throw new IllegalArgumentException("Dataspace type null");

		try {
			persistenceService.saveDataspace(dataspace);
		} catch (DataspacePersistenceServiceException e) {
			log.error("Persistence service filed.", e);
			throw new DataspacePersistenceFailedException(e);
		}

		cache.put(new DataspaceKey(dataspace.getUserId(), dataspace.getDataspaceType()), dataspace);
	}

	/**
	 * Private class used as key for service cache.
	 * 
	 * @author lrosenberg
	 */
	private static final class DataspaceKey {

		/**
		 * User id.
		 */
		private String userId;

		/**
		 * Dataspace type id.
		 */
		private int dataspaceId;

		/**
		 * Default constructor.
		 * 
		 * @param aUserId
		 *            - user id
		 * @param aDataspaceType
		 *            - dataspace type
		 */
		public DataspaceKey(String aUserId, DataspaceType aDataspaceType) {
			this.userId = aUserId;
			this.dataspaceId = aDataspaceType.getId();
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof DataspaceKey && (BasicComparable.compareString(userId, ((DataspaceKey) o).userId) == 0
                    && dataspaceId == ((DataspaceKey) o).dataspaceId);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + dataspaceId;
			result = prime * result + ((userId == null) ? 0 : userId.hashCode());
			return result;
		}

		@Override
		public String toString() {
			return "DataspaceKey [dataspaceId=" + dataspaceId + ", userId=" + userId + ']';
		}
	}

}
