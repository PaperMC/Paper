package org.bukkit.block.data.type;

import org.bukkit.block.data.Waterlogged;

/**
 * 'pickles' indicates the number of pickles in this block.
 */
public interface SeaPickle extends Waterlogged {

    /**
     * Gets the value of the 'pickles' property.
     *
     * @return the 'pickles' value
     */
    int getPickles();

    /**
     * Sets the value of the 'pickles' property.
     *
     * @param pickles the new 'pickles' value
     */
    void setPickles(int pickles);

    /**
     * Gets the minimum allowed value of the 'pickles' property.
     *
     * @return the minimum 'pickles' value
     */
    int getMinimumPickles();

    /**
     * Gets the maximum allowed value of the 'pickles' property.
     *
     * @return the maximum 'pickles' value
     */
    int getMaximumPickles();
}
