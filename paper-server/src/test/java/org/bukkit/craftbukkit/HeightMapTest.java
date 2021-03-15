package org.bukkit.craftbukkit;

import org.bukkit.HeightMap;
import org.junit.Assert;
import org.junit.Test;

public class HeightMapTest {

    @Test
    public void heightMapConversionFromNMSToBukkitShouldNotThrowExceptio() {
        for (net.minecraft.world.level.levelgen.HeightMap.Type nmsHeightMapType : net.minecraft.world.level.levelgen.HeightMap.Type.values()) {
            Assert.assertNotNull("fromNMS", CraftHeightMap.fromNMS(nmsHeightMapType));
        }
    }

    @Test
    public void heightMapConversionFromBukkitToNMSShouldNotThrowExceptio() {
        for (HeightMap bukkitHeightMap : HeightMap.values()) {
            Assert.assertNotNull("toNMS", CraftHeightMap.toNMS(bukkitHeightMap));
        }
    }
}
