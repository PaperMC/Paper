package org.bukkit.craftbukkit.potion;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectList;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class CraftPotionUtil {

    private static final BiMap<PotionType, PotionType> upgradeable = ImmutableBiMap.<PotionType, PotionType>builder()
            .put(PotionType.JUMP, PotionType.STRONG_LEAPING)
            .put(PotionType.SPEED, PotionType.STRONG_SWIFTNESS)
            .put(PotionType.INSTANT_HEAL, PotionType.STRONG_HEALING)
            .put(PotionType.INSTANT_DAMAGE, PotionType.STRONG_HARMING)
            .put(PotionType.POISON, PotionType.STRONG_POISON)
            .put(PotionType.REGEN, PotionType.STRONG_REGENERATION)
            .put(PotionType.STRENGTH, PotionType.STRONG_STRENGTH)
            .put(PotionType.SLOWNESS, PotionType.STRONG_SLOWNESS)
            .put(PotionType.TURTLE_MASTER, PotionType.STRONG_TURTLE_MASTER)
            .build();
    private static final BiMap<PotionType, PotionType> extendable = ImmutableBiMap.<PotionType, PotionType>builder()
            .put(PotionType.NIGHT_VISION, PotionType.LONG_NIGHT_VISION)
            .put(PotionType.INVISIBILITY, PotionType.LONG_INVISIBILITY)
            .put(PotionType.JUMP, PotionType.LONG_LEAPING)
            .put(PotionType.FIRE_RESISTANCE, PotionType.LONG_FIRE_RESISTANCE)
            .put(PotionType.SPEED, PotionType.LONG_SWIFTNESS)
            .put(PotionType.SLOWNESS, PotionType.LONG_SLOWNESS)
            .put(PotionType.WATER_BREATHING, PotionType.LONG_WATER_BREATHING)
            .put(PotionType.POISON, PotionType.LONG_POISON)
            .put(PotionType.REGEN, PotionType.LONG_REGENERATION)
            .put(PotionType.STRENGTH, PotionType.LONG_STRENGTH)
            .put(PotionType.WEAKNESS, PotionType.LONG_WEAKNESS)
            .put(PotionType.TURTLE_MASTER, PotionType.LONG_TURTLE_MASTER)
            .put(PotionType.SLOW_FALLING, PotionType.LONG_SLOW_FALLING)
            .build();

    public static PotionType fromBukkit(PotionData data) {
        PotionType type;
        if (data.isUpgraded()) {
            type = upgradeable.get(data.getType());
        } else if (data.isExtended()) {
            type = extendable.get(data.getType());
        } else {
            type = data.getType();
        }
        Preconditions.checkNotNull(type, "Unknown potion type from data " + data);

        return type;
    }

    public static PotionData toBukkit(PotionType type) {
        if (type == null) {
            return new PotionData(PotionType.UNCRAFTABLE, false, false);
        }

        PotionType potionType;
        potionType = extendable.inverse().get(type);
        if (potionType != null) {
            return new PotionData(potionType, true, false);
        }
        potionType = upgradeable.inverse().get(type);
        if (potionType != null) {
            return new PotionData(potionType, false, true);
        }

        return new PotionData(type, false, false);
    }

    public static MobEffect fromBukkit(PotionEffect effect) {
        MobEffectList type = CraftPotionEffectType.bukkitToMinecraft(effect.getType());
        return new MobEffect(type, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles());
    }

    public static PotionEffect toBukkit(MobEffect effect) {
        PotionEffectType type = CraftPotionEffectType.minecraftToBukkit(effect.getEffect());
        int amp = effect.getAmplifier();
        int duration = effect.getDuration();
        boolean ambient = effect.isAmbient();
        boolean particles = effect.isVisible();
        return new PotionEffect(type, duration, amp, ambient, particles);
    }

    public static boolean equals(MobEffectList mobEffect, PotionEffectType type) {
        PotionEffectType typeV = CraftPotionEffectType.minecraftToBukkit(mobEffect);
        return typeV.equals(type);
    }
}
