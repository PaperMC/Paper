package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class EnchantmentTest extends AbstractTestingBase {

    @Test
    public void verifyMapping() {
        for (MinecraftKey key : BuiltInRegistries.ENCHANTMENT.keySet()) {
            net.minecraft.world.item.enchantment.Enchantment nms = BuiltInRegistries.ENCHANTMENT.get(key);

            Enchantment bukkitById = Enchantment.getByKey(CraftNamespacedKey.fromMinecraft(key));

            assertFalse(bukkitById.getName().startsWith("UNKNOWN"), "Unknown enchant name for " + key);

            assertNotNull(bukkitById.getItemTarget(), "Unknown target for " + key);
        }
    }
}
