package org.bukkit.inventory;

import org.bukkit.MinecraftExperimental;
import org.bukkit.block.ChiseledBookshelf;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a chiseled bookshelf.
 */
@MinecraftExperimental
@ApiStatus.Experimental
public interface ChiseledBookshelfInventory extends Inventory {

    @Nullable
    @Override
    public ChiseledBookshelf getHolder();
}
