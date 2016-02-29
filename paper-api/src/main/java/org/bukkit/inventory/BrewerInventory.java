package org.bukkit.inventory;

import org.bukkit.Material;
import org.bukkit.block.BrewingStand;

/**
 * Interface to the inventory of a Brewing Stand.
 */
public interface BrewerInventory extends Inventory {

    /**
     * Get the current ingredient for brewing.
     *
     * @return The ingredient.
     */
    ItemStack getIngredient();

    /**
     * Set the current ingredient for brewing.
     *
     * @param ingredient The ingredient
     */
    void setIngredient(ItemStack ingredient);

    /**
     * Get the current fuel for brewing.
     *
     * @return The fuel
     */
    ItemStack getFuel();

    /**
     * Set the current fuel for brewing. Generally only
     * {@link Material#BLAZE_POWDER} will be of use.
     *
     * @param fuel The fuel
     */
    void setFuel(ItemStack fuel);

    BrewingStand getHolder();
}
