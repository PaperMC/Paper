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
     * Gets the creaking currently tied to this heart.
     *
     * @return the creaking if one is active, null otherwise
     */
    @Nullable
    Creaking getCreaking();

    /**
     * Checks if this heart is currently active and controlling a creaking.
     *
     * @return true if active
     */
    boolean isActive();
}
