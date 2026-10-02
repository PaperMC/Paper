package org.bukkit.loot;

import org.jetbrains.annotations.Nullable;

/// Represents a [org.bukkit.block.Container] or a
/// [org.bukkit.entity.Mob] that can have a loot table.
///
/// Container loot will only generate upon opening, and only when the container
/// is _first_ opened.
///
/// Entities will only generate loot upon death.
public interface Lootable {

    /// Set the loot table for a container or entity.
    ///
    /// If the provided loot table is null, the loot table will be reset to its default behavior.
    ///
    /// @param table the Loot Table this [org.bukkit.block.Container] or
    /// [org.bukkit.entity.Mob] will have.
    void setLootTable(@Nullable LootTable table);

    /// Gets the Loot Table attached to this block or entity.
    ///
    /// If a block/entity does not have a loot table, this will return null, NOT
    /// an empty loot table.
    ///
    /// @return the Loot Table attached to this block or entity.
    @Nullable
    LootTable getLootTable();

    // Paper start
    /// Set the loot table and seed for a container or entity at the same time.
    ///
    /// If the provided loot table is null, the loot table will be reset to its default behavior.
    ///
    /// @param table the Loot Table this [org.bukkit.block.Container] or [org.bukkit.entity.Mob] will have.
    /// @param seed the seed to used to generate loot. Default is 0.
    void setLootTable(final @Nullable LootTable table, final long seed);

    /// Returns whether or not this object has a Loot Table
    /// @return Has a loot table
    default boolean hasLootTable() {
        return this.getLootTable() != null;
    }

    /// Clears the associated Loot Table to this object, essentially resetting it to default
    /// @see #setLootTable(LootTable)
    default void clearLootTable() {
        this.setLootTable(null);
    }
    // Paper end

    /// Set the seed used when this Loot Table generates loot.
    ///
    /// @param seed the seed to used to generate loot. Default is 0.
    void setSeed(long seed);

    /// Get the Loot Table's seed.
    ///
    /// The seed is used when generating loot.
    ///
    /// @return the seed
    long getSeed();
}
