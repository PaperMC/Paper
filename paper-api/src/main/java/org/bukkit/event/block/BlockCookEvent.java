package org.bukkit.event.block;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Called when an item is successfully cooked in a block.
 */
public interface BlockCookEvent extends BlockEventNew, Cancellable {

    /**
     * Gets the smelted item for this event
     *
     * @return smelting source item
     */
    ItemStack getSource();

    /**
     * Gets the resultant item for this event
     *
     * @return smelting result item
     */
    ItemStack getResult();

    /**
     * Sets the resultant item for this event
     *
     * @param result new result item
     */
    void setResult(ItemStack result);

    /**
     * Gets the cooking recipe associated with this event.
     *
     * @return the recipe
     */
    @Nullable CookingRecipe<?> getRecipe();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
