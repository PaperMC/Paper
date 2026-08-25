package org.bukkit.event.inventory;

import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Recipe;

/**
 * Called when the recipe of an Item is completed inside a crafting matrix.
 */
public interface CraftItemEvent extends InventoryClickEvent {

    @Override
    CraftingInventory getInventory();

    /**
     * @return A copy of the current recipe on the crafting matrix.
     */
    Recipe getRecipe();
}
