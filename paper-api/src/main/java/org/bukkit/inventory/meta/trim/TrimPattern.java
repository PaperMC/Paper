package org.bukkit.inventory.meta.trim;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.InlinedRegistryBuilderProvider;
import io.papermc.paper.registry.data.TrimPatternRegistryEntry;
import java.util.function.Consumer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Translatable;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a pattern that may be used in an {@link ArmorTrim}.
 */
@NullMarked
public interface TrimPattern extends Keyed, Translatable {

    /**
     * Creates an inlined trim pattern.
     *
     * @param value a consumer for the builder factory
     * @return the created trim pattern
     */
    @ApiStatus.Experimental
    static TrimPattern create(final Consumer<RegistryBuilderFactory<TrimPattern, ? extends TrimPatternRegistryEntry.Builder>> value) {
        return InlinedRegistryBuilderProvider.instance().createTrimPattern(value);
    }

    // Start generate - TrimPattern
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

    private static TrimPattern getTrimPattern(@KeyPattern.Value final String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    // Paper start - adventure
    /**
     * Get the description of this {@link TrimPattern}.
     *
     * @return the description
     */
    Component description();

    /**
     * @deprecated this method assumes that {@link #description()} will
     * always be a translatable component which is not guaranteed.
     */
    @Override
    @Deprecated(forRemoval = true)
    String getTranslationKey();
    // Paper end - adventure

    // Paper start - Registry#getKey
    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#TRIM_PATTERN}. TrimPatterns can exist without a key.
     */
    @Deprecated(forRemoval = true, since = "1.20.4")
    @Override
    NamespacedKey getKey();

    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#TRIM_PATTERN}. TrimPatterns can exist without a key.
     */
    @Deprecated(forRemoval = true, since = "1.20.4")
    @Override
    default Key key() {
        return Keyed.super.key();
    }
    // Paper end - Registry#getKey
}
