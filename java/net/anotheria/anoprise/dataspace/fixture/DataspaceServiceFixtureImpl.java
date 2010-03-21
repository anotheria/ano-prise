package net.anotheria.anoprise.dataspace.fixture;

import java.util.HashMap;
import java.util.Map;

import net.anotheria.anoprise.dataspace.Dataspace;
import net.anotheria.anoprise.dataspace.DataspaceServiceException;
import net.anotheria.anoprise.dataspace.DataspaceServiceImpl;
import net.anotheria.anoprise.dataspace.DataspaceType;


/**
 * DataspaceService fixture implementation.
 */
public class DataspaceServiceFixtureImpl extends DataspaceServiceImpl {

	/**
	 * Memory cache.
	 */
	private static Map<String, Dataspace> dataspaceStore = new HashMap<String, Dataspace>();

	@Override
	public Dataspace getDataspace(String userId, DataspaceType dataspaceType) throws DataspaceServiceException {
		if (dataspaceStore.get(userId) == null) {
			return new Dataspace(userId, dataspaceType);
		}
		return dataspaceStore.get(userId);
	}

	@Override
	public void saveDataspace(Dataspace dataspace) throws DataspaceServiceException {
		dataspaceStore.put(dataspace.getUserId(), dataspace);
	}
}
