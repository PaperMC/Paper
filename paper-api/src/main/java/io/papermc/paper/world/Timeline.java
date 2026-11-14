package io.papermc.paper.world;

import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a timeline that can apply environment attributes or define time markers for a {@link WorldClock}.
 *
 * @see Timelines
 */
@ApiStatus.NonExtendable
public interface Timeline extends Keyed {
}
