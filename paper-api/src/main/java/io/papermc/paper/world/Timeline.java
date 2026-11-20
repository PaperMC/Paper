package io.papermc.paper.world;

import java.util.OptionalInt;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a timeline that can apply environment attributes or define time markers for a {@link WorldClock}.
 *
 * @see Timelines
 */
@ApiStatus.NonExtendable
public interface Timeline extends Keyed {

    /**
     * Gets the clock that drives this timeline.
     *
     * @return the clock
     */
    WorldClock clock();

    /**
     * Gets the period of this timeline in ticks, if defined.
     *
     * @return the period in ticks, or an empty {@link OptionalInt} if not defined
     */
    OptionalInt periodTicks();
}
