package net.anotheria.anoprise.dataspace.persistence;

import net.anotheria.anoprise.dataspace.Dataspace;
import net.anotheria.anoprise.dataspace.DataspaceType;
import net.anotheria.anoprise.metafactory.Service;

/**
 * DataspacePersistenceService interface.
 * 
 * @author lrosenberg
 */
public interface DataspacePersistenceService extends Service {

	/**
	 * Load dataspace from persistence by given userId and dataspaceId.
	 * 
	 * @param userId
	 *            - user id
	 * @param dataspaceType
	 *            - dataspace type
	 * @return loaded dataspace
	 * @throws DataspacePersistenceServiceException
	 */
	Dataspace loadDataspace(String userId, DataspaceType dataspaceType) throws DataspacePersistenceServiceException;

	/**
	 * Save given dataspace in persistence.
	 * 
	 * @param dataspace
	 *            - dataspace
	 * @throws DataspacePersistenceServiceException
	 */
	void saveDataspace(Dataspace dataspace) throws DataspacePersistenceServiceException;

}
