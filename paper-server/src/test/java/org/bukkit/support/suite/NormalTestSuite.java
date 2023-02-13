package org.bukkit.support.suite;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite(failIfNoTests = false)
@SuiteDisplayName("Test suite for standalone tests, which don't need any registry values present")
@IncludeTags("Normal")
@SelectPackages({"org.bukkit", "io.papermc"})
@ConfigurationParameter(key = "TestSuite", value = "Normal")
public class NormalTestSuite {
}
