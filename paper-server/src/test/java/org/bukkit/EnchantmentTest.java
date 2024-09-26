package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

@AllFeatures
public class EnchantmentTest {

    @Test
    public void verifyMapping() {
        for (MinecraftKey key : CraftRegistry.getMinecraftRegistry(Registries.ENCHANTMENT).keySet()) {
            net.minecraft.world.item.enchantment.Enchantment nms = CraftRegistry.getMinecraftRegistry(Registries.ENCHANTMENT).get(key);

            Enchantment bukkitById = Enchantment.getByKey(CraftNamespacedKey.fromMinecraft(key));

            assertFalse(bukkitById.getName().startsWith("UNKNOWN"), "Unknown enchant name for " + key);
        }
    }
}
