package net.anotheria.anoprise.fs;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FSServiceTest {

	private static FSService<FSTestObject> service;
	private static final String ownerId = "123456789";
	private static long currentTime;

	@BeforeClass
	public static void init() {
		currentTime = new Date().getTime();
		try {
			FSServiceConfig config = new FSServiceConfig(System.getProperty("user.home"), ("test" + currentTime));
			service = FSServiceFactory.createFSService(config);
		} catch (FSServiceConfigException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@AfterClass
	public static void deInit() {
		File file = new File(System.getProperty("user.home") + File.separator + ("test" + currentTime));
		if (file.exists())
			deleteDir(file);
	}

	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	@Test
	public void testSaveReadDelete() throws FSServiceException {
		FSTestObject obj = new FSTestObject(ownerId);
		service.save(obj);
		FSTestObject result = service.read(ownerId);
		Assert.assertTrue(obj.equals(result));
		service.delete(ownerId);
	}

	private class FSTestObject implements FSSaveable {

		private static final long serialVersionUID = 5206989403406117205L;

		private String ownerId;

		public FSTestObject(String aOwnerId) {
			this.ownerId = aOwnerId;
		}

		@Override
		public String getOwnerId() {
			return ownerId;
		}

		private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
			ObjectInputStream.GetField fields = ois.readFields();

			ownerId = String.class.cast(fields.get("ownerId", null));
		}

		private void writeObject(ObjectOutputStream oos) throws IOException {
			ObjectOutputStream.PutField fields = oos.putFields();

			fields.put("ownerId", ownerId);

			oos.writeFields();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof FSTestObject))
				return false;
			FSTestObject other = (FSTestObject) obj;
			if (ownerId == null) {
				if (other.ownerId != null)
					return false;
			} else if (!ownerId.equals(other.ownerId))
				return false;
			return true;
		}

	}
}
