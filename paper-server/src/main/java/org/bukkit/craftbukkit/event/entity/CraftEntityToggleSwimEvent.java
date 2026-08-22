package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityToggleSwimEvent;

public class CraftEntityToggleSwimEvent extends CraftEntityEvent implements EntityToggleSwimEvent {

    private final boolean isSwimming;
    private boolean cancelled;

    public CraftEntityToggleSwimEvent(final LivingEntity livingEntity, final boolean isSwimming) {
        super(livingEntity);
        this.isSwimming = isSwimming;
    }

    @Override
    public boolean isSwimming() {
        return this.isSwimming;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Deprecated
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityToggleSwimEvent.getHandlerList();
    }
}
