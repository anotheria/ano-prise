package net.anotheria.anoprise.dataspace.system;

import net.anotheria.anoprise.dataspace.Dataspace;
import net.anotheria.anoprise.dataspace.DataspaceType;
import net.anotheria.anoprise.dataspace.attribute.Attribute;
import net.anotheria.anoprise.dataspace.attribute.StringAttribute;
import net.anotheria.anoprise.dataspace.persistence.DataspacePersistenceService;
import net.anotheria.anoprise.dataspace.persistence.DataspacePersistenceServiceException;
import net.anotheria.anoprise.dataspace.persistence.DataspacePersistenceServiceFactory;

import org.apache.log4j.Logger;

/**
 * PersistenceTestClient utility class for testing persistence.
 * 
 * @author abolbat
 * @version 1.0, 2010/01/04
 * 
 */
public class PersistenceTestClient {

	/**
	 * Logger.
	 */
	private static final Logger log = Logger.getLogger(PersistenceTestClient.class);

	/**
	 * BS <code>DataspacePersistenceService</code>.
	 */
	private DataspacePersistenceService dataspacePersistenceService;

	/**
	 * Default constructor.
	 */
	public PersistenceTestClient() {
		dataspacePersistenceService = DataspacePersistenceServiceFactory.getInstance();
	}

	/**
	 * Test dataspace persistence.
	 */
	public void execute() {
		log.info(System.currentTimeMillis() + " SCRIPT: Start executing");

		final String userId = "XX1XX";
		final DataspaceType dataspaceType = DataspaceType.BANNER;

		Dataspace dataspace = new Dataspace(userId, dataspaceType);
		final int firstCount = 10;
		for (int i = 1; i <= firstCount; i++) {
			Attribute attribute = new StringAttribute("TestAttribute: " + i, "TestValue: " + i);
			dataspace.addAttribute(attribute.getName(), attribute);
		}

		try {
			// save filled dataspace
			long startSaveTime = System.currentTimeMillis();
			log.info(startSaveTime + " SCRIPT: Saving dataspace. Attributes count: " + dataspace.getAttributes().size());
			dataspacePersistenceService.saveDataspace(dataspace);
			long endSaveTime = System.currentTimeMillis();
			log.info(endSaveTime + " SCRIPT: Saving duration: " + (endSaveTime - startSaveTime) + "ms");

			// load dataspace
			long startLoadingTime = System.currentTimeMillis();
			log.info(startLoadingTime + " SCRIPT: Loading dataspace.");
			dataspace = dataspacePersistenceService.loadDataspace(dataspace.getUserId(), dataspace.getDataspaceType());
			long endLoadingTime = System.currentTimeMillis();
			log.info(endSaveTime + " SCRIPT: Loading duration: " + (endLoadingTime - startLoadingTime) + "ms. Loaded attributes count: "
					+ dataspace.getAttributes().size());

			// save filled dataspace with more elements
			dataspace = new Dataspace(userId, dataspaceType);
			final int secondCount = 15;
			for (int i = 1; i <= secondCount; i++) {
				Attribute attribute = new StringAttribute("TestAttribute: " + i, "TestValue: " + i);
				dataspace.addAttribute(attribute.getName(), attribute);
			}
			long startSaveMoreTime = System.currentTimeMillis();
			log.info(startSaveMoreTime + " SCRIPT: Saving dataspace with more attributes. Attributes count: " + dataspace.getAttributes().size());
			dataspacePersistenceService.saveDataspace(dataspace);
			long endSaveMoreTime = System.currentTimeMillis();
			log.info(endSaveMoreTime + " SCRIPT: Saving duration: " + (endSaveMoreTime - startSaveMoreTime) + "ms");

			// save cleared dataspace
			dataspace = new Dataspace(userId, dataspaceType);
			long startSaveClearedTime = System.currentTimeMillis();
			log.info(startSaveClearedTime + " SCRIPT: Saving cleared dataspace. Attributes count: " + dataspace.getAttributes().size());
			dataspacePersistenceService.saveDataspace(dataspace);
			long endSaveClearedTime = System.currentTimeMillis();
			log.info(endSaveClearedTime + " SCRIPT: Saving duration: " + (endSaveClearedTime - startSaveClearedTime) + "ms");
		} catch (DataspacePersistenceServiceException dpse) {
			log.fatal("DataspacePersistenceService exception: ", dpse);
		}
		log.info(System.currentTimeMillis() + " SCRIPT: Executed");
	}

	/**
	 * Main run method.
	 * 
	 * @param args
	 *            - all arguments ignored
	 */
	public static final void main(String[] args) {
		new PersistenceTestClient().execute();
	}
}
