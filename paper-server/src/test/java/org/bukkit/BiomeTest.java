package org.bukkit;

import net.minecraft.server.BiomeBase;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.junit.Assert;
import org.junit.Test;

public class BiomeTest {

    @Test
    public void testBukkitToMinecraft() {
        for (Biome biome : Biome.values()) {
            Assert.assertNotNull("No NMS mapping for " + biome, CraftBlock.biomeToBiomeBase(biome));
        }
    }

    @Test
    public void testMinecraftToBukkit() {
        for (BiomeBase biome : BiomeBase.REGISTRY_ID) {
            Assert.assertNotNull("No Bukkit mapping for " + biome, CraftBlock.biomeBaseToBiome(biome));
        }
    }
}
