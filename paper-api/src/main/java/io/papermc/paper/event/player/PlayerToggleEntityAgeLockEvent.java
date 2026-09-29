package io.papermc.paper.event.player;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player toggles the age lock of an entity using an item.
 */
@NullMarked
public class PlayerToggleEntityAgeLockEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LivingEntity entity;
    private final ItemStack item;
    private final EquipmentSlot hand;
    private final boolean ageLocked;
    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerToggleEntityAgeLockEvent(final Player player, final LivingEntity entity, final ItemStack item, final EquipmentSlot hand, final boolean ageLocked) {
        super(player);
        this.entity = entity;
        this.item = item;
        this.hand = hand;
        this.ageLocked = ageLocked;
    }

    /**
     * {@return the entity that is having its age locked or unlocked}
     */
    public LivingEntity getEntity() {
        return this.entity;
    }

    /**
     * {@return the item being used to toggle the age lock of the entity}
     */
    public ItemStack getItem() {
        return this.item.clone();
    }

    /**
     * {@return the hand being used to toggle the age lock of the entity}
     */
    public EquipmentSlot getHand() {
        return this.hand;
    }

    /**
     * {@return whether the age of the entity is going to be locked or not}
     */
    public boolean isAgeLocked() {
        return this.ageLocked;
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
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
