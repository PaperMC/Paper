package org.bukkit.block.data.type;

import org.bukkit.block.data.Waterlogged;

/**
 * 'persistent' indicates whether or not leaves will be checked by the server to
 * see if they are subject to decay or not.
 * <br>
 * 'distance' denotes how far the block is from a tree and is used in
 * conjunction with 'persistent' flag to determine if the leaves will decay or
 * not.
 *
 * @since 1.13
 */
public interface Leaves extends Waterlogged {

    /**
     * Gets the value of the 'persistent' property.
     *
     * @return the persistent value
     */
    boolean isPersistent();

    /**
     * Sets the value of the 'persistent' property.
     *
     * @param persistent the new 'persistent' value
     */
    void setPersistent(boolean persistent);

    /**
     * Gets the value of the 'distance' property.
     *
     * @return the 'distance' value
     */
    int getDistance();

    /**
     * Sets the value of the 'distance' property.
     *
     * @param distance the new 'distance' value
     */
    void setDistance(int distance);

    // Paper start
    /**
     * Gets the maximum allowed value of the 'distance' property.
     *
     * @return the maximum 'distance' value
     * @since 1.18.1
     */
    int getMaximumDistance();

    /**
     * Gets the minimum allowed value of the 'distance' property.
     *
     * @return the minimum 'distance' value
     * @since 1.18.1
     */
    int getMinimumDistance();
    // Paper end
}
