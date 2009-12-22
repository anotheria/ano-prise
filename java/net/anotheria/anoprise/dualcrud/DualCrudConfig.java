package net.anotheria.anoprise.dualcrud;

/**
 * Configuration for the DualCrudService operations.
 * @author lrosenberg.
 *
 */
public class DualCrudConfig {
	/**
	 * Direction in which migration takes place. Useful for reverse migration to rollback effects of a previously not that successful migration.
	 */
	private Direction migrationDirection;
	/**
	 * If true the DLService will try to read from both persistences.
	 */
	private boolean readFromBoth;
	/**
	 * If true and a migration direction is specified, reading from old persistence will cause a migration.
	 */
	private boolean migrateOnRead;
	/**
	 * If true, the service will write to both peristences.
	 */
	private boolean writeToBoth;
	/**
	 * If true upon a successful migration the old copy will be deleted.
	 */
	private boolean deleteUponMigration;
	/**
	 * If true the migration will take place at least on call to write.
	 */
	private boolean migrateOnWrite;
	
	/**
	 * Migration direction.
	 * @author another
	 *
	 */
	private static enum Direction{
		/**
		 * Read from left only, no migration.
		 */
		LEFT,
		/**
		 * Read from right only, no migration.
		 */
		RIGHT,
		/**
		 * Migrate from left to right.
		 */
		LEFTTORIGHT,
		/**
		 * Migrate from right to left.
		 */
		RIGHTTOLEFT
	};
	
	/**
	 * This config allows reading from old and new persistences and migrating from old to new on each read from old persistence.
	 * @return
	 */
	public static final DualCrudConfig migrateOnTheFly(){
		DualCrudConfig config = new DualCrudConfig();

		config.migrationDirection = Direction.LEFTTORIGHT;
		config.readFromBoth = true;
		config.migrateOnRead = true;
		config.migrateOnWrite = true;
		config.writeToBoth = false;
		config.deleteUponMigration = true;
		
		return config;
	}
	
	public static final DualCrudConfig migrateBackOnTheFly(){
		DualCrudConfig config = migrateOnTheFly();
		config.migrationDirection = Direction.RIGHTTOLEFT;
		return config;
	}

	/**
	 * This config enables reading and writing to both persistences, but migrates only on call to migrate().
	 * @return
	 */
	public static final DualCrudConfig migrateOnCallOnly(){
		DualCrudConfig config = new DualCrudConfig();

		config.migrationDirection = Direction.LEFTTORIGHT;
		config.readFromBoth = true;
		config.migrateOnRead = false;
		config.migrateOnWrite = false;
		config.writeToBoth = false;
		config.deleteUponMigration = true;
		
		return config;
	}
	
	public static final DualCrudConfig migrateBackOnCallOnly(){
		DualCrudConfig config = migrateOnCallOnly();
		config.migrationDirection = Direction.RIGHTTOLEFT;
		return config;
	}

	/**
	 * This config is useful for writing to two persistences for backup. Everything written to the 'master' will also be writter to the 'slave' (in fact 
	 * both persistences are equal).
	 * @return
	 */
	public static final DualCrudConfig duplicate(){
		DualCrudConfig config = new DualCrudConfig();

		config.migrationDirection = Direction.LEFTTORIGHT;
		config.readFromBoth = false;
		config.migrateOnRead = false;
		config.migrateOnWrite = false;
		config.writeToBoth = true;
		config.deleteUponMigration = false;
		
		return config;
	}
	
	public static final DualCrudConfig useLeftOnly(){
		DualCrudConfig config = new DualCrudConfig();

		config.migrationDirection = Direction.LEFT;
		config.readFromBoth = false;
		config.migrateOnRead = false;
		config.migrateOnWrite = false;
		config.writeToBoth = false;
		config.deleteUponMigration = false;
		
		return config;
	}
	
	public static final DualCrudConfig useRightOnly(){
		DualCrudConfig config = useLeftOnly();
		config.migrationDirection = Direction.RIGHT;
		return config;
	}
	
	public <T extends CrudSaveable> CrudService<T> getPrimaryReader(CrudService<T> left, CrudService<T> right){
		switch(migrationDirection){
		case LEFT:
			return left;
		case RIGHT:
			return right;
		case LEFTTORIGHT:
			return right;
		case RIGHTTOLEFT:
			return left;
		}
		throw new AssertionError("Can't happen, migration direction "+migrationDirection+" is not supported.");
	}
	
	public <T extends CrudSaveable> CrudService<T> getSecondaryReader(CrudService<T> left, CrudService<T> right){
		switch(migrationDirection){
		case LEFT:
			return left;
		case RIGHT:
			return right;
		case LEFTTORIGHT:
			return left;
		case RIGHTTOLEFT:
			return right;
		}
		throw new AssertionError("Can't happen, migration direction "+migrationDirection+" is not supported.");
	}
	public <T extends CrudSaveable> CrudService<T> getPrimaryWriter(CrudService<T> left, CrudService<T> right){
		switch(migrationDirection){
		case LEFT:
			return left;
		case RIGHT:
			return right;
		case LEFTTORIGHT:
			return right;
		case RIGHTTOLEFT:
			return left;
		}
		throw new AssertionError("Can't happen, migration direction "+migrationDirection+" is not supported.");
	}
	public <T extends CrudSaveable> CrudService<T> getSecondaryWriter(CrudService<T> left, CrudService<T> right){
		switch(migrationDirection){
		case LEFT:
			return left;
		case RIGHT:
			return right;
		case LEFTTORIGHT:
			return left;
		case RIGHTTOLEFT:
			return right;
		}
		throw new AssertionError("Can't happen, migration direction "+migrationDirection+" is not supported.");
	}

	public boolean readFromBoth() {
		return readFromBoth;
	}

	public void setReadFromBoth(boolean readFromBoth) {
		this.readFromBoth = readFromBoth;
	}

	public boolean migrateOnRead() {
		return migrateOnRead;
	}

	public void setMigrateOnRead(boolean migrateOnRead) {
		this.migrateOnRead = migrateOnRead;
	}

	public boolean writeToBoth() {
		return writeToBoth;
	}

	public void setWriteToBoth(boolean writeToBoth) {
		this.writeToBoth = writeToBoth;
	}

	public boolean deleteUponMigration() {
		return deleteUponMigration;
	}

	public void setDeleteUponMigration(boolean deleteUponMigration) {
		this.deleteUponMigration = deleteUponMigration;
	}

	public boolean migrateOnWrite() {
		return migrateOnWrite;
	}

	public void setMigrateOnWrite(boolean migrateOnWrite) {
		this.migrateOnWrite = migrateOnWrite;
	}
}
