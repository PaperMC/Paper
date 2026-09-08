package org.bukkit.craftbukkit.event.entity;

import net.minecraft.Optionull;
import net.minecraft.world.InteractionHand;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.Nullable;

public class CraftEntityResurrectEvent extends CraftEntityEvent implements EntityResurrectEvent {

    private final @Nullable EquipmentSlot hand;
    private boolean cancelled;

    public CraftEntityResurrectEvent(final LivingEntity livingEntity, final @Nullable EquipmentSlot hand) {
        super(livingEntity);
        this.hand = hand;
    }

    public CraftEntityResurrectEvent(final net.minecraft.world.entity.LivingEntity livingEntity, final @Nullable InteractionHand hand) {
        this(livingEntity.getBukkitEntity(), Optionull.map(hand, CraftEquipmentSlot::getHand));
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
