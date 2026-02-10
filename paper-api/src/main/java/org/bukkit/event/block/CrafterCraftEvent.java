package org.bukkit.event.block;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;

/**
 * Event called when a Crafter is about to craft an item.
 */
public interface CrafterCraftEvent extends BlockEvent, Cancellable {

    /**
     * Gets the result for the craft.
     *
     * @return the result for the craft
     */
    ItemStack getResult();

    /**
     * Sets the result of the craft.
     *
     * @param result the result of the craft
     */
    void setResult(ItemStack result);

    /**
     * Gets the recipe that was used to craft this item.
     *
     * @return the recipe that was used to craft this item
     */
    CraftingRecipe getRecipe();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
