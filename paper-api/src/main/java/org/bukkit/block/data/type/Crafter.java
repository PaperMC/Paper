package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.jetbrains.annotations.NotNull;

/**
 * 'orientation' is the direction the block is facing.
 * <br>
 * Similar to {@link Powerable}, 'triggered' indicates whether or not the
 * dispenser is currently activated.
 * <br>
 * 'crafting' is whether crafter's mouth is open and top is glowing.
 */
public interface Crafter extends BlockData {

    /**
     * Gets the value of the 'crafting' property.
     *
     * @return the 'crafting' value
     */
    boolean isCrafting();

    /**
     * Sets the value of the 'crafting' property.
     *
     * @param crafting the new 'crafting' value
     */
    void setCrafting(boolean crafting);

    /**
     * Gets the value of the 'triggered' property.
     *
     * @return the 'triggered' value
     */
    boolean isTriggered();

    /**
     * Sets the value of the 'triggered' property.
     *
     * @param triggered the new 'triggered' value
     */
    void setTriggered(boolean triggered);

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
     * The directions the Crafter can be oriented.
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
