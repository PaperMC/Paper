package com.destroystokyo.paper.loottable;

import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

/**
 * Represents an Inventory that can generate loot, such as Minecarts inside of Mineshafts
 *
 * @since 1.9.4
 */
@NullMarked
public interface LootableEntityInventory extends LootableInventory {

    /**
     * Gets the entity that is lootable
     * @return The Entity
     */
    Entity getEntity();
}
