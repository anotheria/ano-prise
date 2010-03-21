package net.anotheria.anoprise.dataspace.persistence;

/**
 * DataspacePersistenceService factory.
 * 
 * @author abolbat
 */
public final class DataspacePersistenceServiceFactory {

	/**
	 * Instance of {@link DataspacePersistenceServiceImpl}.
	 */
	private static DataspacePersistenceServiceImpl instance;

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
		if (instance == null)
			instance = new DataspacePersistenceServiceImpl();

		return instance;
	}

}
