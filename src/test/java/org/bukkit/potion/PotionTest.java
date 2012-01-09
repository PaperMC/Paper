package org.bukkit.potion;

import static org.junit.Assert.*;

import org.bukkit.craftbukkit.potion.CraftPotionBrewer;
import org.junit.BeforeClass;
import org.junit.Test;

import net.minecraft.server.MobEffectList;

public class PotionTest {
    @BeforeClass
    public static void setUp() {
        Potion.setPotionBrewer(new CraftPotionBrewer());
        MobEffectList.BLINDNESS.getClass();
        PotionEffectType.stopAcceptingRegistrations();
    }

    @Test
    public void getEffects() {
        for (PotionType type : PotionType.values()) {
            for (PotionEffect effect : new Potion(type).getEffects()) {
                assertTrue(effect.getType() == PotionEffectType.getById(effect.getType().getId()));
            }
        }
    }
}
