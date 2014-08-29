package net.anotheria.anoprise.dataspace.persistence;

/**
 * DataspacePersistenceService factory.
 * 
 * @author abolbat
 */
public final class DataspacePersistenceServiceFactory {

	/**
	 * Default constructor.
	 */
	private DataspacePersistenceServiceFactory() {
	}

	/**
	 * Get instance of {@link DataspacePersistenceServiceImpl}.
	 * 
	 * @return {@link DataspacePersistenceServiceImpl}
	 */
	public static synchronized DataspacePersistenceService getInstance() {
		return new DataspacePersistenceServiceImpl(DataspacePersistenceConfiguration.getInstance());
	}

	/**
	 * Get instance of {@link DataspacePersistenceServiceImpl} with custom configuration.
	 * 
	 * @return {@link DataspacePersistenceServiceImpl}
	 */
	public static synchronized DataspacePersistenceService getInstance(String configurationFileName) {
		return new DataspacePersistenceServiceImpl(DataspacePersistenceConfiguration.getInstance(configurationFileName));
	}

}
