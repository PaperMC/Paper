package org.bukkit.craftbukkit.potion;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectList;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftPotionUtil {

    public static MobEffect fromBukkit(PotionEffect effect) {
        Holder<MobEffectList> type = CraftPotionEffectType.bukkitToMinecraftHolder(effect.getType());
        return new MobEffect(type, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles());
    }

    public static PotionEffect toBukkit(MobEffect effect) {
        PotionEffectType type = CraftPotionEffectType.minecraftHolderToBukkit(effect.getEffect());
        int amp = effect.getAmplifier();
        int duration = effect.getDuration();
        boolean ambient = effect.isAmbient();
        boolean particles = effect.isVisible();
        return new PotionEffect(type, duration, amp, ambient, particles);
    }

    public static boolean equals(Holder<MobEffectList> mobEffect, PotionEffectType type) {
        PotionEffectType typeV = CraftPotionEffectType.minecraftHolderToBukkit(mobEffect);
        return typeV.equals(type);
    }
}
