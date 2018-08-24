package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Creeper;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a Creeper is ignited either by a
 * flint and steel, {@link Creeper#ignite()} or
 * {@link Creeper#setIgnited(boolean)}.
 */
@NullMarked
public class CreeperIgniteEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean ignited;
    private boolean cancelled;

    @ApiStatus.Internal
    public CreeperIgniteEvent(final Creeper creeper, final boolean ignited) {
        super(creeper);
        this.ignited = ignited;
    }

    @Override
    public Creeper getEntity() {
        return (Creeper) super.getEntity();
    }

    public boolean isIgnited() {
        return this.ignited;
    }

    public void setIgnited(final boolean ignited) {
        this.ignited = ignited;
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
