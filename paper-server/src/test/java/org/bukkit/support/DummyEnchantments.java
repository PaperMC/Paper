package org.bukkit.support;

import net.minecraft.world.item.enchantment.Enchantments;

public class DummyEnchantments {
    static {
        Enchantments.DAMAGE_ALL.getClass();
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void setup() {}
}
