package net.anotheria.anoprise.dualcrud;

/**
 * CrudServiceFixture factory for fixture implementation.
 *
 * @author abolbat
 */
public final class CrudServiceFixtureFactory {

	/**
	 * Create fixture implementation.
	 *
	 * @return {@link net.anotheria.anoprise.dualcrud.CrudService}
	 * @param <T> a T object.
	 */
	public static <T extends CrudSaveable> CrudService<T> create() {
		return new CrudServiceFixture<T>();
	}

}
