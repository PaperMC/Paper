package org.bukkit.inventory.meta.trim;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
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

    // Start generate - TrimPattern
    // @GeneratedFrom 1.21.6-pre1
    TrimPattern BOLT = getTrimPattern("bolt");

    TrimPattern COAST = getTrimPattern("coast");

    TrimPattern DUNE = getTrimPattern("dune");

    TrimPattern EYE = getTrimPattern("eye");

    TrimPattern FLOW = getTrimPattern("flow");

    TrimPattern HOST = getTrimPattern("host");

    TrimPattern RAISER = getTrimPattern("raiser");

    TrimPattern RIB = getTrimPattern("rib");

    TrimPattern SENTRY = getTrimPattern("sentry");

    TrimPattern SHAPER = getTrimPattern("shaper");

    TrimPattern SILENCE = getTrimPattern("silence");

    TrimPattern SNOUT = getTrimPattern("snout");

    TrimPattern SPIRE = getTrimPattern("spire");

    TrimPattern TIDE = getTrimPattern("tide");

    TrimPattern VEX = getTrimPattern("vex");

    TrimPattern WARD = getTrimPattern("ward");

    TrimPattern WAYFINDER = getTrimPattern("wayfinder");

    TrimPattern WILD = getTrimPattern("wild");
    // End generate - TrimPattern

    @NotNull
    private static TrimPattern getTrimPattern(@NotNull String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).getOrThrow(NamespacedKey.minecraft(key));
    }

    // Paper start - adventure
    /**
     * Get the description of this {@link TrimPattern}.
     *
     * @return the description
     */
    net.kyori.adventure.text.@org.jetbrains.annotations.NotNull Component description();

    /**
     * @deprecated this method assumes that {@link #description()} will
     * always be a translatable component which is not guaranteed.
     */
    @Override
    @Deprecated(forRemoval = true)
    @org.jetbrains.annotations.NotNull String getTranslationKey();
    // Paper end - adventure

    // Paper start - Registry#getKey
    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#TRIM_PATTERN}. TrimPatterns can exist without a key.
     */
    @Deprecated(forRemoval = true, since = "1.20.4")
    @Override
    org.bukkit.@org.jetbrains.annotations.NotNull NamespacedKey getKey();

    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#TRIM_PATTERN}. TrimPatterns can exist without a key.
     */
    @Deprecated(forRemoval = true, since = "1.20.4")
    @Override
    default net.kyori.adventure.key.@org.jetbrains.annotations.NotNull Key key() {
        return org.bukkit.Keyed.super.key();
    }
    // Paper end - Registry#getKey
}
