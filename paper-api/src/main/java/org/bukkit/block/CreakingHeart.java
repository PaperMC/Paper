package org.bukkit.block;

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
     */
    void setCreaking(@Nullable Creaking creaking);

    /**
     * Attempts to spawn a creaking for protect this creaking heart.
     *
     * @return the {@link Creaking} for protect the creaking heart or null if fails
     */
    @NullMarked
    Creaking spawnCreaking();

}
