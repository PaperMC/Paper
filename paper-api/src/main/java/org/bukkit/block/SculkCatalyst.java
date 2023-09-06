package org.bukkit.block;

import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured state of a sculk catalyst.
 */
public interface SculkCatalyst extends TileState {

    /**
     * Causes a new sculk bloom, as if an entity just died around this catalyst.
     * <p>
     * Typically, charges should be set to the exp reward of a mob
     * ({@link EntityDeathEvent#getDroppedExp()}), which is usually
     * 3-5 for animals, and 5-10 for the average mob (up to 50 for
     * wither skeletons). Roughly speaking, for each charge, 1 more
     * sculk block will be placed.
     * <p>
     * If <code>charges > 1000</code>, multiple cursors will be spawned in the
     * block.
     *
     * @param block which block to spawn the cursor in
     * @param charges how much charge to spawn.
     */
    void bloom(@NotNull Block block, int charges);
}
