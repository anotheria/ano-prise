package net.anotheria.anoprise.fs;

import net.anotheria.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @param <T>
 *            - {@link FSSaveable} object type
 */
public class FSServiceImpl<T extends FSSaveable> implements FSService<T> {

	/**
	 * Configuration.
	 */
	private final FSServiceConfig config;

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(FSServiceImpl.class.getName());


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
	public T read(FSSaveableID id) throws FSServiceException {
		String filePath = config.getReadFolderPath(id.getOwnerId(), id.getSaveableId());
		File file = new File(filePath);

		if (!file.exists()) {
			log.debug("read("+id.getOwnerId()+") " + "Item not found. Owner id: " + id.getOwnerId() + ". File path: " + filePath);
			throw new FSItemNotFoundException(id.getOwnerId());
		}

		ObjectInputStream in = null;
		try {
			synchronized (this) {
				in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
				@SuppressWarnings("unchecked")
				T result = (T) in.readObject();
				return result;
			}
		} catch (IOException ioe) {
			log.error("read("+id.getOwnerId()+")", ioe);
			throw new FSServiceException(ioe.getMessage(), ioe);
		} catch (ClassNotFoundException cnfe) {
			log.error("read("+id.getOwnerId()+")", cnfe);
			throw new FSServiceException(cnfe.getMessage(), cnfe);
		} finally {
			IOUtils.closeIgnoringException(in);
		}
	}

	@Override
	public void save(T t) throws FSServiceException {
		String folderPath = config.getStoreFolderPath(t.getDirOwnerId());
		String filePath = config.getStoreFilePath(folderPath, t.getFileOwnerId());

		File file = new File(folderPath);
		if (!file.exists()){
			boolean madeDir = file.mkdirs();
			if (!madeDir && !file.exists())
				throw new FSServiceException("save("+t.getOwnerId()+") - can't create needed folder structure - "+folderPath);
		}

		file = new File(filePath);

		ObjectOutputStream out = null;
		try {
			synchronized (this) {
				out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
				out.writeObject(t);
				out.flush();
			}
		} catch (IOException ioe) {
			log.error("save("+t.getOwnerId()+")", ioe);
			throw new FSServiceException(ioe.getMessage(), ioe);
		} finally {
			IOUtils.closeIgnoringException(out);
		}
	}

	@Override
	public void delete(FSSaveableID id) throws FSServiceException {
		String filePath = config.getReadFolderPath(id.getOwnerId(), id.getSaveableId());
		File f = new File(filePath);

		if (!f.exists())
			return;

		if (!f.delete())
			throw new FSServiceException("Deletion filed. Owner id: " + id.getOwnerId() + ". File path: " + filePath);
	}

}
