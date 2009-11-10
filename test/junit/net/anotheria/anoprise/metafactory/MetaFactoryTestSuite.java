package net.anotheria.anoprise.metafactory;

import org.configureme.ConfigurationManager;
import org.configureme.environments.DynamicEnvironment;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value=Suite.class)
@SuiteClasses(value={MetaFactoryTest.class, ConfigurableResolverTest.class} )
public class MetaFactoryTestSuite {
	@BeforeClass public static void setConfigureMe(){
		ConfigurationManager.INSTANCE.setDefaultEnvironment(new DynamicEnvironment("test", "junut"));
	}
}
