package org.bukkit.entity;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Merchant;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a villager NPC
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
     * Reset this villager's trade offers.
     * <br>
     * For {@link org.bukkit.entity.Villager Villagers}, only two trades are
     * created, rather than the number of trades expected for the villager's
     * level. You should use {@link org.bukkit.entity.Villager#addTrades(int)}
     * to add the remaining trades.
     */
    public void resetOffers();
    // Paper end
}
