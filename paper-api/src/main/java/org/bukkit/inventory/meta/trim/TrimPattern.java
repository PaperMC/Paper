package org.bukkit.inventory.meta.trim;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

/**
 * Represents a pattern that may be used in an {@link ArmorTrim}.
 */
public interface TrimPattern extends Keyed {

    /**
     * {@link Material#SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern SENTRY = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("sentry"));
    /**
     * {@link Material#DUNE_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern DUNE = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("dune"));
    /**
     * {@link Material#COAST_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern COAST = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("coast"));
    /**
     * {@link Material#WILD_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern WILD = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("wild"));
    /**
     * {@link Material#WARD_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern WARD = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("ward"));
    /**
     * {@link Material#EYE_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern EYE = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("eye"));
    /**
     * {@link Material#VEX_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern VEX = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("vex"));
    /**
     * {@link Material#TIDE_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern TIDE = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("tide"));
    /**
     * {@link Material#SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern SNOUT = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("snout"));
    /**
     * {@link Material#RIB_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern RIB = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("rib"));
    /**
     * {@link Material#SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern SPIRE = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("spire"));
    /**
     * {@link Material#WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern WAYFINDER = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("wayfinder"));
    /**
     * {@link Material#SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern SHAPER = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("shaper"));
    /**
     * {@link Material#SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern SILENCE = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("silence"));
    /**
     * {@link Material#RAISER_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern RAISER = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("raiser"));
    /**
     * {@link Material#HOST_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern HOST = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("host"));
}
