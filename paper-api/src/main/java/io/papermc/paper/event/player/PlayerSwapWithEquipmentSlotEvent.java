package io.papermc.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Triggered when a {@link Player} swaps an item with an equipment slot.
 */
public interface PlayerSwapWithEquipmentSlotEvent extends PlayerEvent, Cancellable {

    /**
     * {@return the item in one of the hand slots}
     */
    ItemStack getItemInHand();

    /**
     * {@return the slot to swap into}
     */
    EquipmentSlot getSlot();

    /**
     * {@return the item to swap}
     */
    ItemStack getItemToSwap();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
