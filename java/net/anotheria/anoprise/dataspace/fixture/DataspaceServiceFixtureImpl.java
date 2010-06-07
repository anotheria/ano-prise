package net.anotheria.anoprise.dataspace.fixture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.anotheria.anoprise.dataspace.Dataspace;
import net.anotheria.anoprise.dataspace.DataspaceService;
import net.anotheria.anoprise.dataspace.DataspaceServiceException;
import net.anotheria.anoprise.dataspace.DataspaceType;

/**
 * DataspaceService fixture implementation.
 */
public class DataspaceServiceFixtureImpl implements DataspaceService {

	/**
	 * Memory cache.
	 */
	private static Map<String, List<Dataspace>> dataspaceStore = new HashMap<String, List<Dataspace>>();

	@Override
	public Dataspace getDataspace(String userId, DataspaceType dataspaceType) throws DataspaceServiceException {
		Dataspace result = null;
		if (dataspaceStore.get(userId) == null)
			result = new Dataspace(userId, dataspaceType);

		for (Dataspace dataspace : dataspaceStore.get(userId))
			if (dataspace.getDataspaceType().getId() == dataspaceType.getId())
				result = dataspace;

		return result;
	}

	@Override
	public void saveDataspace(Dataspace dataspace) throws DataspaceServiceException {
		List<Dataspace> datapaces = dataspaceStore.get(dataspace.getUserId());
		if (datapaces == null)
			datapaces = new ArrayList<Dataspace>();

		try {
			datapaces.add(Dataspace.class.cast(dataspace.clone()));
			dataspaceStore.put(dataspace.getUserId(), datapaces);
		} catch (CloneNotSupportedException e) {
			throw new DataspaceServiceException(e.getMessage());
		}
	}
}
