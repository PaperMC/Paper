package org.bukkit;

import org.jetbrains.annotations.ApiStatus;

/**
 * This represents a Feature Flag for a World.
 * <br>
 * Flags which are unavailable in the current version will be null and/or
 * removed.
 */
@ApiStatus.Experimental
public interface FeatureFlag extends Keyed {

    public static final FeatureFlag VANILLA = Bukkit.getUnsafe().getFeatureFlag(NamespacedKey.minecraft("vanilla"));

    public static final FeatureFlag BUNDLE = Bukkit.getUnsafe().getFeatureFlag(NamespacedKey.minecraft("bundle"));

    /**
     * <strong>AVAILABLE BETWEEN VERSIONS:</strong> 1.19 - 1.19.4
     *
     * @deprecated not available since 1.20
     */
    @Deprecated
    public static final FeatureFlag UPDATE_1_20 = Bukkit.getUnsafe().getFeatureFlag(NamespacedKey.minecraft("update_1_20"));

    public static final FeatureFlag TRADE_REBALANCE = Bukkit.getUnsafe().getFeatureFlag(NamespacedKey.minecraft("trade_rebalance"));

    public static final FeatureFlag UPDATE_121 = Bukkit.getUnsafe().getFeatureFlag(NamespacedKey.minecraft("update_1_21"));
}
