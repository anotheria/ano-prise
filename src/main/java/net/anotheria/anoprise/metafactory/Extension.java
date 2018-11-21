package net.anotheria.anoprise.metafactory;

/**
 * Extensions definition for factory types.
 *
 * @author lrosenberg
 */
public enum Extension {
    /**
     * None.
     */
    NONE,
    /**
     * Local service factory.
     */
    LOCAL,
    /**
     * Remote service factory (used by distributeme for synch remote).
     */
    REMOTE,
    /**
     * Asynch service factory (used by distributeme for asynch remote).
     */
    ASYNCH,
    /**
     * Domain knowledge factory (means real impl).
     */
    DOMAIN,
    /**
     * In memory service factory.
     */
    INMEMORY,
    /**
     * CSM-based Service factory.
     */
    CMS,
    /**
     * FederationService factory.
     */
    FEDERATION,
    /**
     * DB (VO) Service factory.
     */
    DB,
    /**
     * Peristence service factory.
     */
    PERSISTENCE,
    /**
     * JDBC-based persistence service factory.
     */
    JDBC,
    /**
     * Editorinterface.
     */
    EDITORINTERFACE,
    /**
     * Test Fixture.
     */
    FIXTURE,
    /**
     * RMI service factory.
     */
    RMI,
    /**
     * jsonrpc service factory.
     */
    JSONRPC,    
	/**
	 * {@link net.anotheria.anoprise.dualcrud.DualCrudService} left {@link net.anotheria.anoprise.dualcrud.CrudService}.
	 */
	CRUD_LEFT,
	/**
	 * {@link net.anotheria.anoprise.dualcrud.DualCrudService} right {@link net.anotheria.anoprise.dualcrud.CrudService}.
	 */
	CRUD_RIGHT;


    public String toExt() {
        return toString().toLowerCase();
    }

    public String toName(Class<? extends Service> clazz) {
        return toName(clazz.getName());
    }

    public String toName(String clazzName) {
        return this == NONE ? clazzName :
                clazzName + "." + toExt();
    }
}
