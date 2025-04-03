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
    org.bukkit.block.Orientation getOrientation();

    /**
     * Sets the value of the 'orientation' property.
     *
     * @param orientation the new 'orientation' value
     */
    void setOrientation(@NotNull org.bukkit.block.Orientation orientation);

    /**
     * The directions the Jigsaw can be oriented.
     *
     * @deprecated this property is not specific to the Jigsaw, use
     * {@link org.bukkit.block.Orientation} instead. All references
     * to this enum will be redirected to that enum at runtime.
     */
    @Deprecated
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
