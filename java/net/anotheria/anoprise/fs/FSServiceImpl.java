package net.anotheria.anoprise.fs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
		File file = new File(config.getStoringFilePath(ownerId));
		if (!file.exists())
			throw new FSItemNotFoundException(ownerId);

		ObjectInputStream in = null;
		try {
			synchronized (this) {
				in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
				FSSaveable result = FSSaveable.class.cast(in.readObject());
				in.close();
				return result;
			}
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
		File file = new File(config.getStoringFolderPath(t.getOwnerId()));
		file.mkdirs();
		file = new File(config.getStoringFilePath(t.getOwnerId()));

		ObjectOutputStream out = null;
		try {
			synchronized (this) {
				out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
				out.writeObject(t);
				out.close();
			}
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
