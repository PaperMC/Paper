package org.bukkit;

import net.minecraft.data.RegistryGeneration;
import net.minecraft.world.level.biome.BiomeBase;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;

public class BiomeTest extends AbstractTestingBase {

    @Test
    public void testBukkitToMinecraft() {
        for (Biome biome : Biome.values()) {
            if (biome == Biome.CUSTOM) {
                continue;
            }

            Assert.assertNotNull("No NMS mapping for " + biome, CraftBlock.biomeToBiomeBase(RegistryGeneration.WORLDGEN_BIOME, biome));
        }
    }

    @Test
    public void testMinecraftToBukkit() {
        for (BiomeBase biomeBase : RegistryGeneration.WORLDGEN_BIOME) {
            Biome biome = CraftBlock.biomeBaseToBiome(RegistryGeneration.WORLDGEN_BIOME, biomeBase);
            Assert.assertTrue("No Bukkit mapping for " + biomeBase, biome != null && biome != Biome.CUSTOM);
        }
    }
}
