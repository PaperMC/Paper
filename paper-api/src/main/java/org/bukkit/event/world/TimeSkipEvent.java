package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;

/**
 * Called when the time skips in a world.
 * <p>
 * If the event is cancelled the time will not change.
 *
 * @see ClockTimeSkipEvent for changing of clocks that affect all worlds
 */
public interface TimeSkipEvent extends ClockTimeSkipEvent {

    /**
     * Returns the world that time is skipped in.
     *
     * @return world that time is skipped in
     */
    World getWorld();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
