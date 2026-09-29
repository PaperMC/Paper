package org.bukkit.block;

import org.bukkit.Location;
import org.bukkit.entity.Creaking;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a captured state of a creaking heart.
 */
@NullMarked
public interface CreakingHeart extends TileState {

    /**
     * Gets the creaking protecting this creaking heart.
     *
     * @return the creaking, or {@code null} if this creaking heart doesn't have a protector.
     */
    @Nullable Creaking getCreaking();

    /**
     * Sets the creaking protecting this creaking heart.
     *
     * @param creaking the creaking, or {@code null} to remove any existing creaking.
     * @throws IllegalArgumentException if the creaking is in another world.
     * @throws IllegalStateException if this block state is not placed.
     */
    void setCreaking(@Nullable Creaking creaking);

    /**
     * Attempts to spawn a creaking to protect this creaking heart.
     *
     * @return the {@link Creaking} that was spawned to protect this heart, or {@code null} if it failed.
     * @throws IllegalStateException if this block state is not placed.
     */
    @Nullable Creaking spawnCreaking();

    /**
     * Attempts to spread resin to adjacent blocks.
     *
     * @apiNote This method triggers events related to a block being modified.
     * @return the location resin was spread to, or {@code null} if it failed to spread.
     * @throws IllegalStateException if this block state is not placed.
     */
    @Nullable Location spreadResin();
}
