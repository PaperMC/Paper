package org.bukkit.inventory;

import org.bukkit.block.Lectern;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a Lectern.
 */
public interface LecternInventory extends Inventory {

    @Nullable
    @Override
    public Lectern getHolder();
}
