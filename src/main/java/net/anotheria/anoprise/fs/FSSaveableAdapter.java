package net.anotheria.anoprise.fs;

/**
 * @author Vlad Lukjanenko
 */
public abstract class FSSaveableAdapter implements FSSaveable {

    @Override
    public String getFileOwnerId() {
        return getOwnerId();
    }

    @Override
    public String getDirOwnerId() {
        return getOwnerId();
    }
}
