package io.papermc.paper.world;

import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a world clock that tracks time for worlds and drives timelines.
 *
 * @see WorldClocks
 */
@ApiStatus.NonExtendable
public interface WorldClock extends Keyed {
}
