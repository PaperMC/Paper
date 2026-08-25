package org.bukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Recipe;
import org.jspecify.annotations.Nullable;

public interface PrepareItemCraftEvent extends InventoryEvent {

    /**
     * Get the recipe that has been formed. If this event was triggered by a
     * tool repair, this will be a temporary shapeless recipe representing the
     * repair.
     *
     * @return The recipe being crafted.
     */
    @Nullable Recipe getRecipe();

    /**
     * @return The crafting inventory on which the recipe was formed.
     */
    @Override
    CraftingInventory getInventory();

    /**
     * Check if this event was triggered by a tool repair operation rather
     * than a crafting recipe.
     *
     * @return {@code true} if this is a repair.
     */
    boolean isRepair();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
