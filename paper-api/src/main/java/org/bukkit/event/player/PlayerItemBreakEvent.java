package org.bukkit.event.player;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Fired when a player's item breaks (such as a shovel or flint and steel).
 * <p>
 * After this event, the item's amount will be set to {@code item amount - 1}
 * and its durability will be reset to 0.
 */
public interface PlayerItemBreakEvent extends PlayerEvent {

    /**
     * Gets the item that broke
     *
     * @return The broken item
     */
    ItemStack getBrokenItem();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
