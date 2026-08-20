package org.bukkit.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Called when a player swap items between main hand and off-hand using the
 * hotkey.
 */
public interface PlayerSwapHandItemsEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the item switched to the main hand.
     *
     * @return item in the main hand
     */
    ItemStack getMainHandItem();

    /**
     * Sets the item in the main hand.
     *
     * @param mainHandItem new item in the main hand
     */
    void setMainHandItem(@Nullable ItemStack mainHandItem);

    /**
     * Gets the item switched to the off-hand.
     *
     * @return item in the off-hand
     */
    ItemStack getOffHandItem();

    /**
     * Sets the item in the off-hand.
     *
     * @param offHandItem new item in the off-hand
     */
    void setOffHandItem(@Nullable ItemStack offHandItem);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
