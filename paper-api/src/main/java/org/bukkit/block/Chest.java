package org.bukkit.block;

import com.destroystokyo.paper.loottable.LootableBlockInventory; // Paper
import org.bukkit.Nameable; // Paper
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.Lootable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured state of a chest.
 */
public interface Chest extends Container, LootableBlockInventory, Lidded { // Paper

    /**
     * Gets the inventory of the chest block represented by this block state.
     * <p>
     * If the chest is a double chest, it returns just the portion of the
     * inventory linked to the half of the chest corresponding to this block state.
     * <p>
     * If the block was changed to a different type in the meantime, the
     * returned inventory might no longer be valid.
     * <p>
     * If this block state is not placed this will return the captured
     * inventory snapshot instead.
     *
     * @return the inventory
     */
    @NotNull
    Inventory getBlockInventory();

    // Paper start - More Chest Block API
    /**
     * Checks whether this chest is blocked
     * by either a block above or a sitting cat
     *
     * @return whether this chest is blocked
     */
    boolean isBlocked();
    // Paper end - More Chest Block API
}
