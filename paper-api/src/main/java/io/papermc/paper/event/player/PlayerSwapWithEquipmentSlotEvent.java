package io.papermc.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Triggered when a {@link Player} swaps an item with an equipment slot.
 */
@NullMarked
public class PlayerSwapWithEquipmentSlotEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ItemStack itemInHand;
    private final EquipmentSlot slot;
    private final ItemStack itemToSwap;
    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerSwapWithEquipmentSlotEvent(
        final Player player,
        final ItemStack itemInHand,
        final EquipmentSlot slot,
        final ItemStack itemToSwap
    ) {
        super(player);
        this.itemInHand = itemInHand;
        this.slot = slot;
        this.itemToSwap = itemToSwap;
    }

    /**
     * {@return the item in one of the hand slots}
     */
    public ItemStack getItemInHand() {
        return this.itemInHand.clone();
    }

    /**
     * {@return the slot to swap into}
     */
    public EquipmentSlot getSlot() {
        return this.slot;
    }

    /**
     * {@return the item to swap}
     */
    public ItemStack getItemToSwap() {
        return this.itemToSwap.clone();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
