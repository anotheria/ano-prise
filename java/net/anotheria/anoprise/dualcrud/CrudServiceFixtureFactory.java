package net.anotheria.anoprise.dualcrud;

/**
 * CrudServiceFixture factory for fixture implementation.
 * 
 * @author abolbat
 * @version 1.0, 2010/02/23
 */
public final class CrudServiceFixtureFactory {

	/**
	 * Create fixture implementation.
	 * 
	 * @return {@link CrudService}
	 */
	public static CrudService<CrudSaveable> create() {
		return new CrudServiceFixture();
	}

}
