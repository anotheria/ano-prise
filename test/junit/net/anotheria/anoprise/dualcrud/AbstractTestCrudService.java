package net.anotheria.anoprise.dualcrud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class AbstractTestCrudService implements CrudService<TestCrudsaveable>{

	@Override public boolean exists(TestCrudsaveable t) throws CrudServiceException{
		return getFile(t.getOwnerId()).exists();
	}
	
	@Override
	public void create(TestCrudsaveable t) throws CrudServiceException{
		File f = getFile(t.getOwnerId());
		if (f.exists())
			throw new CrudServiceException("File already exists for "+t);
		f.getParentFile().mkdirs();
		writeOut(t, f);
	}
	
	@Override
	public void save(TestCrudsaveable t) throws CrudServiceException{
		File f = getFile(t.getOwnerId());
		if (!f.exists())
			f.getParentFile().mkdirs();
		writeOut(t, f);
	}

	private void writeOut(TestCrudsaveable object, File target) throws CrudServiceException{
		FileOutputStream fOut = null;
		try{
			fOut = new FileOutputStream(target);
			ObjectOutputStream oOut = new ObjectOutputStream(fOut);
			oOut.writeObject(object);
		}catch(IOException e){
			throw new CrudServiceException("writeOut("+object+", "+target+")", e);
		}
	}

	@Override
	public void delete(TestCrudsaveable t) {
		File f = getFile(t.getOwnerId());
		if (f.exists())
			f.delete();
	}

	@Override
	public TestCrudsaveable read(String ownerId) throws CrudServiceException{
		File f = getFile(ownerId);
		if (!f.exists())
			throw new ItemNotFoundException(ownerId);
		FileInputStream fIn = null;
		try{
			fIn = new FileInputStream(f);
			ObjectInputStream oIn = new ObjectInputStream(fIn);
			return (TestCrudsaveable)oIn.readObject();
		}catch(IOException e){
			throw new CrudServiceException("read("+ownerId+")", e);
		}catch(ClassNotFoundException e){
			throw new CrudServiceException("read("+ownerId+")", e);
		}finally{
			if (fIn!=null){
				try{
					fIn.close();
				}catch(IOException ignored){}
			}
		}
	}

	@Override
	public void update(TestCrudsaveable t) throws CrudServiceException{
		File f = getFile(t.getOwnerId());
		if (!f.exists())
			throw new ItemNotFoundException(t.getOwnerId());
		writeOut(t, f);
	}
	
	protected File getFile(String ownerId){
		return new File("testdata/"+getSpecialPath()+"/"+ownerId+"."+getExtension());
	}
	
	protected abstract String getSpecialPath();
	
	protected abstract String getExtension();
	
	
	
}
