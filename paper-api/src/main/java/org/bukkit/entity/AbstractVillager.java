package org.bukkit.entity;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Merchant;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a villager NPC
 *
 * @since 1.14
 */
public interface AbstractVillager extends Breedable, NPC, InventoryHolder, Merchant {

    /**
     * Gets this villager's inventory.
     * <br>
     * Note that this inventory is not the Merchant inventory, rather, it is the
     * items that a villager might have collected (from harvesting crops, etc.)
     *
     * {@inheritDoc}
     */
    @NotNull
    @Override
    Inventory getInventory();

    // Paper start
    /**
     * Reset this villager's trade offers
     *
     * @since 1.16.4
     */
    public void resetOffers();
    // Paper end
}
