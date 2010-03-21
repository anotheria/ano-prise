package net.anotheria.anoprise.dataspace.system;

import net.anotheria.anoprise.dataspace.persistence.DataspacePersistenceService;
import net.anotheria.anoprise.dataspace.persistence.DataspacePersistenceServiceException;
import net.anotheria.anoprise.dataspace.persistence.DataspacePersistenceServiceFactory;

import org.apache.log4j.Logger;

/**
 * PersistenceCreator utility class for creating persistence structure.
 * 
 * @author abolbat
 * @version 1.0, 2010/01/04
 * 
 */
public class PersistenceCreator {

	/**
	 * Logger.
	 */
	private static final Logger log = Logger.getLogger(PersistenceCreator.class);

	/**
	 * BS <code>DataspacePersistenceService</code>.
	 */
	private DataspacePersistenceService dataspacePersistenceService;

	/**
	 * Default constructor.
	 */
	public PersistenceCreator() {
		dataspacePersistenceService = DataspacePersistenceServiceFactory.getInstance();
	}

	/**
	 * Create persistence structure.
	 */
	public void execute() {
		log.info("SCRIPT: Start executing");
		try {
			dataspacePersistenceService.createPersistenceStructure();
		} catch (DataspacePersistenceServiceException dpse) {
			log.fatal("DataspacePersistenceService exception: ", dpse);
		}
		log.info("SCRIPT: Executed");
	}

	/**
	 * Main run method.
	 * 
	 * @param args
	 *            - all arguments ignored
	 */
	public static final void main(String[] args) {
		new PersistenceCreator().execute();
	}
}
