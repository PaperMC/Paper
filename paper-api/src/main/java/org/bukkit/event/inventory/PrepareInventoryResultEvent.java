package org.bukkit.event.inventory;

import org.bukkit.Warning;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Called when an item is put in a slot and the result is calculated.
 *
 * @deprecated use {@link com.destroystokyo.paper.event.inventory.PrepareResultEvent}
 */
@Deprecated @Warning
public interface PrepareInventoryResultEvent extends InventoryEventNew {

    /**
     * Get result item, may be {@code null}.
     *
     * @return result item
     */
    @Nullable ItemStack getResult();

    /**
     * Set result item, may be {@code null}.
     *
     * @param result result item
     */
    void setResult(@Nullable ItemStack result);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
