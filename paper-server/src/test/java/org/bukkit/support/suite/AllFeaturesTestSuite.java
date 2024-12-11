package org.bukkit.support.suite;

import org.bukkit.registry.PerRegistryTest;
import org.bukkit.registry.RegistryClassTest;
import org.bukkit.registry.RegistryConversionTest;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.ExcludeClassNamePatterns;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite(failIfNoTests = false)
@SuiteDisplayName("Test suite for test which need registry values present, with all feature flags set")
@IncludeTags("AllFeatures")
@SelectPackages({"org.bukkit", "io.papermc"})
@SelectClasses({RegistryClassTest.class, PerRegistryTest.class, RegistryConversionTest.class}) // Make sure general registry tests are run first
@ExcludeClassNamePatterns("org.bukkit.craftbukkit.inventory.ItemStack.*Test")
@ConfigurationParameter(key = "TestSuite", value = "AllFeatures")
public class AllFeaturesTestSuite {
}
