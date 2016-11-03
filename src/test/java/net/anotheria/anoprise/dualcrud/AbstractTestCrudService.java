package net.anotheria.anoprise.dualcrud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTestCrudService implements CrudService<TestCrudsaveable> {

	@Override
	public boolean exists(TestCrudsaveable t) throws CrudServiceException {
		return getFile(t.getOwnerId()).exists();
	}

	@Override
	public TestCrudsaveable create(TestCrudsaveable t) throws CrudServiceException {
		File f = getFile(t.getOwnerId());
		if (f.exists())
			throw new CrudServiceException("File already exists for " + t);
		f.getParentFile().mkdirs();
		writeOut(t, f);

		return t;
	}

	@Override
	public TestCrudsaveable save(TestCrudsaveable t) throws CrudServiceException {
		File f = getFile(t.getOwnerId());
		if (!f.exists())
			f.getParentFile().mkdirs();
		writeOut(t, f);

		return t;
	}

	private void writeOut(TestCrudsaveable object, File target) throws CrudServiceException {
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(target);
			ObjectOutputStream oOut = new ObjectOutputStream(fOut);
			oOut.writeObject(object);
		} catch (IOException e) {
			throw new CrudServiceException("writeOut(" + object + ", " + target + ")", e);
		}
	}

	@Override
	public void delete(TestCrudsaveable t) {
		File f = getFile(t.getOwnerId());
		if (f.exists())
			f.delete();
	}

	@Override
	public TestCrudsaveable read(SaveableID id) throws CrudServiceException {
		File f = getFile(id.getSaveableId());
		if (!f.exists())
			throw new ItemNotFoundException(id.getSaveableId());
		FileInputStream fIn = null;
		try {
			fIn = new FileInputStream(f);
			ObjectInputStream oIn = new ObjectInputStream(fIn);
			return (TestCrudsaveable) oIn.readObject();
		} catch (IOException e) {
			throw new CrudServiceException("read(" + id + ")", e);
		} catch (ClassNotFoundException e) {
			throw new CrudServiceException("read(" + id + ")", e);
		} finally {
			if (fIn != null) {
				try {
					fIn.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

	@Override
	public TestCrudsaveable update(TestCrudsaveable t) throws CrudServiceException {
		File f = getFile(t.getOwnerId());
		if (!f.exists())
			throw new ItemNotFoundException(t.getOwnerId());
		writeOut(t, f);

		return t;
	}

	protected File getFile(String ownerId) {
		return new File("testdata/" + getSpecialPath() + "/" + ownerId + "." + getExtension());
	}

	protected abstract String getSpecialPath();

	protected abstract String getExtension();

	public List<TestCrudsaveable> query(Query q) {
		return new ArrayList<TestCrudsaveable>();
	}

}
