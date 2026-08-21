package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDismountEvent;

public class CraftEntityDismountEvent extends CraftEntityEvent implements EntityDismountEvent {

    private final Entity dismounted;
    private final boolean isCancellable;

    private boolean cancelled;

    public CraftEntityDismountEvent(final Entity entity, final Entity dismounted, final boolean isCancellable) {
        super(entity);
        this.dismounted = dismounted;
        this.isCancellable = isCancellable;
    }

    @Override
    public Entity getDismounted() {
        return this.dismounted;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        if (cancel && !this.isCancellable) {
            return;
        }
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancellable() {
        return this.isCancellable;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityDismountEvent.getHandlerList();
    }
}
