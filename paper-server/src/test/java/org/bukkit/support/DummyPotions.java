package org.bukkit.support;

import net.minecraft.server.MobEffectList;

import org.bukkit.craftbukkit.potion.CraftPotionBrewer;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

public class DummyPotions {
    static {
        Potion.setPotionBrewer(new CraftPotionBrewer());
        MobEffectList.BLINDNESS.getClass();
        PotionEffectType.stopAcceptingRegistrations();
    }

    public static void setup() {}
}
