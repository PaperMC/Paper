package org.bukkit.inventory;

/**
 * Represents some type of crafting recipe.
 */
public interface Recipe {

    /**
     * Get the result of this recipe.
     *
     * @return The result stack
     */
    ItemStack getResult();
}
