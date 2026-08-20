package org.bukkit.event.block;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a player stops damaging a block.
 *
 * @see BlockDamageEvent
 */
public interface BlockDamageAbortEvent extends BlockEvent {

    /**
     * Gets the player that stopped damaging the block involved in this event.
     *
     * @return The player that stopped damaging the block
     */
    Player getPlayer();

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
