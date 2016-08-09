package net.anotheria.anoprise.dataspace;

import net.anotheria.anoprise.metafactory.Service;

/**
 * DataspaceService interface.
 * 
 * @author lrosenberg
 */
public interface DataspaceService extends Service {

	/**
	 * Loads a dataspace from service. If there is no dataspace instance it will be created.
	 * 
	 * @param userId
	 *            - user id
	 * @param dataspaceType
	 *            - dataspace id
	 */
	Dataspace getDataspace(String userId, DataspaceType dataspaceType) throws DataspaceServiceException;

	/**
	 * Save a given dataspace.
	 * 
	 * @param dataspace
	 *            - dataspace
	 */
	void saveDataspace(Dataspace dataspace) throws DataspaceServiceException;
}
