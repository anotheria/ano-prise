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
     * Remote service factory (rmi).
     */
    REMOTE,
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
    JSONRPC;


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
