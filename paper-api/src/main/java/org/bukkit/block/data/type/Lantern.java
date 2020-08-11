package org.bukkit.block.data.type;

import org.bukkit.block.data.Waterlogged;

/**
 * 'hanging' denotes whether the lantern is hanging from a block.
 */
public interface Lantern extends Waterlogged {

    /**
     * Gets the value of the 'hanging' property.
     *
     * @return the 'hanging' value
     */
    boolean isHanging();

    /**
     * Sets the value of the 'hanging' property.
     *
     * @param hanging the new 'hanging' value
     */
    void setHanging(boolean hanging);
}
