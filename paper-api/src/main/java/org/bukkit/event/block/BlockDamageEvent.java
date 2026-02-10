package org.bukkit.event.block;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a block is damaged by a player.
 * <p>
 * If this event is cancelled, the block will not be damaged.
 *
 * @see BlockDamageAbortEvent
 */
public interface BlockDamageEvent extends BlockEvent, Cancellable {

    /**
     * Gets the player damaging the block involved in this event.
     *
     * @return The player damaging the block involved in this event
     */
    Player getPlayer();

    /**
     * Gets the item currently in the player's hand.
     *
     * @return The item currently in the player's hand
     */
    ItemStack getItemInHand();

    /**
     * Gets the  block face the player is interacting with.
     *
     * @return The block face clicked to damage the block
     */
    BlockFace getBlockFace();

    /**
     * Gets if the block is set to instantly break when damaged by the player.
     *
     * @return {@code true} if the block should instantly break when damaged by the
     *     player
     */
    boolean getInstaBreak();

    /**
     * Sets if the block should instantly break when damaged by the player.
     *
     * @param instaBreak {@code true} if you want the block to instantly break when damaged
     *     by the player
     */
    void setInstaBreak(boolean instaBreak);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
