package org.bukkit.support;

import net.minecraft.server.MobEffects;

import org.bukkit.craftbukkit.potion.CraftPotionBrewer;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

public class DummyPotions {
    static {
        Potion.setPotionBrewer(new CraftPotionBrewer());
        MobEffects.BLINDNESS.getClass();
        PotionEffectType.stopAcceptingRegistrations();
    }

    public static void setup() {}
}
