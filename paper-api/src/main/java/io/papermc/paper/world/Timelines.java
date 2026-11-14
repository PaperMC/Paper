package io.papermc.paper.world;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;

/**
 * All the built-in timelines.
 */
public final class Timelines {

    // Start generate - Timelines
    public static final Timeline DAY = get("day");

    public static final Timeline EARLY_GAME = get("early_game");

    public static final Timeline MOON = get("moon");

    public static final Timeline VILLAGER_SCHEDULE = get("villager_schedule");
    // End generate - Timelines

    private static Timeline get(@KeyPattern.Value final String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.TIMELINE).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    private Timelines() {
    }
}
