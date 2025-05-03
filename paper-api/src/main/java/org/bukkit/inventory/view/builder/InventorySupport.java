package org.bukkit.inventory.view.builder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

/**
 * Capability for InventoryViewBuilders to support adding Inventories directly.
 *
 * @param <V> the type of view produced by the builder
 */
public interface InventorySupport<V extends InventoryView> {

    /**
     * Sets the inventory to be used by the inventory supporting builder
     * <p>
     * Note setting an Inventory will clear all other settings besides the title.
     *
     * @param inventory the inventory
     * @return the builder instance
     */
    InventoryViewBuilder<V> inventory(Inventory inventory);
}
