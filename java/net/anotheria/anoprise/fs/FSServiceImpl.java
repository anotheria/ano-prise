package net.anotheria.anoprise.fs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;

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
	 * Update locks.
	 */
	private final ConcurrentHashMap<String, String> updateLocks;

	/**
	 * Default constructor.
	 * 
	 * @param aConfig
	 *            - {@link FSServiceConfig}
	 */
	public FSServiceImpl(FSServiceConfig aConfig) {
		this.config = aConfig;
		this.updateLocks = new ConcurrentHashMap<String, String>();
	}

	@Override
	public FSSaveable read(String ownerId) throws FSServiceException {
		while (updateLocks.containsKey(ownerId)) {
		}

		return innerRead(ownerId);
	}

	/**
	 * Read implementation.
	 * 
	 * @param ownerId
	 *            - owner id
	 * @return {@link FSSaveable} instance
	 * @throws FSServiceException
	 */
	private FSSaveable innerRead(String ownerId) throws FSServiceException {
		File file = new File(config.getStoringFilePath(ownerId));
		if (!file.exists())
			throw new FSItemNotFoundException(ownerId);

		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
			return FSSaveable.class.cast(in.readObject());
		} catch (IOException ioe) {
			throw new FSServiceException(ioe.getMessage(), ioe);
		} catch (ClassNotFoundException cnfe) {
			throw new FSServiceException(cnfe.getMessage(), cnfe);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception ignored) {
				}
		}
	}

	@Override
	public void save(FSSaveable t) throws FSServiceException {
		while (updateLocks.containsKey(t.getOwnerId())) {
		}

		updateLocks.put(t.getOwnerId(), t.getOwnerId());
		innerSave(t);
		updateLocks.remove(t.getOwnerId());
	}

	/**
	 * Save implementation.
	 * 
	 * @param t
	 *            - instance of {@link FSSaveable}
	 * @throws FSServiceException
	 */
	private void innerSave(FSSaveable t) throws FSServiceException {
		File file = new File(config.getStoringFolderPath(t.getOwnerId()));
		file.mkdirs();
		file = new File(config.getStoringFilePath(t.getOwnerId()));

		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			out.writeObject(t);
		} catch (IOException ioe) {
			throw new FSServiceException(ioe.getMessage(), ioe);
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (Exception ignored) {
				}
		}
	}

}
