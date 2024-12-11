package org.bukkit.support.suite;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite(failIfNoTests = false)
@SuiteDisplayName("Test suite for test which need registry values present, with the bundle feature flag set")
@IncludeTags("BundleFeature")
@SelectPackages({"org.bukkit", "io.papermc"})
@ConfigurationParameter(key = "TestSuite", value = "BundleFeature")
public class BundleFeatureTestSuite {
}
