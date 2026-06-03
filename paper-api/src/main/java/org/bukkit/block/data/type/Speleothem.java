package org.bukkit.block.data.type;

import java.util.Set;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;
import org.jspecify.annotations.NullMarked;

/**
 * 'thickness' represents the speleothem thickness.
 * <br>
 * 'vertical_direction' represents the speleothem orientation.
 * <br>
 * Some blocks may not be able to face in all directions, use
 * {@link #getVerticalDirections()} to get all possible directions for this
 * block.
 */
@NullMarked
public interface Speleothem extends Waterlogged {

    /**
     * Gets the value of the 'vertical_direction' property.
     *
     * @return the 'vertical_direction' value
     */
    BlockFace getVerticalDirection();

    /**
     * Sets the value of the 'vertical_direction' property.
     *
     * @param direction the new 'vertical_direction' value
     */
    void setVerticalDirection(BlockFace direction);

    /**
     * Gets the faces which are applicable to this block.
     *
     * @return the allowed 'vertical_direction' values
     */
    Set<BlockFace> getVerticalDirections();

    /**
     * Gets the value of the 'thickness' property.
     *
     * @return the 'thickness' value
     */
    Thickness getThickness();

    /**
     * Sets the value of the 'thickness' property.
     *
     * @param thickness the new 'thickness' value
     */
    void setThickness(Thickness thickness);

    /**
     * Represents the thickness of the speleothem, corresponding to its position
     * within a multi-block speleothem formation.
     */
    enum Thickness { // todo - snapshot - check ABI
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
