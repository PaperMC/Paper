package org.bukkit.block;

import org.bukkit.Nameable;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured state of a container block.
 */
public interface Container extends io.papermc.paper.block.LockableTileState, io.papermc.paper.block.TileStateInventoryHolder { // Paper

    // Paper - moved to TileStateInventoryHolder
}
