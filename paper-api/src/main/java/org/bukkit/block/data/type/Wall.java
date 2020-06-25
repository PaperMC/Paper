package org.bukkit.block.data.type;

import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;
import org.jetbrains.annotations.NotNull;

/**
 * This class encompasses the 'north', 'east', 'south', 'west', height flags
 * which are used to set the height of a wall.
 */
public interface Wall extends Waterlogged {

    /**
     * Gets the height of the specified face.
     *
     * @param face to check
     * @return if face is enabled
     */
    @NotNull
    Height getHeight(@NotNull BlockFace face);

    /**
     * Set the height of the specified face.
     *
     * @param face to set
     * @param height the height
     */
    void setHeight(@NotNull BlockFace face, @NotNull Height height);

    /**
     * The different heights a face of a wall may have.
     */
    public enum Height {
        /**
         * No wall present.
         */
        NONE,
        /**
         * Low wall present.
         */
        LOW,
        /**
         * Tall wall present.
         */
        TALL;
    }
}
