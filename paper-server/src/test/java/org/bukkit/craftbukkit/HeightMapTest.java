package org.bukkit.craftbukkit;

import static org.junit.jupiter.api.Assertions.*;
import org.bukkit.HeightMap;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class HeightMapTest {

    @Test
    public void heightMapConversionFromNMSToBukkitShouldNotThrowExceptio() {
        for (net.minecraft.world.level.levelgen.Heightmap.Types nmsHeightMapType : net.minecraft.world.level.levelgen.Heightmap.Types.values()) {
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
