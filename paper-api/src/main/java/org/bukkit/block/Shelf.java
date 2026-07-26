package org.bukkit.block;

import io.papermc.paper.block.TileStateInventoryHolder;
import org.bukkit.inventory.ShelfInventory;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Shelf extends TileStateInventoryHolder {

    @Override
    ShelfInventory getInventory();

    @Override
    ShelfInventory getSnapshotInventory();
}
