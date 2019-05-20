package org.bukkit.inventory;

import java.util.List;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a merchant. A merchant is a special type of inventory which can
 * facilitate custom trades between items.
 */
public interface Merchant {

    /**
     * Get a list of trades currently available from this merchant.
     *
     * @return an immutable list of trades
     */
    @NotNull
    List<MerchantRecipe> getRecipes();

    /**
     * Set the list of trades currently available from this merchant.
     * <br>
     * This will not change the selected trades of players currently trading
     * with this merchant.
     *
     * @param recipes a list of recipes
     */
    void setRecipes(@NotNull List<MerchantRecipe> recipes);

    /**
     * Get the recipe at a certain index of this merchant's trade list.
     *
     * @param i the index
     * @return the recipe
     * @throws IndexOutOfBoundsException if recipe index out of bounds
     */
    @NotNull
    MerchantRecipe getRecipe(int i) throws IndexOutOfBoundsException;

    /**
     * Set the recipe at a certain index of this merchant's trade list.
     *
     * @param i the index
     * @param recipe the recipe
     * @throws IndexOutOfBoundsException if recipe index out of bounds
     */
    void setRecipe(int i, @NotNull MerchantRecipe recipe) throws IndexOutOfBoundsException;

    /**
     * Get the number of trades this merchant currently has available.
     *
     * @return the recipe count
     */
    int getRecipeCount();

    /**
     * Gets whether this merchant is currently trading.
     *
     * @return whether the merchant is trading
     */
    boolean isTrading();

    /**
     * Gets the player this merchant is trading with, or null if it is not
     * currently trading.
     *
     * @return the trader, or null
     */
    @Nullable
    HumanEntity getTrader();
}
