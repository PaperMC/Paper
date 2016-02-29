package org.bukkit.support;

import net.minecraft.server.Enchantments;

public class DummyEnchantments {
    static {
        Enchantments.DAMAGE_ALL.getClass();
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void setup() {}
}
