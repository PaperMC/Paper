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
     * @return the creaking or null if this creaking heart don't have protector.
     */
    @Nullable
    Creaking getCreaking();

    /**
     * Sets the creaking protecting this creaking heart.
     *
     * @param creaking the creaking or null for make this creaking heart don't have protector
     * @throws IllegalArgumentException if the creaking passed it's in another world
     * @throws IllegalStateException if this block state is not placed
     */
    void setCreaking(@Nullable Creaking creaking);

    /**
     * Gets the max distance between the Creaking Heart and the Creaking before to remove.
     *
     * @return the max distance
     */
    int getCreakingRemovalDistance();

    /**
     * Sets the max distance between the Creaking Heart and the Creaking before to remove.
     *
     * @param distance the max distance
     * @throws IllegalArgumentException if the distance is negative
     */
    void setCreakingRemovalDistance(int distance);

    /**
     * Attempts to spawn a creaking for protect this creaking heart.
     *
     * @return the {@link Creaking} for protect the creaking heart or null if fails
     * @throws IllegalStateException if this block state is not placed
     */
    @Nullable
    Creaking spawnCreaking();

    /**
     * Attempts to spread resin to adjacent blocks.
     *
     * @apiNote This method triggers events related to a block being modified
     * @return the location of spread resin or null if it cannot spread
     * @throws IllegalStateException if this block state is not placed
     */
    @Nullable
    Location spreadResin();
}
