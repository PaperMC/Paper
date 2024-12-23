package org.bukkit.entity.minecart;

import com.destroystokyo.paper.loottable.LootableEntityInventory;
import org.bukkit.entity.Minecart;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.loot.Lootable;

/**
 * Represents a minecart with a chest. These types of {@link Minecart
 * minecarts} have their own inventory that can be accessed using methods
 * from the {@link InventoryHolder} interface.
 *
 * @since 1.5.1
 */
public interface StorageMinecart extends Minecart, InventoryHolder, LootableEntityInventory { // Paper
}
