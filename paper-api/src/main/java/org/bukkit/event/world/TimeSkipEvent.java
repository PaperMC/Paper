package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the time skips in a world.
 * <p>
 * If the event is cancelled the time will not change.
 */
public class TimeSkipEvent extends ClockTimeSkipEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final World world;

    @ApiStatus.Internal
    public TimeSkipEvent(@NotNull World world, @NotNull SkipReason skipReason, long skipAmount) {
        super(skipReason, skipAmount);
        this.world = world;
    }

    /**
     * Returns the world that time is skipped in.
     *
     * @return world that time is skipped in
     */
    @NotNull
    public World getWorld() {
        return world;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
