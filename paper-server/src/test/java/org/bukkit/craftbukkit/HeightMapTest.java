package org.bukkit.craftbukkit;

import static org.junit.jupiter.api.Assertions.*;
import org.bukkit.HeightMap;
import org.junit.jupiter.api.Test;

public class HeightMapTest {

    @Test
    public void heightMapConversionFromNMSToBukkitShouldNotThrowExceptio() {
        for (net.minecraft.world.level.levelgen.HeightMap.Type nmsHeightMapType : net.minecraft.world.level.levelgen.HeightMap.Type.values()) {
            assertNotNull(CraftHeightMap.fromNMS(nmsHeightMapType), "fromNMS");
        }
    }

    @Test
    public void heightMapConversionFromBukkitToNMSShouldNotThrowExceptio() {
        for (HeightMap bukkitHeightMap : HeightMap.values()) {
            assertNotNull(CraftHeightMap.toNMS(bukkitHeightMap), "toNMS");
        }
    }
}
