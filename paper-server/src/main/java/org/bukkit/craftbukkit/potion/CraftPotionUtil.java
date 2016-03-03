package org.bukkit.craftbukkit.potion;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.potion.PotionData;

public class CraftPotionUtil {

    private static final BiMap<PotionType, String> regular = ImmutableBiMap.<PotionType, String>builder()
            .put(PotionType.UNCRAFTABLE, "minecraft:empty")
            .put(PotionType.WATER, "minecraft:water")
            .put(PotionType.MUNDANE, "minecraft:mundane")
            .put(PotionType.THICK, "minecraft:thick")
            .put(PotionType.AWKWARD, "minecraft:awkward")
            .put(PotionType.NIGHT_VISION, "minecraft:night_vision")
            .put(PotionType.INVISIBILITY, "minecraft:invisibility")
            .put(PotionType.JUMP, "minecraft:leaping")
            .put(PotionType.FIRE_RESISTANCE, "minecraft:fire_resistance")
            .put(PotionType.SPEED, "minecraft:swiftness")
            .put(PotionType.SLOWNESS, "minecraft:slowness")
            .put(PotionType.WATER_BREATHING, "minecraft:water_breathing")
            .put(PotionType.INSTANT_HEAL, "minecraft:healing")
            .put(PotionType.INSTANT_DAMAGE, "minecraft:harming")
            .put(PotionType.POISON, "minecraft:poison")
            .put(PotionType.REGEN, "minecraft:regeneration")
            .put(PotionType.STRENGTH, "minecraft:strength")
            .put(PotionType.WEAKNESS, "minecraft:weakness")
            .put(PotionType.LUCK, "minecraft:luck")
            .build();
    private static final BiMap<PotionType, String> upgradeable = ImmutableBiMap.<PotionType, String>builder()
            .put(PotionType.JUMP, "minecraft:strong_leaping")
            .put(PotionType.SPEED, "minecraft:strong_swiftness")
            .put(PotionType.INSTANT_HEAL, "minecraft:strong_healing")
            .put(PotionType.INSTANT_DAMAGE, "minecraft:strong_harming")
            .put(PotionType.POISON, "minecraft:strong_poison")
            .put(PotionType.REGEN, "minecraft:strong_regeneration")
            .put(PotionType.STRENGTH, "minecraft:strong_strength")
            .build();
    private static final BiMap<PotionType, String> extendable = ImmutableBiMap.<PotionType, String>builder()
            .put(PotionType.NIGHT_VISION, "minecraft:long_night_vision")
            .put(PotionType.INVISIBILITY, "minecraft:long_invisibility")
            .put(PotionType.JUMP, "minecraft:long_leaping")
            .put(PotionType.FIRE_RESISTANCE, "minecraft:long_fire_resistance")
            .put(PotionType.SPEED, "minecraft:long_swiftness")
            .put(PotionType.SLOWNESS, "minecraft:long_slowness")
            .put(PotionType.WATER_BREATHING, "minecraft:long_water_breathing")
            .put(PotionType.POISON, "minecraft:long_poison")
            .put(PotionType.REGEN, "minecraft:long_regeneration")
            .put(PotionType.STRENGTH, "minecraft:long_strength")
            .put(PotionType.WEAKNESS, "minecraft:long_weakness")
            .build();

    public static String fromBukkit(PotionData data) {
        if (data.isUpgraded()) {
            return upgradeable.get(data.getType());
        }
        if (data.isExtended()) {
            return extendable.get(data.getType());
        }
        return regular.get(data.getType());
    }

    public static PotionData toBukkit(String type) {
        PotionType potionType = null;
        potionType = extendable.inverse().get(type);
        if (potionType != null) {
            return new PotionData(potionType, true, false);
        }
        potionType = upgradeable.inverse().get(type);
        if (potionType != null) {
            return new PotionData(potionType, false, true);
        }
        return new PotionData(regular.inverse().get(type), false, false);
    }

    public static MobEffect fromBukkit(PotionEffect effect) {
        MobEffectList type = MobEffectList.fromId(effect.getType().getId());
        return new MobEffect(type, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles());
    }

    public static PotionEffect toBukkit(MobEffect effect) {
        PotionEffectType type = PotionEffectType.getById(MobEffectList.getId(effect.getMobEffect()));
        int amp = effect.getAmplifier();
        int duration = effect.getDuration();
        boolean ambient = effect.isAmbient();
        boolean particles = effect.isShowParticles();
        return new PotionEffect(type, duration, amp, ambient, particles);
    }

    public static boolean equals(MobEffectList mobEffect, PotionEffectType type) {
        PotionEffectType typeV = PotionEffectType.getById(MobEffectList.getId(mobEffect));
        return typeV.equals(type);
    }
}
