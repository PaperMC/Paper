package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

/**
 * 'orientation' is the direction the block is facing.
 */
public interface Jigsaw extends BlockData {

    /**
     * Gets the value of the 'orientation' property.
     *
     * @return the 'orientation' value
     */
    @NotNull
    Orientation getOrientation();

    /**
     * Sets the value of the 'orientation' property.
     *
     * @param orientation the new 'orientation' value
     */
    void setOrientation(@NotNull Orientation orientation);

    /**
     * The directions the Jigsaw can be oriented.
     */
    public enum Orientation {

        DOWN_EAST,
        DOWN_NORTH,
        DOWN_SOUTH,
        DOWN_WEST,
        UP_EAST,
        UP_NORTH,
        UP_SOUTH,
        UP_WEST,
        WEST_UP,
        EAST_UP,
        NORTH_UP,
        SOUTH_UP;
    }
}
