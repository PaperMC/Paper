package org.bukkit.support;

import net.minecraft.server.Enchantment;

public class DummyEnchantments {
    static {
        Enchantment.getEffects();
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void setup() {}
}
