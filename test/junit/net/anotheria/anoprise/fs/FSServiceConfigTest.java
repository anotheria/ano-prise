package net.anotheria.anoprise.fs;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FSServiceConfigTest {

	private static FSServiceConfig config;
	private static final String ownerId1 = "1";
	private static final String ownerId12 = "12";
	private static final String ownerId123 = "123";
	private static final String ownerId1234 = "1234";
	private static final String ownerId12345 = "12345";
	private static final String ownerId123456 = "123456";
	private static final String ownerId1234567 = "1234567";
	private static final String ownerId12345678 = "12345678";
	private static final String ownerId123456789 = "123456789";

	@BeforeClass
	public static void init() {
		try {
			config = new FSServiceConfig("/work/fsstorage/", "test");
		} catch (FSServiceConfigException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Test
	public void testStoringFileName() throws FSServiceConfigException {
		Assert.assertTrue(("1" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFileName(ownerId1)));
		Assert.assertTrue(("12" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFileName(ownerId12)));
		Assert.assertTrue(("123" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFileName(ownerId123)));
		Assert.assertTrue(("234" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFileName(ownerId1234)));
		Assert.assertTrue(("345" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFileName(ownerId12345)));
		Assert.assertTrue(("456" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFileName(ownerId123456)));
		Assert.assertTrue(("567" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFileName(ownerId1234567)));
		Assert.assertTrue(("678" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFileName(ownerId12345678)));
		Assert.assertTrue(("789" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFileName(ownerId123456789)));
	}

	@Test
	public void testStoringFilePath() throws FSServiceConfigException {
		Assert.assertTrue(("/work/fsstorage/test/0/0/1" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFilePath(ownerId1)));
		Assert.assertTrue(("/work/fsstorage/test/0/0/12" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFilePath(ownerId12)));
		Assert.assertTrue(("/work/fsstorage/test/0/0/123" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFilePath(ownerId123)));
		Assert.assertTrue(("/work/fsstorage/test/0/1/234" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFilePath(ownerId1234)));
		Assert.assertTrue(("/work/fsstorage/test/0/12/345" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFilePath(ownerId12345)));
		Assert.assertTrue(("/work/fsstorage/test/0/123/456" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFilePath(ownerId123456)));
		Assert.assertTrue(("/work/fsstorage/test/1/234/567" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFilePath(ownerId1234567)));
		Assert.assertTrue(("/work/fsstorage/test/12/345/678" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFilePath(ownerId12345678)));
		Assert.assertTrue(("/work/fsstorage/test/123/456/789" + FSServiceConfig.FILE_EXTENSION).equals(config.getStoringFilePath(ownerId123456789)));
	}

	@Test
	public void testStoringFilePathLEON() throws FSServiceConfigException {
		Assert.assertEquals(("000/000/1.dat"), (FSServiceConfig.testGetStoreFileName(ownerId1, 9, 3, "dat")));
		Assert.assertEquals(("000/000/12.dat"), (FSServiceConfig.testGetStoreFileName(ownerId12, 9, 3, "dat")));
		Assert.assertEquals(("000/000/123.dat"), (FSServiceConfig.testGetStoreFileName(ownerId123, 9, 3, "dat")));
		Assert.assertEquals(("000/001/1234.dat"), (FSServiceConfig.testGetStoreFileName(ownerId1234, 9, 3, "dat")));
		Assert.assertEquals(("000/012/12345.dat"), (FSServiceConfig.testGetStoreFileName(ownerId12345, 9, 3, "dat")));
		Assert.assertEquals(("000/123/123456.dat"), (FSServiceConfig.testGetStoreFileName(ownerId123456, 9, 3, "dat")));
		Assert.assertEquals(("001/234/1234567.dat"), (FSServiceConfig.testGetStoreFileName(ownerId1234567, 9, 3, "dat")));
		Assert.assertEquals(("012/345/12345678.dat"), (FSServiceConfig.testGetStoreFileName(ownerId12345678, 9, 3, "dat")));
		Assert.assertEquals(("123/456/123456789.dat"), (FSServiceConfig.testGetStoreFileName(ownerId123456789, 9, 3, "dat")));
	}

	@Test
	public void testStoringFilePathLEON2() throws FSServiceConfigException {
		Assert.assertEquals(("00/00/00/00/1.dat"), (FSServiceConfig.testGetStoreFileName(ownerId1, 10, 2, "dat")));
		Assert.assertEquals(("00/00/00/00/12.dat"), (FSServiceConfig.testGetStoreFileName(ownerId12, 10, 2, "dat")));
		Assert.assertEquals(("00/00/00/01/123.dat"), (FSServiceConfig.testGetStoreFileName(ownerId123, 10, 2, "dat")));
		Assert.assertEquals(("00/00/00/12/1234.dat"), (FSServiceConfig.testGetStoreFileName(ownerId1234, 10, 2, "dat")));
		Assert.assertEquals(("00/00/01/23/12345.dat"), (FSServiceConfig.testGetStoreFileName(ownerId12345, 10, 2, "dat")));
		Assert.assertEquals(("00/00/12/34/123456.dat"), (FSServiceConfig.testGetStoreFileName(ownerId123456, 10, 2, "dat")));
		Assert.assertEquals(("00/01/23/45/1234567.dat"), (FSServiceConfig.testGetStoreFileName(ownerId1234567, 10, 2, "dat")));
		Assert.assertEquals(("00/12/34/56/12345678.dat"), (FSServiceConfig.testGetStoreFileName(ownerId12345678, 10, 2, "dat")));
		Assert.assertEquals(("01/23/45/67/123456789.dat"), (FSServiceConfig.testGetStoreFileName(ownerId123456789, 10, 2,  "dat")));
	}

	@Test
	public void testStoringFilePathLEON3() throws FSServiceConfigException {
		Assert.assertEquals(("0/0/0/0/1.dat"), (FSServiceConfig.testGetStoreFileName(ownerId1, 5, 1, "dat")));
		Assert.assertEquals(("0/0/0/1/12.dat"), (FSServiceConfig.testGetStoreFileName(ownerId12, 5, 1, "dat")));
		Assert.assertEquals(("0/0/1/2/123.dat"), (FSServiceConfig.testGetStoreFileName(ownerId123, 5, 1, "dat")));
		Assert.assertEquals(("0/1/2/3/1234.dat"), (FSServiceConfig.testGetStoreFileName(ownerId1234, 5, 1, "dat")));
		Assert.assertEquals(("1/2/3/4/12345.dat"), (FSServiceConfig.testGetStoreFileName(ownerId12345, 5,1, "dat")));
	}

	@Test
	public void testStoringFolderPath() throws FSServiceConfigException {
		Assert.assertTrue("/work/fsstorage/test/0/0".equals(config.getStoringFolderPath(ownerId1)));
		Assert.assertTrue("/work/fsstorage/test/0/0".equals(config.getStoringFolderPath(ownerId12)));
		Assert.assertTrue("/work/fsstorage/test/0/0".equals(config.getStoringFolderPath(ownerId123)));
		Assert.assertTrue("/work/fsstorage/test/0/1".equals(config.getStoringFolderPath(ownerId1234)));
		Assert.assertTrue("/work/fsstorage/test/0/12".equals(config.getStoringFolderPath(ownerId12345)));
		Assert.assertTrue("/work/fsstorage/test/0/123".equals(config.getStoringFolderPath(ownerId123456)));
		Assert.assertTrue("/work/fsstorage/test/1/234".equals(config.getStoringFolderPath(ownerId1234567)));
		Assert.assertTrue("/work/fsstorage/test/12/345".equals(config.getStoringFolderPath(ownerId12345678)));
		Assert.assertTrue("/work/fsstorage/test/123/456".equals(config.getStoringFolderPath(ownerId123456789)));
	}

	@Test
	public void testValidation() throws FSServiceConfigException {
		try {
			new FSServiceConfig(null, "test");
			Assert.assertTrue(false);
		} catch (FSServiceConfigException fsse) {
			Assert.assertTrue(true);
		}

		try {
			new FSServiceConfig("/work/fsstorage/", null);
			Assert.assertTrue(false);
		} catch (FSServiceConfigException fsse) {
			Assert.assertTrue(true);
		}

		try {
			config.getStoringFileName(null);
			Assert.assertTrue(false);
		} catch (FSServiceConfigException fsse) {
			Assert.assertTrue(true);
		}

		try {
			config.getStoringFileName("");
			Assert.assertTrue(false);
		} catch (FSServiceConfigException fsse) {
			Assert.assertTrue(true);
		}

		try {
			config.getStoringFileName("1234567890");
			Assert.assertTrue(false);
		} catch (FSServiceConfigException fsse) {
			Assert.assertTrue(true);
		}

		try {
			config.getStoringFileName("abcd123");
			Assert.assertTrue(false);
		} catch (FSServiceConfigException fsse) {
			Assert.assertTrue(true);
		}
	}
}
