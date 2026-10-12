package org.bukkit.support.suite;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeTags("VanillaFeature")
@SelectPackages("net.minecraft.world.entity")
@ConfigurationParameter(key = "TestSuite", value = "VanillaFeature")
public class LegacyCombatTestSuite {
}
