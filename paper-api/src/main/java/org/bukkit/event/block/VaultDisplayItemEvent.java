package org.bukkit.event.block;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Called when a vault in a trial chamber is about to display an item.
 */
public interface VaultDisplayItemEvent extends BlockEventNew, Cancellable {

    /**
     * Gets the item that will be displayed inside the vault.
     *
     * @return the item to be displayed
     */
    @Nullable ItemStack getDisplayItem();

    /**
     * Sets the item that will be displayed inside the vault.
     *
     * @param displayItem the item to be displayed
     */
    void setDisplayItem(@Nullable ItemStack displayItem);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
