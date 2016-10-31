package net.anotheria.anoprise.dualcrud;

/**
 * DualCrudServiceFactory.
 *
 * @author lrosenberg
 */
public final class DualCrudServiceFactory {

	/**
	 * Create new instance of {@link net.anotheria.anoprise.dualcrud.DualCrudService} with given left and right {@link net.anotheria.anoprise.dualcrud.CrudService} and {@link net.anotheria.anoprise.dualcrud.DualCrudConfig}.
	 *
	 * @param left
	 *            - {@link net.anotheria.anoprise.dualcrud.CrudService}
	 * @param right
	 *            - {@link net.anotheria.anoprise.dualcrud.CrudService}
	 * @param config
	 *            - {@link net.anotheria.anoprise.dualcrud.DualCrudConfig}
	 * @return created instance of {@link net.anotheria.anoprise.dualcrud.DualCrudService}
	 */
	public static final <T extends CrudSaveable> DualCrudService<T> createDualCrudService(CrudService<T> left, CrudService<T> right, DualCrudConfig config) {
		return new DualCrudServiceImpl<T>(config, left, right);
	}

	/**
	 * Default constructor.
	 */
	private DualCrudServiceFactory() {
	}
}
