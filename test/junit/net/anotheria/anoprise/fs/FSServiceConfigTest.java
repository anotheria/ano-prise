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
