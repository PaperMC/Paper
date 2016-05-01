package com.destroystokyo.paper.loottable;

import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;

/**
 * Represents an Inventory that can generate loot, such as Chests inside of Fortresses and Mineshafts
 */
@NullMarked
public interface LootableBlockInventory extends LootableInventory {

    /**
     * Gets the block that is lootable
     * @return The Block
     */
    Block getBlock();
}
