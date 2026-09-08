package org.bukkit.craftbukkit.event.entity;

import net.minecraft.world.entity.player.Player;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTameEvent;

public class CraftEntityTameEvent extends CraftEntityEvent implements EntityTameEvent {

    private final AnimalTamer owner;
    private boolean cancelled;

    public CraftEntityTameEvent(final LivingEntity entity, final AnimalTamer owner) {
        super(entity);
        this.owner = owner;
    }

    public CraftEntityTameEvent(final net.minecraft.world.entity.LivingEntity entity, final Player owner) {
        this(entity.getBukkitEntity(), owner.getBukkitEntity());
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public AnimalTamer getOwner() {
        return this.owner;
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
        return EntityTameEvent.getHandlerList();
    }
}
