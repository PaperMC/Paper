package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.jspecify.annotations.Nullable;

/**
 * Called when an entity is damaged by a block
 * <p>
 * For explosions, the Block returned by {@link #getDamager()} has
 * already been cleared. See {@link #getDamagerBlockState()} for a snapshot
 * of the block if it has already been changed.
 */
public interface EntityDamageByBlockEvent extends EntityDamageEvent {

    /**
     * Returns the block that damaged the player.
     *
     * @return Block that damaged the player
     */
    @Nullable Block getDamager();

    /**
     * Returns the captured BlockState of the block that damaged the player.
     * <p>
     * This block state is not placed so {@link BlockState#isPlaced()}
     * will be {@code false}.
     *
     * @return the block state
     */
    @Nullable BlockState getDamagerBlockState();
}
