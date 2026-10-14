package org.bukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an entity dies and may have the opportunity to be resurrected.
 * Will be called in a cancelled state if the entity does not have a totem
 * equipped.
 */
public class EntityResurrectEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final EquipmentSlot hand;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityResurrectEvent(@NotNull LivingEntity livingEntity, @Nullable EquipmentSlot hand) {
        super(livingEntity);
        this.hand = hand;
    }

    @ApiStatus.Internal
    @Deprecated(since = "1.19.2", forRemoval = true)
    public EntityResurrectEvent(@NotNull LivingEntity livingEntity) {
        this(livingEntity, null);
    }

    @NotNull
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    /**
     * Get the hand in which the totem of undying was found, or {@code null} if the
     * entity did not have a totem of undying.
     *
     * @return the hand, or {@code null}
     */
    @Nullable
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
