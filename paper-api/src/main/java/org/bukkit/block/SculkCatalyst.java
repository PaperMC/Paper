package org.bukkit.block;

import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

/// Represents a captured state of a sculk catalyst.
public interface SculkCatalyst extends TileState {

    /// Causes a new sculk bloom, as if an entity just died around this catalyst.
    ///
    /// Typically, charges should be set to the exp reward of a mob
    /// ([EntityDeathEvent#getDroppedExp()]), which is usually
    /// 3-5 for animals, and 5-10 for the average mob (up to 50 for
    /// wither skeletons). Roughly speaking, for each charge, 1 more
    /// sculk block will be placed.
    ///
    /// If `charges > 1000`, multiple cursors will be spawned in the
    /// block.
    ///
    /// @param block which block to spawn the cursor in
    /// @param charges how much charge to spawn.
    void bloom(@NotNull Block block, int charges);

    // Paper start - SculkCatalyst bloom API
    /// Bloom at the specified position as if an entity that drops experience just died there.
    ///
    /// @param position position to bloom at
    /// @param charge charge to bloom with, normally the amount of experience dropped from the dead entity
    void bloom(@NotNull io.papermc.paper.math.Position position, int charge);
    // Paper end - SculkCatalyst bloom API
}
