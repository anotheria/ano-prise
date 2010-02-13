package net.anotheria.anoprise.fs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.BeforeClass;
import org.junit.Test;

public class FSServiceTest {

	private static FSService<FSTestObject> service;
	private static final String ownerId = "123456789";

	@BeforeClass
	public static void init() {
		try {
			FSServiceConfig config = new FSServiceConfig(System.getProperty("user.home"), "test");
			service = FSServiceFactory.createFSService(config);
		} catch (FSServiceConfigException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Test
	public void testSaveReadDelete() throws FSServiceException {
		FSTestObject obj = new FSTestObject(ownerId);
		service.save(obj);
		service.read(ownerId);
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

	}
}
