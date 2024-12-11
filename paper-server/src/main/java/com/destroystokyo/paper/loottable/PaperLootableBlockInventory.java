package com.destroystokyo.paper.loottable;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public interface PaperLootableBlockInventory extends LootableBlockInventory, PaperLootableInventory, PaperLootableBlock {

    /* PaperLootableInventory */
    @Override
    default PaperLootableInventoryData lootableDataForAPI() {
        return Objects.requireNonNull(this.getRandomizableContainer().lootableData(), "Can only manage loot tables on tile entities with lootableData");
    }

    /* LootableBlockInventory */
    @Override
    default Block getBlock() {
        final BlockPos position = this.getRandomizableContainer().getBlockPos();
        return CraftBlock.at(this.getNMSWorld(), position);
    }

}
