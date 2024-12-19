package org.bukkit.inventory;

import org.bukkit.block.ChiseledBookshelf;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a chiseled bookshelf.
 *
 * @since 1.19.3
 */
public interface ChiseledBookshelfInventory extends Inventory {

    @Nullable
    @Override
    public ChiseledBookshelf getHolder();
}
