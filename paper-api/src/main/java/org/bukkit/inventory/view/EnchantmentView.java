package org.bukkit.inventory.view;

import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * An instance of {@link InventoryView} which provides extra methods related to
 * enchantment table view data.
 *
 * @since 1.21
 */
public interface EnchantmentView extends InventoryView {

    /**
     * @since 1.21.1
     */
    @NotNull
    @Override
    EnchantingInventory getTopInventory();

    /**
     * Gets the random enchantment seed used in this view
     *
     * @return The random seed used
     */
    int getEnchantmentSeed();

    // Paper start - add enchantment seed update API
    /**
     * Sets the random enchantment seed used in this view. Loses its effect once the view is closed.
     *
     * @param seed the random seed to use
     * @since 1.21.1
     */
    void setEnchantmentSeed(int seed);
    // Paper end - add enchantment seed update API

    /**
     * Gets the offers of this EnchantmentView
     *
     * @return The enchantment offers that are provided
     */
    @NotNull
    EnchantmentOffer[] getOffers();

    /**
     * Sets the offers to provide to the player.
     *
     * @param offers The offers to provide
     * @throws IllegalArgumentException if the array length isn't 3
     */
    void setOffers(@NotNull EnchantmentOffer[] offers) throws IllegalArgumentException;
}
