package net.anotheria.anoprise.fs;

/**
 * Factory for file system service.
 * 
 * @author abolbat
 * @version 1.0, 2010/02/13
 */
public final class FSServiceFactory {

	/**
	 * Create instance of {@link FSService} with given {@link FSServiceConfig}.
	 * 
	 * @param config
	 *            - {@link FSServiceConfig}
	 * @return {@link FSService}
	 */
	public static <T extends FSSaveable> FSService<T> createFSService(FSServiceConfig config) {
		return new FSServiceImpl<T>(config);
	}

	/**
	 * Default constructor.
	 */
	private FSServiceFactory() {
	}
}
