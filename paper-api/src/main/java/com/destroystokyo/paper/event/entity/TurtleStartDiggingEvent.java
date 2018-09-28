package com.destroystokyo.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Turtle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when a Turtle starts digging to lay eggs
 */
@NullMarked
public class TurtleStartDiggingEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location location;
    private boolean cancelled;

    @ApiStatus.Internal
    public TurtleStartDiggingEvent(final Turtle turtle, final Location location) {
        super(turtle);
        this.location = location;
    }

    /**
     * The turtle digging
     *
     * @return The turtle
     */
    @Override
    public Turtle getEntity() {
        return (Turtle) super.getEntity();
    }

    /**
     * Get the location where the turtle is digging
     *
     * @return Location where digging
     */
    public Location getLocation() {
        return this.location.clone();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
