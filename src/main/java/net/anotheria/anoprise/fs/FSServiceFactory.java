package net.anotheria.anoprise.fs;

/**
 * Factory for file system service.
 *
 * @author abolbat
 */
public final class FSServiceFactory {

	/**
	 * Create instance of {@link net.anotheria.anoprise.fs.FSService} with given {@link net.anotheria.anoprise.fs.FSServiceConfig}.
	 *
	 * @param config
	 *            - {@link net.anotheria.anoprise.fs.FSServiceConfig}
	 * @return {@link net.anotheria.anoprise.fs.FSService}
	 * @param <T> a T object.
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
