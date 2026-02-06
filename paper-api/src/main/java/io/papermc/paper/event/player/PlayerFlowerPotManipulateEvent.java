package io.papermc.paper.event.player;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a player places an item in or takes an item out of a flowerpot.
 */
public interface PlayerFlowerPotManipulateEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the flowerpot that is involved in this event.
     *
     * @return the flowerpot that is involved with this event
     */
    Block getFlowerpot();

    /**
     * Gets the item being placed, or taken from, the flower pot.
     * Check if placing with {@link #isPlacing()}.
     *
     * @return the item placed, or taken from, the flowerpot
     */
    ItemStack getItem();

    /**
     * Gets if the item is being placed into the flowerpot.
     *
     * @return if the item is being placed into the flowerpot
     */
    boolean isPlacing();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
