package net.anotheria.anoprise.cache;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value=Suite.class)
@SuiteClasses(value={RoundRobinSoftReferenceExperimentalCacheTest.class,RoundRobinSoftReferenceCacheTest.class,RoundRobinHardwiredCacheTest.class,
		ExpiringCacheTest.class, TestConfigurationReading.class, ConfigurableRoundRobinHardwiredCacheTest.class, BoundedHardwiredCacheTester.class} )

public class CacheTestSuite {
	public static final int START_SIZE = 3000;
	public static final int MAX_SIZE = 5000;
}
