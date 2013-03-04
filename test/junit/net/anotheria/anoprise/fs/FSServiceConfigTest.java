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
    private static final String ownerId12345678910 = "test_test";

    @BeforeClass
    public static void init() {
        try {
            config = new FSServiceConfig("/work/fsstorage/", "dat");
        } catch (FSServiceConfigException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void testStoreFileName() throws FSServiceConfigException {
        Assert.assertEquals("1.dat", config.getStoreFileName(ownerId1));
        Assert.assertEquals("12.dat", config.getStoreFileName(ownerId12));
        Assert.assertEquals("123.dat", config.getStoreFileName(ownerId123));
        Assert.assertEquals("1234.dat", config.getStoreFileName(ownerId1234));
        Assert.assertEquals("12345.dat", config.getStoreFileName(ownerId12345));
        Assert.assertEquals("123456.dat", config.getStoreFileName(ownerId123456));
        Assert.assertEquals("1234567.dat", config.getStoreFileName(ownerId1234567));
        Assert.assertEquals("12345678.dat", config.getStoreFileName(ownerId12345678));
        Assert.assertEquals("123456789.dat", config.getStoreFileName(ownerId123456789));
    }

    @Test
    public void testStoringFilePath() throws FSServiceConfigException {
        Assert.assertEquals("/work/fsstorage/00/00/00/00/1.dat", config.getStoreFilePath(ownerId1));
        Assert.assertEquals("/work/fsstorage/00/00/00/00/12.dat", config.getStoreFilePath(ownerId12));
        Assert.assertEquals("/work/fsstorage/00/00/00/01/123.dat", config.getStoreFilePath(ownerId123));
        Assert.assertEquals("/work/fsstorage/00/00/00/12/1234.dat", config.getStoreFilePath(ownerId1234));
        Assert.assertEquals("/work/fsstorage/00/00/01/23/12345.dat", config.getStoreFilePath(ownerId12345));
        Assert.assertEquals("/work/fsstorage/00/00/12/34/123456.dat", config.getStoreFilePath(ownerId123456));
        Assert.assertEquals("/work/fsstorage/00/01/23/45/1234567.dat", config.getStoreFilePath(ownerId1234567));
        Assert.assertEquals("/work/fsstorage/00/12/34/56/12345678.dat", config.getStoreFilePath(ownerId12345678));
        Assert.assertEquals("/work/fsstorage/01/23/45/67/123456789.dat", config.getStoreFilePath(ownerId123456789));
    }

    @Test
    public void testStoringFolderPath() throws FSServiceConfigException {
        Assert.assertEquals("/work/fsstorage/00/00/00/00/", config.getStoreFolderPath(ownerId1));
        Assert.assertEquals("/work/fsstorage/00/00/00/00/", config.getStoreFolderPath(ownerId12));
        Assert.assertEquals("/work/fsstorage/00/00/00/01/", config.getStoreFolderPath(ownerId123));
        Assert.assertEquals("/work/fsstorage/00/00/00/12/", config.getStoreFolderPath(ownerId1234));
        Assert.assertEquals("/work/fsstorage/00/00/01/23/", config.getStoreFolderPath(ownerId12345));
        Assert.assertEquals("/work/fsstorage/00/00/12/34/", config.getStoreFolderPath(ownerId123456));
        Assert.assertEquals("/work/fsstorage/00/01/23/45/", config.getStoreFolderPath(ownerId1234567));
        Assert.assertEquals("/work/fsstorage/00/12/34/56/", config.getStoreFolderPath(ownerId12345678));
        Assert.assertEquals("/work/fsstorage/01/23/45/67/", config.getStoreFolderPath(ownerId123456789));
    }

    @Test
    public void testStoringFilePathLEON() throws FSServiceConfigException {
        Assert.assertEquals(("000/000/1.dat"), (FSServiceConfig.getStoreFilePath(ownerId1, 9, 3, "dat", false)));
        Assert.assertEquals(("000/000/12.dat"), (FSServiceConfig.getStoreFilePath(ownerId12, 9, 3, "dat", false)));
        Assert.assertEquals(("000/000/123.dat"), (FSServiceConfig.getStoreFilePath(ownerId123, 9, 3, "dat", false)));
        Assert.assertEquals(("000/001/1234.dat"), (FSServiceConfig.getStoreFilePath(ownerId1234, 9, 3, "dat", false)));
        Assert.assertEquals(("000/012/12345.dat"), (FSServiceConfig.getStoreFilePath(ownerId12345, 9, 3, "dat", false)));
        Assert.assertEquals(("000/123/123456.dat"), (FSServiceConfig.getStoreFilePath(ownerId123456, 9, 3, "dat", false)));
        Assert.assertEquals(("001/234/1234567.dat"), (FSServiceConfig.getStoreFilePath(ownerId1234567, 9, 3, "dat", false)));
        Assert.assertEquals(("012/345/12345678.dat"), (FSServiceConfig.getStoreFilePath(ownerId12345678, 9, 3, "dat", false)));
        Assert.assertEquals(("123/456/123456789.dat"), (FSServiceConfig.getStoreFilePath(ownerId123456789, 9, 3, "dat", false)));
    }

    @Test
    public void testStoringFilePathLEON2() throws FSServiceConfigException {
        Assert.assertEquals(("00/00/00/00/1.dat"), (FSServiceConfig.getStoreFilePath(ownerId1, 10, 2, "dat", false)));
        Assert.assertEquals(("00/00/00/00/12.dat"), (FSServiceConfig.getStoreFilePath(ownerId12, 10, 2, "dat", false)));
        Assert.assertEquals(("00/00/00/01/123.dat"), (FSServiceConfig.getStoreFilePath(ownerId123, 10, 2, "dat", false)));
        Assert.assertEquals(("00/00/00/12/1234.dat"), (FSServiceConfig.getStoreFilePath(ownerId1234, 10, 2, "dat", false)));
        Assert.assertEquals(("00/00/01/23/12345.dat"), (FSServiceConfig.getStoreFilePath(ownerId12345, 10, 2, "dat", false)));
        Assert.assertEquals(("00/00/12/34/123456.dat"), (FSServiceConfig.getStoreFilePath(ownerId123456, 10, 2, "dat", false)));
        Assert.assertEquals(("00/01/23/45/1234567.dat"), (FSServiceConfig.getStoreFilePath(ownerId1234567, 10, 2, "dat", false)));
        Assert.assertEquals(("00/12/34/56/12345678.dat"), (FSServiceConfig.getStoreFilePath(ownerId12345678, 10, 2, "dat", false)));
        Assert.assertEquals(("01/23/45/67/123456789.dat"), (FSServiceConfig.getStoreFilePath(ownerId123456789, 10, 2, "dat", false)));
    }

    @Test
    public void testStoringFilePathLEON3() throws FSServiceConfigException {
        Assert.assertEquals(("0/0/0/0/1.dat"), (FSServiceConfig.getStoreFilePath(ownerId1, 5, 1, "dat", false)));
        Assert.assertEquals(("0/0/0/1/12.dat"), (FSServiceConfig.getStoreFilePath(ownerId12, 5, 1, "dat", false)));
        Assert.assertEquals(("0/0/1/2/123.dat"), (FSServiceConfig.getStoreFilePath(ownerId123, 5, 1, "dat", false)));
        Assert.assertEquals(("0/1/2/3/1234.dat"), (FSServiceConfig.getStoreFilePath(ownerId1234, 5, 1, "dat", false)));
        Assert.assertEquals(("1/2/3/4/12345.dat"), (FSServiceConfig.getStoreFilePath(ownerId12345, 5, 1, "dat", false)));


        Assert.assertEquals("t/e/s/t/_/t/e/s/test_test.dat",FSServiceConfig.getStoreFilePath(ownerId12345678910, ownerId12345678910.length()-1, 1, "dat", true));
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
            config.getStoreFileName(null);
            Assert.assertTrue(false);
        } catch (FSServiceConfigException fsse) {
            Assert.assertTrue(true);
        }

        try {
            config.getStoreFileName("");
            Assert.assertTrue(false);
        } catch (FSServiceConfigException fsse) {
            Assert.assertTrue(true);
        }

        try {
            config.getStoreFileName("12345678900");
            Assert.assertTrue(false);
        } catch (FSServiceConfigException fsse) {
            Assert.assertTrue(true);
        }

        try {
            config.getStoreFileName("abcd123");
            Assert.assertTrue(false);
        } catch (FSServiceConfigException fsse) {
            Assert.assertTrue(true);
        }
    }
}
