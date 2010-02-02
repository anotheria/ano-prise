package net.anotheria.anoprise.sessiondistributor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value=Suite.class)
@SuiteClasses(value={ImplTest.class, SessionAttributeTest.class, SessionHolderTest.class } )
public class SessionDistributorTestSuite {

}
