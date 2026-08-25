package org.bukkit.event.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a player stops damaging a block.
 *
 * @see BlockDamageEvent
 */
public interface BlockDamageAbortEvent extends BlockEvent, PlayerEvent {

    /**
     * Gets the item currently in the player's hand.
     *
     * @return The item currently in the player's hand
     */
    ItemStack getItemInHand();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
