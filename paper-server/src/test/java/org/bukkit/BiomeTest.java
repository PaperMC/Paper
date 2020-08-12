package org.bukkit;

import net.minecraft.server.BiomeBase;
import net.minecraft.server.RegistryGeneration;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;

public class BiomeTest extends AbstractTestingBase {

    @Test
    public void testBukkitToMinecraft() {
        for (Biome biome : Biome.values()) {
            Assert.assertNotNull("No NMS mapping for " + biome, CraftBlock.biomeToBiomeBase(RegistryGeneration.WORLDGEN_BIOME, biome));
        }
    }

    @Test
    public void testMinecraftToBukkit() {
        for (Object biome : RegistryGeneration.WORLDGEN_BIOME) {
            Assert.assertNotNull("No Bukkit mapping for " + biome, CraftBlock.biomeBaseToBiome(RegistryGeneration.WORLDGEN_BIOME, (BiomeBase) biome));
        }
    }
}
