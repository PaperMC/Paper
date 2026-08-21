package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityCombustEvent;

public class CraftEntityCombustEvent extends CraftEntityEvent implements EntityCombustEvent {

    private float duration;
    private boolean cancelled;

    public CraftEntityCombustEvent(final Entity combustee, final float duration) {
        super(combustee);
        this.duration = duration;
    }

    @Override
    public float getDuration() {
        return this.duration;
    }

    @Override
    public void setDuration(final float duration) {
        this.duration = duration;
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
        return EntityCombustEvent.getHandlerList();
    }
}
