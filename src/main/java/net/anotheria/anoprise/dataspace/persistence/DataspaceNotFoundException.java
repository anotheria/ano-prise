package net.anotheria.anoprise.dataspace.persistence;

/**
 * Dataspace persistence exception used in DataspacePersistenceService. Throwed if dataspace not found in persistence.
 * 
 * @author lrosenberg
 */
public class DataspaceNotFoundException extends DataspacePersistenceServiceException {

	/**
	 * Public constructor.
	 * 
	 * @param userId
	 *            - user id
	 * @param dataspaceId
	 *            - dataspace id
	 */
	public DataspaceNotFoundException(String userId, String dataspaceId) {
		super("No dataspace found for user: " + userId + ", dataspace: " + dataspaceId);
	}
}
