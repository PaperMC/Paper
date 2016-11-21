package org.bukkit.inventory;

/**
 * Represents a trading inventory between a player and a merchant.
 * <br>
 * The holder of this Inventory is the owning Villager, or null if the player is
 * trading with a merchant created by a plugin.
 */
public interface MerchantInventory extends Inventory {

    /**
     * Get the index of the currently selected recipe.
     *
     * @return the index of the currently selected recipe
     */
    int getSelectedRecipeIndex();

    /**
     * Get the currently selected recipe.
     *
     * @return the currently selected recipe
     */
    MerchantRecipe getSelectedRecipe();
}
