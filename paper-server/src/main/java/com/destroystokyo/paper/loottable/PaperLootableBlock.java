package com.destroystokyo.paper.loottable;

import net.minecraft.world.RandomizableContainer;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.loot.LootTable;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface PaperLootableBlock extends PaperLootable {

    RandomizableContainer getRandomizableContainer();

    /* Lootable */
    @Override
    default @Nullable LootTable getLootTable() {
        return CraftLootTable.minecraftToBukkit(this.getRandomizableContainer().getLootTable());
    }

    @Override
    default void setLootTable(final @Nullable LootTable table, final long seed) {
        this.getRandomizableContainer().setLootTable(CraftLootTable.bukkitToMinecraft(table), seed);
    }

    @Override
    default long getSeed() {
        return this.getRandomizableContainer().getLootTableSeed();
    }
}
