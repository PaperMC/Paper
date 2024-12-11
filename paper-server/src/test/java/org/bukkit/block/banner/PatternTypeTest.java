package org.bukkit.block.banner;

import static org.junit.jupiter.api.Assertions.*;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

@AllFeatures
public class PatternTypeTest {

    @Test
    public void testToBukkit() {
        for (BannerPattern nms : MinecraftServer.getDefaultRegistryAccess().lookupOrThrow(Registries.BANNER_PATTERN)) {
            PatternType bukkit = Registry.BANNER_PATTERN.get(CraftNamespacedKey.fromMinecraft(nms.assetId()));

            assertNotNull(bukkit, "No Bukkit banner pattern for " + nms + " " + nms);
        }
    }

    @Test
    public void testToNMS() {
        net.minecraft.core.Registry<BannerPattern> registry = MinecraftServer.getDefaultRegistryAccess().lookupOrThrow(Registries.BANNER_PATTERN);
        for (PatternType bukkit : PatternType.values()) {
            BannerPattern found = null;
            for (BannerPattern nms : registry) {
                NamespacedKey nmsKey = CraftNamespacedKey.fromMinecraft(registry.getKey(nms));
                if (bukkit.getKey().equals(nmsKey)) {
                    found = nms;
                    break;
                }
            }

            assertNotNull(found, "No NMS banner pattern for " + bukkit + " " + bukkit.getKey());
        }
    }
}
