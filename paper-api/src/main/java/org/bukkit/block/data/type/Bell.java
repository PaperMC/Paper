package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;

/**
 * 'attachment' denotes how the bell is attached to its block.
 */
public interface Bell extends Directional {

    /**
     * What the bell is attached to.
     */
    public enum Attachment {

        /**
         * Placed on floor.
         */
        FLOOR,
        /**
         * Placed on ceiling.
         */
        CEILING,
        /**
         * Placed on one wall.
         */
        SINGLE_WALL,
        /**
         * Placed between two walls.
         */
        DOUBLE_WALL;
    }
}
