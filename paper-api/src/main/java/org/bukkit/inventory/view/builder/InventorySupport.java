package org.bukkit.inventory.view.builder;

import org.bukkit.Server;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;


/**
 * Adds inventory support to a {@link InventoryViewBuilder}.
 */
public interface InventorySupport {

    /**
     * Applies an inventory to the inventory supported builder
     * <p>
     * Note that setting the inventory disabled most other applied fields besides the title. Setting an inventory might
     * also break expected container functionality.
     * <p>
     * The inventory provided to this method must not be created via
     * {@link Server#createInventory(InventoryHolder, int)}. Instead use [PLACEHOLDER]
     *
     * @param inventory the inventory
     * @return this builder that supports Inventory support
     */
    InventoryViewBuilder<InventoryView> inventory(Inventory inventory);
}
