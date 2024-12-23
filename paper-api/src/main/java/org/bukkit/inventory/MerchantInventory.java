package org.bukkit.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a trading inventory between a player and a merchant.
 * <br>
 * The holder of this Inventory is the owning Villager, or null if the player is
 * trading with a merchant created by a plugin.
 *
 * @since 1.3.1
 */
public interface MerchantInventory extends Inventory {

    /**
     * Get the index of the currently selected recipe.
     *
     * @return the index of the currently selected recipe
     * @since 1.9.4
     */
    int getSelectedRecipeIndex();

    /**
     * Get the currently active recipe.
     * <p>
     * This will be <code>null</code> if the items provided by the player do
     * not match the ingredients of the selected recipe. This does not
     * necessarily match the recipe selected by the player: If the player has
     * selected the first recipe, the merchant will search all of its offers
     * for a matching recipe to activate.
     *
     * @return the currently active recipe
     * @since 1.9.4
     */
    @Nullable
    MerchantRecipe getSelectedRecipe();

    /**
     * Gets the Merchant associated with this inventory.
     *
     * @return merchant
     * @since 1.14
     */
    @NotNull
    Merchant getMerchant();
}
