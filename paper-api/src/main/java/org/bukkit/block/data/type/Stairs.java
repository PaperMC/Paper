package org.bukkit.block.data.type;

import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;
import org.jetbrains.annotations.NotNull;

/**
 * 'shape' represents the texture and bounding box shape of these stairs.
 */
public interface Stairs extends Bisected, Directional, Waterlogged {

    /**
     * Gets the value of the 'shape' property.
     *
     * @return the 'shape' value
     */
    @NotNull
    Shape getShape();

    /**
     * Sets the value of the 'shape' property.
     *
     * @param shape the new 'shape' value
     */
    void setShape(@NotNull Shape shape);

    /**
     * The shape of a stair block - used for constructing corners.
     */
    public enum Shape {
        /**
         * Regular stair block.
         */
        STRAIGHT,
        /**
         * Inner corner stair block with higher left side.
         */
        INNER_LEFT,
        /**
         * Inner corner stair block with higher right side.
         */
        INNER_RIGHT,
        /**
         * Outer corner stair block with higher left side.
         */
        OUTER_LEFT,
        /**
         * Outer corner stair block with higher right side.
         */
        OUTER_RIGHT;
    }
}
