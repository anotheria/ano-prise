package net.anotheria.anoprise.dualcrud;

/**
 * DualCrudServiceFactory.
 * 
 * @author lrosenberg
 * @version 1.0, 2010/02/02
 */
public final class DualCrudServiceFactory {

	/**
	 * Create new instance of {@link DualCrudService} with given left and right {@link CrudService} and {@link DualCrudConfig}.
	 * 
	 * @param <T>
	 *            - {@link CrudSaveable} type
	 * @param left
	 *            - {@link CrudService}
	 * @param right
	 *            - {@link CrudService}
	 * @param config
	 *            - {@link DualCrudConfig}
	 * @return created instance of {@link DualCrudService}
	 */
	public static final <T extends CrudSaveable> DualCrudService<T> createDualCrudService(CrudService<T> left, CrudService<T> right, DualCrudConfig config) {
		return new DualCrudServiceImpl<>(config, left, right);
	}

	/**
	 * Default constructor.
	 */
	private DualCrudServiceFactory() {
	}
}
