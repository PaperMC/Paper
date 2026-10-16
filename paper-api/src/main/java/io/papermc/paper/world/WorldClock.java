package io.papermc.paper.world;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a world clock that tracks time for worlds and drives timelines.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
@NullMarked
public interface WorldClock extends Keyed {

    // Start generate - WorldClock
    WorldClock OVERWORLD = getWorldClock("overworld");

    WorldClock THE_END = getWorldClock("the_end");
    // End generate - WorldClock

    private static WorldClock getWorldClock(@KeyPattern.Value final String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.WORLD_CLOCK).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }
}
