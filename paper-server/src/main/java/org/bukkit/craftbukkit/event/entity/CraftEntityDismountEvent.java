package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDismountEvent;

public class CraftEntityDismountEvent extends CraftEntityEvent implements EntityDismountEvent {

    private final Entity dismounted;
    private final boolean cancellable;

    private boolean cancelled;

    public CraftEntityDismountEvent(final Entity entity, final Entity dismounted, final boolean cancellable) {
        super(entity);
        this.dismounted = dismounted;
        this.cancellable = cancellable;
    }

    public CraftEntityDismountEvent(final net.minecraft.world.entity.Entity entity, final net.minecraft.world.entity.Entity dismounted, final boolean cancellable) {
        this(entity.getBukkitEntity(), dismounted.getBukkitEntity(), cancellable);
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
        if (cancel && !this.cancellable) {
            return;
        }
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancellable() {
        return this.cancellable;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityDismountEvent.getHandlerList();
    }
}
