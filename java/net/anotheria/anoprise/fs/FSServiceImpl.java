package net.anotheria.anoprise.fs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**
 * Main implementation for file system service.
 * 
 * @author abolbat
 * @version 1.0, 2010/02/13
 */
public class FSServiceImpl implements FSService<FSSaveable> {

	/**
	 * Configuration.
	 */
	private final FSServiceConfig config;

	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(FSServiceImpl.class.getName());

	/**
	 * Prefix for logging.
	 */
	private static final String SERVICE_LOG_PREFIX = "FS_SERVICE: ";

	/**
	 * Default constructor.
	 * 
	 * @param aConfig
	 *            - {@link FSServiceConfig}
	 */
	public FSServiceImpl(FSServiceConfig aConfig) {
		this.config = aConfig;
	}

	@Override
	public FSSaveable read(String ownerId) throws FSServiceException {
		String filePath = config.getStoringFolderPath(ownerId);
		File file = new File(filePath);
		if (!file.exists()) {
			log.debug(SERVICE_LOG_PREFIX + "Item not found. Owner id: " + ownerId + ". File path: " + filePath);
			throw new FSItemNotFoundException(ownerId);
		}

		ObjectInputStream in = null;
		try {
			synchronized (this) {
				in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
				FSSaveable result = FSSaveable.class.cast(in.readObject());
				in.close();
				return result;
			}
		} catch (IOException ioe) {
			log.error(SERVICE_LOG_PREFIX + "IOException: " + ioe.getMessage());
			throw new FSServiceException(ioe.getMessage(), ioe);
		} catch (ClassNotFoundException cnfe) {
			log.error(SERVICE_LOG_PREFIX + "ClassNotFoundException: " + cnfe.getMessage());
			throw new FSServiceException(cnfe.getMessage(), cnfe);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException ignored) {
					log.debug(SERVICE_LOG_PREFIX + "Ignored IOException: " + ignored.getMessage());
				}
		}
	}

	@Override
	public void save(FSSaveable t) throws FSServiceException {
		String folderPath = config.getStoringFolderPath(t.getOwnerId());
		String filePath = config.getStoringFolderPath(t.getOwnerId());
		File file = new File(folderPath);
		file.mkdirs();
		file = new File(filePath);

		ObjectOutputStream out = null;
		try {
			synchronized (this) {
				out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
				out.writeObject(t);
				out.close();
			}
		} catch (IOException ioe) {
			log.error(SERVICE_LOG_PREFIX + "IOException: " + ioe.getMessage());
			throw new FSServiceException(ioe.getMessage(), ioe);
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException ignored) {
					log.debug(SERVICE_LOG_PREFIX + "Ignored IOException: " + ignored.getMessage());
				}
		}
	}

	@Override
	public void delete(FSSaveable t) throws FSServiceException {
		String filePath = config.getStoringFolderPath(t.getOwnerId());
		File f = new File(filePath);

		if (!f.exists())
			return;

		if (!f.delete())
			throw new FSServiceException("Deletion filed. Owner id: " + t.getOwnerId() + ". File path: " + filePath);
	}

}
