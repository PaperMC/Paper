package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/// Called when a block is formed by entities.
///
/// Examples:
///
///   - Snow formed by a [org.bukkit.entity.Snowman].
///   - Frosted Ice formed by the Frost Walker enchantment.
///
public class EntityBlockFormEvent extends BlockFormEvent {

    private final Entity entity;

    @ApiStatus.Internal
    public EntityBlockFormEvent(@NotNull final Entity entity, @NotNull final Block block, @NotNull final BlockState blockstate) {
        super(block, blockstate);

        this.entity = entity;
    }

    /// Get the entity that formed the block.
    ///
    /// @return Entity involved in event
    @NotNull
    public Entity getEntity() {
        return this.entity;
    }
}
