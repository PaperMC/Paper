package org.bukkit.support.suite;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite(failIfNoTests = false)
@SuiteDisplayName("Test suite for slow tests, which don't need to run every time")
@IncludeTags("Slow")
@SelectPackages({"org.bukkit", "io.papermc"})
@ConfigurationParameter(key = "TestSuite", value = "Slow")
public class SlowTestSuite {
}
