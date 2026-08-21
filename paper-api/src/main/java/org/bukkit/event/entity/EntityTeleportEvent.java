package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Thrown when a non-player entity is teleported from one location to another.
 * <p>
 * This may be as a result of natural causes (Enderman, Shulker), pathfinding
 * (Wolf), or commands (/teleport).
 */
public interface EntityTeleportEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the location that this entity moved from
     *
     * @return Location this entity moved from
     */
    Location getFrom();

    /**
     * Sets the location that this entity moved from
     *
     * @param from New location this entity moved from
     */
    void setFrom(Location from);

    /**
     * Gets the location that this entity moved to
     *
     * @return Location the entity moved to
     */
    @Nullable Location getTo();

    /**
     * Sets the location that this entity moved to
     *
     * @param to New Location this entity moved to
     */
    void setTo(@Nullable Location to);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
