package org.bukkit.inventory.meta.trim;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Translatable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a pattern that may be used in an {@link ArmorTrim}.
 */
public interface TrimPattern extends Keyed, Translatable {

    /**
     * {@link Material#SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern SENTRY = getTrimPattern("sentry");
    /**
     * {@link Material#DUNE_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern DUNE = getTrimPattern("dune");
    /**
     * {@link Material#COAST_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern COAST = getTrimPattern("coast");
    /**
     * {@link Material#WILD_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern WILD = getTrimPattern("wild");
    /**
     * {@link Material#WARD_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern WARD = getTrimPattern("ward");
    /**
     * {@link Material#EYE_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern EYE = getTrimPattern("eye");
    /**
     * {@link Material#VEX_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern VEX = getTrimPattern("vex");
    /**
     * {@link Material#TIDE_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern TIDE = getTrimPattern("tide");
    /**
     * {@link Material#SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern SNOUT = getTrimPattern("snout");
    /**
     * {@link Material#RIB_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern RIB = getTrimPattern("rib");
    /**
     * {@link Material#SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern SPIRE = getTrimPattern("spire");
    /**
     * {@link Material#WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern WAYFINDER = getTrimPattern("wayfinder");
    /**
     * {@link Material#SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern SHAPER = getTrimPattern("shaper");
    /**
     * {@link Material#SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern SILENCE = getTrimPattern("silence");
    /**
     * {@link Material#RAISER_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern RAISER = getTrimPattern("raiser");
    /**
     * {@link Material#HOST_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern HOST = getTrimPattern("host");
    /**
     * {@link Material#FLOW_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern FLOW = getTrimPattern("flow");
    /**
     * {@link Material#BOLT_ARMOR_TRIM_SMITHING_TEMPLATE}.
     */
    public static final TrimPattern BOLT = getTrimPattern("bolt");

    @NotNull
    private static TrimPattern getTrimPattern(@NotNull String key) {
        return Registry.TRIM_PATTERN.getOrThrow(NamespacedKey.minecraft(key));
    }
}
