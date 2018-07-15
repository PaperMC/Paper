package org.bukkit;

import net.minecraft.server.MinecraftKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;

public class EnchantmentTest extends AbstractTestingBase {

    @Test
    public void verifyMapping() {
        for (MinecraftKey key : net.minecraft.server.Enchantment.enchantments.keySet()) {
            net.minecraft.server.Enchantment nms = net.minecraft.server.Enchantment.enchantments.get(key);

            Enchantment bukkitById = Enchantment.getByKey(CraftNamespacedKey.fromMinecraft(key));

            Assert.assertFalse("Unknown enchant name for " + key, bukkitById.getName().startsWith("UNKNOWN"));

            Assert.assertNotNull("Unknown target for " + key, bukkitById.getItemTarget());
        }
    }
}
