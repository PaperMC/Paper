package org.bukkit.inventory;

import org.bukkit.block.Shelf;
import org.jetbrains.annotations.Nullable;

public interface ShelfInventory extends Inventory {

    @Nullable
    @Override
    public Shelf getHolder();
}
