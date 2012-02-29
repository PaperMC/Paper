package org.bukkit.inventory;

import org.bukkit.block.BrewingStand;

public interface BrewerInventory extends Inventory {
    /**
     * Get the current ingredient for brewing.
     * @return The ingredient.
     */
    ItemStack getIngredient();
    /**
     * Set the current ingredient for brewing.
     * @param ingredient The ingredient
     */
    void setIngredient(ItemStack ingredient);

    BrewingStand getHolder();
}
