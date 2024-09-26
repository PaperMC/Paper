package org.bukkit.support.suite;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.ExcludeClassNamePatterns;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite(failIfNoTests = false)
@SuiteDisplayName("Test suite for test which need registry values present, with all feature flags set")
@IncludeTags("AllFeatures")
@SelectPackages("org.bukkit")
@ExcludeClassNamePatterns("org.bukkit.craftbukkit.inventory.ItemStack.*Test")
@ConfigurationParameter(key = "TestSuite", value = "AllFeatures")
public class AllFeaturesTestSuite {
}
