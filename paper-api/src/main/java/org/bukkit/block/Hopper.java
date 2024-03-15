package org.bukkit.block;

import com.destroystokyo.paper.loottable.LootableBlockInventory;
import org.bukkit.loot.Lootable;

/**
 * Represents a captured state of a hopper.
 */
public interface Hopper extends Container, LootableBlockInventory { // Paper
    // Paper start - Expanded Hopper API
    /**
     * Sets the cooldown before the hopper transfers or sucks in another item
     * @param cooldown the cooldown in ticks
     * @throws IllegalArgumentException if the passed cooldown value is negative.
     */
    void setTransferCooldown(@org.jetbrains.annotations.Range(from = 0, to = Integer.MAX_VALUE) int cooldown);

    /**
     * Returns the cooldown before the hopper transfers or sucks in another item
     * @return the cooldown in ticks
     */
    int getTransferCooldown();
    // Paper end - Expanded Hopper API
}

