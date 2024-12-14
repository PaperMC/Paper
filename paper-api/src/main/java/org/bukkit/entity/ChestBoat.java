package org.bukkit.entity;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.loot.Lootable;

/**
 * A {@link Boat} with a chest.
 *
 * @since 1.19
 */
public interface ChestBoat extends Boat, InventoryHolder, com.destroystokyo.paper.loottable.LootableEntityInventory { // Paper
}
