package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.Nullable;

public class CraftEntityResurrectEvent extends CraftEntityEvent implements EntityResurrectEvent {

    private final EquipmentSlot hand;
    private boolean cancelled;

    public CraftEntityResurrectEvent(final LivingEntity livingEntity, final @Nullable EquipmentSlot hand) {
        super(livingEntity);
        this.hand = hand;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public @Nullable EquipmentSlot getHand() {
        return this.hand;
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
        return EntityResurrectEvent.getHandlerList();
    }
}
