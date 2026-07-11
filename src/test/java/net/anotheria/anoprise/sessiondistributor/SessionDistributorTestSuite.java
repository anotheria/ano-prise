package net.anotheria.anoprise.sessiondistributor;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ImplTest.class, SessionAttributeTest.class, DistributedSessionVOTest.class})
public class SessionDistributorTestSuite {

}
