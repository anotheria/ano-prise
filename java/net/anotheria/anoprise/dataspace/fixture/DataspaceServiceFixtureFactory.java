package net.anotheria.anoprise.dataspace.fixture;

import net.anotheria.anoprise.dataspace.DataspaceService;
import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * DataspaceService factory for fixture implementation.
 */
public class DataspaceServiceFixtureFactory implements ServiceFactory<DataspaceService> {

	@Override
	public DataspaceService create() {
		return new DataspaceServiceFixtureImpl();
	}

}
