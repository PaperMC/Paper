package io.papermc.paper.event.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEventNew;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Called when an item is about to be composted by a hopper.
 * To prevent hoppers from moving items into composters, cancel the {@link InventoryMoveItemEvent}.
 */
public interface CompostItemEvent extends BlockEventNew {

    /**
     * Gets the item that was used on the composter.
     *
     * @return the item
     */
    ItemStack getItem();

    /**
     * Gets whether the composter will rise a level.
     *
     * @return {@code true} if successful
     */
    boolean willRaiseLevel();

    /**
     * Sets whether the composter will rise a level.
     *
     * @param willRaiseLevel {@code true} if the composter should rise a level
     */
    void setWillRaiseLevel(boolean willRaiseLevel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
