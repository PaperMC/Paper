package org.bukkit.inventory;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/// Container class containing the results of a Crafting event.
///
/// This class makes no guarantees about the nature or mutability of the returned
/// values.
public interface ItemCraftResult {

    /// The resulting [ItemStack] that was crafted.
    ///
    /// @return [ItemStack] that was crafted.
    @NotNull
    public ItemStack getResult();

    /// Gets the resulting matrix from the crafting operation.
    ///
    /// @return resulting matrix
    public @NotNull ItemStack @NotNull [] getResultingMatrix();

    /// Gets the overflowed items for items that don't fit back into the crafting
    /// matrix.
    ///
    /// @return overflow items
    @NotNull
    public List<ItemStack> getOverflowItems();
}
