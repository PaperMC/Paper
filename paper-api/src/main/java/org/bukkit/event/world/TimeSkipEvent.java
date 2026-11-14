package org.bukkit.event.world;

import io.papermc.paper.world.WorldClock;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the time skips in a world.
 * <p>
 * If the event is cancelled the time will not change.
 *
 * @see ClockTimeSkipEvent for changing of clocks that affect all worlds
 */
@NullMarked
public class TimeSkipEvent extends ClockTimeSkipEvent {

    private final World world;

    @ApiStatus.Internal
    public TimeSkipEvent(final World world, final WorldClock clock, final SkipReason skipReason, final long skipAmount) {
        super(clock, skipReason, skipAmount);
        this.world = world;
    }

    /**
     * Returns the world that time is skipped in.
     *
     * @return world that time is skipped in
     */
    public World getWorld() {
        return this.world;
    }
}
