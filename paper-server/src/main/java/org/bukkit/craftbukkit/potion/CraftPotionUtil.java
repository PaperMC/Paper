package org.bukkit.craftbukkit.potion;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class CraftPotionUtil {

    private static final BiMap<PotionType, PotionType> upgradeable = ImmutableBiMap.<PotionType, PotionType>builder()
            // Start generate - CraftPotionUtil#upgradeable
            // @GeneratedFrom 1.21.6-pre1
            .put(PotionType.HARMING, PotionType.STRONG_HARMING)
            .put(PotionType.HEALING, PotionType.STRONG_HEALING)
            .put(PotionType.LEAPING, PotionType.STRONG_LEAPING)
            .put(PotionType.POISON, PotionType.STRONG_POISON)
            .put(PotionType.REGENERATION, PotionType.STRONG_REGENERATION)
            .put(PotionType.SLOWNESS, PotionType.STRONG_SLOWNESS)
            .put(PotionType.STRENGTH, PotionType.STRONG_STRENGTH)
            .put(PotionType.SWIFTNESS, PotionType.STRONG_SWIFTNESS)
            .put(PotionType.TURTLE_MASTER, PotionType.STRONG_TURTLE_MASTER)
            // End generate - CraftPotionUtil#upgradeable
            .build();
    private static final BiMap<PotionType, PotionType> extendable = ImmutableBiMap.<PotionType, PotionType>builder()
            // Start generate - CraftPotionUtil#extendable
            // @GeneratedFrom 1.21.6-pre1
            .put(PotionType.FIRE_RESISTANCE, PotionType.LONG_FIRE_RESISTANCE)
            .put(PotionType.INVISIBILITY, PotionType.LONG_INVISIBILITY)
            .put(PotionType.LEAPING, PotionType.LONG_LEAPING)
            .put(PotionType.NIGHT_VISION, PotionType.LONG_NIGHT_VISION)
            .put(PotionType.POISON, PotionType.LONG_POISON)
            .put(PotionType.REGENERATION, PotionType.LONG_REGENERATION)
            .put(PotionType.SLOW_FALLING, PotionType.LONG_SLOW_FALLING)
            .put(PotionType.SLOWNESS, PotionType.LONG_SLOWNESS)
            .put(PotionType.STRENGTH, PotionType.LONG_STRENGTH)
            .put(PotionType.SWIFTNESS, PotionType.LONG_SWIFTNESS)
            .put(PotionType.TURTLE_MASTER, PotionType.LONG_TURTLE_MASTER)
            .put(PotionType.WATER_BREATHING, PotionType.LONG_WATER_BREATHING)
            .put(PotionType.WEAKNESS, PotionType.LONG_WEAKNESS)
            // End generate - CraftPotionUtil#extendable
            .build();

    public static PotionType fromBukkit(PotionData data) {
        if (data == null) {
            return null;
        }

        PotionType type;
        if (data.isUpgraded()) {
            type = CraftPotionUtil.upgradeable.get(data.getType());
        } else if (data.isExtended()) {
            type = CraftPotionUtil.extendable.get(data.getType());
        } else {
            type = data.getType();
        }
        Preconditions.checkNotNull(type, "Unknown potion type from data " + data);

        return type;
    }

    public static PotionData toBukkit(PotionType type) {
        if (type == null) {
            return null;
        }

        PotionType potionType;
        potionType = CraftPotionUtil.extendable.inverse().get(type);
        if (potionType != null) {
            return new PotionData(potionType, true, false);
        }
        potionType = CraftPotionUtil.upgradeable.inverse().get(type);
        if (potionType != null) {
            return new PotionData(potionType, false, true);
        }

        return new PotionData(type, false, false);
    }

    public static MobEffectInstance fromBukkit(PotionEffect effect) {
        Holder<MobEffect> type = CraftPotionEffectType.bukkitToMinecraftHolder(effect.getType());
        // Note: do not copy over the hidden effect, as this method is only used for applying to entities which we do not want to convert over.
        return new MobEffectInstance(type, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles(), effect.hasIcon());
    }

    public static PotionEffect toBukkit(MobEffectInstance effect) {
        PotionEffectType type = CraftPotionEffectType.minecraftHolderToBukkit(effect.getEffect());
        int amp = effect.getAmplifier();
        int duration = effect.getDuration();
        boolean ambient = effect.isAmbient();
        boolean particles = effect.isVisible();
        return new PotionEffect(type, duration, amp, ambient, particles, effect.showIcon(), effect.hiddenEffect == null ? null : toBukkit(effect.hiddenEffect));
    }

    public static boolean equals(Holder<MobEffect> mobEffect, PotionEffectType type) {
        PotionEffectType typeV = CraftPotionEffectType.minecraftHolderToBukkit(mobEffect);
        return typeV.equals(type);
    }
}
