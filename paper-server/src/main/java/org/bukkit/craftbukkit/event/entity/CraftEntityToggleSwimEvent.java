package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityToggleSwimEvent;

public class CraftEntityToggleSwimEvent extends CraftEntityEvent implements EntityToggleSwimEvent {

    private final boolean swimming;
    private boolean cancelled;

    public CraftEntityToggleSwimEvent(final LivingEntity livingEntity, final boolean swimming) {
        super(livingEntity);
        this.swimming = swimming;
    }

    public CraftEntityToggleSwimEvent(final net.minecraft.world.entity.LivingEntity livingEntity, final boolean swimming) {
        this(livingEntity.getBukkitEntity(), swimming);
    }

    @Override
    public boolean isSwimming() {
        return this.swimming;
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
