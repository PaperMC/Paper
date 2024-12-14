package org.bukkit.inventory;

import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a Brewing Stand.
 *
 * @since 1.1.0 R5
 */
public interface BrewerInventory extends Inventory {

    /**
     * Get the current ingredient for brewing.
     *
     * @return The ingredient.
     */
    @Nullable
    ItemStack getIngredient();

    /**
     * Set the current ingredient for brewing.
     *
     * @param ingredient The ingredient
     */
    void setIngredient(@Nullable ItemStack ingredient);

    /**
     * Get the current fuel for brewing.
     *
     * @return The fuel
     * @since 1.9.4
     */
    @Nullable
    ItemStack getFuel();

    /**
     * Set the current fuel for brewing. Generally only
     * {@link Material#BLAZE_POWDER} will be of use.
     *
     * @param fuel The fuel
     * @since 1.9.4
     */
    void setFuel(@Nullable ItemStack fuel);

    @Override
    @Nullable
    BrewingStand getHolder();
}
