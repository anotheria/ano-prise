package net.anotheria.anoprise.dataspace;

import net.anotheria.anoprise.metafactory.ServiceFactory;

/**
 * DataspaceService factory.
 * 
 * @author abolbat
 */
public class DataspaceServiceFactory implements ServiceFactory<DataspaceService> {

	@Override
	public DataspaceService create() {
		return new DataspaceServiceImpl();
	}

}
