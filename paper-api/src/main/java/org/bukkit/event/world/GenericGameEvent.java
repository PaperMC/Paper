package org.bukkit.event.world;

import com.google.common.base.Preconditions;
import org.bukkit.GameEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a generic Mojang game event.
 *
 * Specific Bukkit events should be used where possible, this event is mainly
 * used internally by Sculk sensors.
 */
public class GenericGameEvent extends WorldEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final GameEvent event;
    private final Location location;
    private final Entity entity;
    private int radius;
    private boolean cancelled;

    public GenericGameEvent(@NotNull GameEvent event, @NotNull Location location, @Nullable Entity entity, int radius, boolean isAsync) {
        super(location.getWorld(), isAsync);
        this.event = event;
        this.location = location;
        this.entity = entity;
        this.radius = radius;
    }

    /**
     * Get the underlying event.
     *
     * @return the event
     */
    @NotNull
    public GameEvent getEvent() {
        return event;
    }

    /**
     * Get the location where the event occurred.
     *
     * @return event location
     */
    @NotNull
    public Location getLocation() {
        return location;
    }

    /**
     * Get the entity which triggered this event, if present.
     *
     * @return triggering entity or null
     */
    @Nullable
    public Entity getEntity() {
        return entity;
    }

    /**
     * Get the block radius to which this event will be broadcast.
     *
     * @return broadcast radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Set the radius to which the event should be broadcast.
     *
     * @param radius radius, must be greater than or equal to 0
     */
    public void setRadius(int radius) {
        Preconditions.checkArgument(radius >= 0, "Radius must be >= 0");
        this.radius = radius;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
