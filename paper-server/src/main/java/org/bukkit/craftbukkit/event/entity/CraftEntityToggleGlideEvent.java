package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class CraftEntityToggleGlideEvent extends CraftEntityEvent implements EntityToggleGlideEvent {

    private final boolean isGliding;
    private boolean cancelled;

    public CraftEntityToggleGlideEvent(final LivingEntity livingEntity, final boolean isGliding) {
        super(livingEntity);
        this.isGliding = isGliding;
    }

    @Override
    public boolean isGliding() {
        return this.isGliding;
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
        return EntityToggleGlideEvent.getHandlerList();
    }
}
