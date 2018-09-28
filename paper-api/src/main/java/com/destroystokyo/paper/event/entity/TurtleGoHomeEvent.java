package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Turtle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when a Turtle decides to go home
 */
@NullMarked
public class TurtleGoHomeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled;

    @ApiStatus.Internal
    public TurtleGoHomeEvent(final Turtle turtle) {
        super(turtle);
    }

    /**
     * The turtle going home
     *
     * @return The turtle
     */
    @Override
    public Turtle getEntity() {
        return (Turtle) super.getEntity();
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
