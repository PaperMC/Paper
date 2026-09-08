package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class CraftEntityToggleGlideEvent extends CraftEntityEvent implements EntityToggleGlideEvent {

    private final boolean gliding;
    private boolean cancelled;

    public CraftEntityToggleGlideEvent(final LivingEntity livingEntity, final boolean gliding) {
        super(livingEntity);
        this.gliding = gliding;
    }

    public CraftEntityToggleGlideEvent(final net.minecraft.world.entity.LivingEntity livingEntity, final boolean gliding) {
        this(livingEntity.getBukkitEntity(), gliding);
    }

    @Override
    public boolean isGliding() {
        return this.gliding;
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
