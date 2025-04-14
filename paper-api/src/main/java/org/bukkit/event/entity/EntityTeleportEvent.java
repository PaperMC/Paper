package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Thrown when a non-player entity is teleported from one location to another.
 * <br>
 * This may be as a result of natural causes (Enderman, Shulker), pathfinding
 * (Wolf), or commands (/teleport).
 */
public class EntityTeleportEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Location from;
    private Location to;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityTeleportEvent(@NotNull Entity entity, @NotNull Location from, @Nullable Location to) {
        super(entity);
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the location that this entity moved from
     *
     * @return Location this entity moved from
     */
    @NotNull
    public Location getFrom() {
        return this.from;
    }

    /**
     * Sets the location that this entity moved from
     *
     * @param from New location this entity moved from
     */
    public void setFrom(@NotNull Location from) {
        this.from = from.clone();
    }

    /**
     * Gets the location that this entity moved to
     *
     * @return Location the entity moved to
     */
    @Nullable
    public Location getTo() {
        return this.to;
    }

    /**
     * Sets the location that this entity moved to
     *
     * @param to New Location this entity moved to
     */
    public void setTo(@Nullable Location to) {
        this.to = to != null ? to.clone() : null;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
