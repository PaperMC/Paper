package org.bukkit.event.world;

import org.bukkit.GameEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jspecify.annotations.Nullable;

/**
 * Represents a generic Mojang game event.
 * <br>
 * Specific Bukkit events should be used where possible, this event is mainly
 * used internally by Sculk sensors.
 */
public interface GenericGameEvent extends WorldEventNew, Cancellable {

    /**
     * Get the underlying event.
     *
     * @return the event
     */
    GameEvent getEvent();

    /**
     * Get the location where the event occurred.
     *
     * @return event location
     */
    Location getLocation();

    /**
     * Get the entity which triggered this event, if present.
     *
     * @return triggering entity or {@code null}
     */
    @Nullable Entity getEntity();

    /**
     * Get the block radius to which this event will be broadcast.
     *
     * @return broadcast radius
     */
    int getRadius();

    /**
     * Set the radius to which the event should be broadcast.
     *
     * @param radius radius, must be greater than or equal to 0
     */
    void setRadius(@NonNegative int radius);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
