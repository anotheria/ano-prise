package net.anotheria.anoprise.mock;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({MockTest.class, MockErrorsTest.class, SimpleTest.class})
public class MockTestSuite {

}
