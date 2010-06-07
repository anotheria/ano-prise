package net.anotheria.anoprise.dataspace.fixture;

import net.anotheria.anoprise.dataspace.Dataspace;
import net.anotheria.anoprise.dataspace.DataspaceService;
import net.anotheria.anoprise.dataspace.DataspaceServiceException;
import net.anotheria.anoprise.dataspace.DataspaceType;
import net.anotheria.anoprise.dataspace.attribute.Attribute;
import net.anotheria.anoprise.dataspace.attribute.StringAttribute;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * DataspaceService test for fixture implementation.
 */
public class DataspaceServiceFixtureImplTest {

	/**
	 * Fixture service.
	 */
	private static DataspaceService service;

	private static DataspaceType dataspaceBanerType;

	private static DataspaceType dataspaceAnotherType;

	@BeforeClass
	public static void init() {
		service = new DataspaceServiceFixtureFactory().create();
		dataspaceBanerType = new BannerDataspaceType();
		dataspaceAnotherType = new AnotherDataspaceType();
	}

	@Test
	public void basicTest() {
		Dataspace newSpace = new Dataspace("111", dataspaceBanerType);
		Attribute attr = new StringAttribute("Banner123", "true");
		newSpace.addAttribute(attr.getName(), attr);

		Dataspace newSpace2 = new Dataspace("111", dataspaceAnotherType);
		Attribute attr2 = new StringAttribute("Another456", "false");
		newSpace2.addAttribute(attr2.getName(), attr2);

		try {
			service.saveDataspace(newSpace);
			service.saveDataspace(newSpace2);

			Dataspace retriviedDataspace = service.getDataspace("111", dataspaceBanerType);
			Dataspace retriviedDataspace2 = service.getDataspace("111", dataspaceAnotherType);
			Assert.assertEquals(newSpace, retriviedDataspace);
			Assert.assertEquals(newSpace2, retriviedDataspace2);

			retriviedDataspace.setUserId("222");
			retriviedDataspace2.setUserId("333");
			Assert.assertNotSame(newSpace, retriviedDataspace);
			Assert.assertNotSame(newSpace2, retriviedDataspace2);
		} catch (DataspaceServiceException e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test dataspace type.
	 * 
	 * @author abolbat
	 */
	private static final class BannerDataspaceType implements DataspaceType {

		@Override
		public int getId() {
			return 1;
		}

		@Override
		public String getName() {
			return "BANNER";
		}

	}

	/**
	 * Test dataspace type.
	 * 
	 * @author abolbat
	 */
	private static final class AnotherDataspaceType implements DataspaceType {

		@Override
		public int getId() {
			return 2;
		}

		@Override
		public String getName() {
			return "ANOTHER_TYPE";
		}

	}

}
