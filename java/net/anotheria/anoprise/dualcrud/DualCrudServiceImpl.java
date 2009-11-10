package net.anotheria.anoprise.dualcrud;

public class DualCrudServiceImpl<T extends CrudSaveable> implements DualCrudService<T>{

	
	private CrudService<T> left;
	private CrudService<T> right;
	private DualCrudConfig config;
	
	protected DualCrudServiceImpl(DualCrudConfig aConfig, CrudService<T> aLeft, CrudService<T> aRight){
		config = aConfig;
		left   = aLeft;
		right  = aRight;
	}
	
	
	@Override
	public void create(T t) throws CrudServiceException {
		CrudService<T> primary = config.getPrimaryWriter(left, right);
		CrudService<T> secondary = config.getSecondaryWriter(left, right);
		primary.create(t);
		if (config.writeToBoth() && !(secondary==primary))
			secondary.create(t);
		
	}

	@Override
	public void delete(T t) throws CrudServiceException {
		CrudService<T> primary = config.getPrimaryWriter(left, right);
		CrudService<T> secondary = config.getSecondaryWriter(left, right);
		primary.delete(t);
		if (primary!=secondary){
			try{
				secondary.delete(t);
			}catch(CrudServiceException e){
				//log exception, but dont abort.
			}
		}
	}

	@Override
	public void migrate(String ownerId) throws CrudServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T read(String ownerId) throws CrudServiceException {
		CrudService<T> primary = config.getPrimaryReader(left, right);
		CrudService<T> secondary = config.getSecondaryReader(left, right);
		
		try{
			T t = primary.read(ownerId);
			return t;
		}catch(ItemNotFoundException e){
			if (!config.readFromBoth())
				throw e;
		}catch(CrudServiceException e){
			throw e;
		}
		
		//if we are here, first failed.
		T fromSecondary = secondary.read(ownerId);
		
		if (config.migrateOnRead()){
			try{
				primary.create(fromSecondary);
				if (config.deleteUponMigration()){
					secondary.delete(fromSecondary);
				}
			}catch(CrudServiceException e){
				//TODO log exception, but don't abort.
			}
			
		}
		
		return fromSecondary;
		
	}

	@Override
	public void save(T t) throws CrudServiceException {
		CrudService<T> primary = config.getPrimaryWriter(left, right);
		CrudService<T> secondary = config.getSecondaryWriter(left, right);
		primary.save(t);
		if (config.migrateOnWrite()){
			try{
				secondary.delete(t);
			}catch(CrudServiceException e){
				//log.error
			}
		}
		
		if (config.writeToBoth()){
			secondary.save(t);
		}
	}

	@Override
	public void update(T t) throws CrudServiceException {
		CrudService<T> primary = config.getPrimaryWriter(left, right);
		CrudService<T> secondary = config.getSecondaryWriter(left, right);
		
		if (primary.exists(t)){
			primary.update(t);
			if (config.migrateOnWrite()){
				try{
					secondary.delete(t);
				}catch(CrudServiceException e){
					//log.error
				}
			}
			
			if (config.writeToBoth()){
				secondary.update(t);
			}
			
			return;
		}
		
		if (secondary.exists(t)){
			if (!config.migrateOnWrite()){
				secondary.update(t);
				return;
			}
			//if we are here, it doesnt exist on the primary, but exists on the secondary and we need to migrate on write.
			primary.create(t);
			try{
				secondary.delete(t);
			}catch(CrudServiceException e){
				//log.error
			}
			return;
		}
		
		throw new ItemNotFoundException(t.getOwnerId());
		
	}
	
	public boolean exists(T t) throws CrudServiceException{
		CrudService<T> primary = config.getPrimaryReader(left, right);
		CrudService<T> secondary = config.getSecondaryReader(left, right);
		if (primary.exists(t))
			return true;
		return secondary!=primary && secondary.exists(t);
	}

}
