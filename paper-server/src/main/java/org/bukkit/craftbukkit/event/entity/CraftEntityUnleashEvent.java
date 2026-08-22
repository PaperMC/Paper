package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityUnleashEvent;

public class CraftEntityUnleashEvent extends CraftEntityEvent implements EntityUnleashEvent {

    private final UnleashReason reason;
    private boolean dropLeash;

    private boolean cancelled;

    public CraftEntityUnleashEvent(final Entity entity, final UnleashReason reason, final boolean dropLeash) {
        super(entity);
        this.reason = reason;
        this.dropLeash = dropLeash;
    }

    @Override
    public UnleashReason getReason() {
        return this.reason;
    }

    @Override
    public boolean isDropLeash() {
        return this.dropLeash;
    }

    @Override
    public void setDropLeash(final boolean dropLeash) {
        this.dropLeash = dropLeash;
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
        return EntityUnleashEvent.getHandlerList();
    }
}
