package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.core.registries.Registries;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

@AllFeatures
public class BiomeTest {

    @Test
    public void testBukkitToMinecraft() {
        for (Biome biome : Biome.values()) {
            if (biome == Biome.CUSTOM) {
                continue;
            }

            assertNotNull(CraftBiome.bukkitToMinecraftHolder(biome), "No NMS mapping for " + biome);
        }
    }

    @Test
    public void testMinecraftToBukkit() {
        for (net.minecraft.world.level.biome.Biome biomeBase : CraftRegistry.getMinecraftRegistry(Registries.BIOME)) {
            Biome biome = CraftBiome.minecraftToBukkit(biomeBase);
            assertTrue(biome != null && biome != Biome.CUSTOM, "No Bukkit mapping for " + biomeBase);
        }
    }
}
