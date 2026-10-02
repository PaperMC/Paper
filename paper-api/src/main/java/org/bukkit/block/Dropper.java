package org.bukkit.block;

import com.destroystokyo.paper.loottable.LootableBlockInventory;
import org.bukkit.loot.Lootable;

/// Represents a captured state of a dropper.
public interface Dropper extends Container, LootableBlockInventory { // Paper

    /// Tries to drop a randomly selected item from the dropper's inventory,
    /// following the normal behavior of a dropper.
    ///
    /// Normal behavior of a dropper is as follows:
    ///
    /// If the block that the dropper is facing is an InventoryHolder,
    /// the randomly selected ItemStack is placed within that
    /// Inventory in the first slot that's available, starting with 0 and
    /// counting up.  If the inventory is full, nothing happens.
    ///
    /// If the block that the dropper is facing is not an InventoryHolder,
    /// the randomly selected ItemStack is dropped on
    /// the ground in the form of an [`Item`][org.bukkit.entity.Item].
    ///
    /// If the block represented by this state is no longer a dropper, this will
    /// do nothing.
    ///
    /// @throws IllegalStateException if this block state is not placed
    public void drop();
}
