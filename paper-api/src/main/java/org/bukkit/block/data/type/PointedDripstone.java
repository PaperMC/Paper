package org.bukkit.block.data.type;

import java.util.Set;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;
import org.jetbrains.annotations.NotNull;

/**
 * 'thickness' represents the dripstone thickness.
 * <br>
 * 'vertical_direction' represents the dripstone orientation.
 * <br>
 * Some blocks may not be able to face in all directions, use
 * {@link #getVerticalDirections()} to get all possible directions for this
 * block.
 */
public interface PointedDripstone extends Waterlogged {

    /**
     * Gets the value of the 'vertical_direction' property.
     *
     * @return the 'vertical_direction' value
     */
    @NotNull
    BlockFace getVerticalDirection();

    /**
     * Sets the value of the 'vertical_direction' property.
     *
     * @param direction the new 'vertical_direction' value
     */
    void setVerticalDirection(@NotNull BlockFace direction);

    /**
     * Gets the faces which are applicable to this block.
     *
     * @return the allowed 'vertical_direction' values
     */
    @NotNull
    Set<BlockFace> getVerticalDirections();

    /**
     * Gets the value of the 'thickness' property.
     *
     * @return the 'thickness' value
     */
    @NotNull
    Thickness getThickness();

    /**
     * Sets the value of the 'thickness' property.
     *
     * @param thickness the new 'thickness' value
     */
    void setThickness(@NotNull Thickness thickness);

    /**
     * Represents the thickness of the dripstone, corresponding to its position
     * within a multi-block dripstone formation.
     */
    public enum Thickness {
        /**
         * Extended tip.
         */
        TIP_MERGE,
        /**
         * Just the tip.
         */
        TIP,
        /**
         * Top section.
         */
        FRUSTUM,
        /**
         * Middle section.
         */
        MIDDLE,
        /**
         * Base.
         */
        BASE;
    }
}
