package io.papermc.paper.world;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;

/**
 * All the built-in world clocks.
 */
public final class WorldClocks {

    // Start generate - WorldClocks
    public static final WorldClock OVERWORLD = get("overworld");

    public static final WorldClock THE_END = get("the_end");
    // End generate - WorldClocks

    private static WorldClock get(@KeyPattern.Value final String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.WORLD_CLOCK).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    private WorldClocks() {
    }
}
