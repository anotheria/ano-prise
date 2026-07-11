package net.anotheria.anoprise.dualcrud;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({TestAlpha.class, TestDual.class})
public class DualCrudTestSuite {

}
