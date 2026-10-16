package io.papermc.paper.world;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a timeline that can apply environment attributes or define time markers for a {@link WorldClock}.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
@NullMarked
public interface Timeline extends Keyed {

    // Start generate - Timeline
    Timeline DAY = getTimeline("day");

    Timeline EARLY_GAME = getTimeline("early_game");

    Timeline MOON = getTimeline("moon");

    Timeline VILLAGER_SCHEDULE = getTimeline("villager_schedule");
    // End generate - Timeline

    private static Timeline getTimeline(@KeyPattern.Value final String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.TIMELINE).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }
}
