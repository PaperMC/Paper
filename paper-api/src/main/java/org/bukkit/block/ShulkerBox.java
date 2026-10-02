package org.bukkit.block;

import com.destroystokyo.paper.loottable.LootableBlockInventory;
import org.bukkit.DyeColor;
import org.bukkit.loot.Lootable;
import org.jetbrains.annotations.Nullable;

/// Represents a captured state of a ShulkerBox.
public interface ShulkerBox extends Container, LootableBlockInventory, Lidded { // Paper

    /// Get the [DyeColor] corresponding to this ShulkerBox
    ///
    /// @return the [DyeColor] of this ShulkerBox, or null if default
    @Nullable
    public DyeColor getColor();
}
