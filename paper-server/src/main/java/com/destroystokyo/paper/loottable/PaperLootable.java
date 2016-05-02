package com.destroystokyo.paper.loottable;

import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public interface PaperLootable extends Lootable {

    @Override
    default void setLootTable(final @Nullable LootTable table) {
        this.setLootTable(table, this.getSeed());
    }

    @Override
    default void setSeed(final long seed) {
        this.setLootTable(this.getLootTable(), seed);
    }
}
