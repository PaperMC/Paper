package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/**
 * 'bites' represents the amount of bites which have been taken from this slice
 * of cake.
 * <br>
 * A value of 0 indicates that the cake has not been eaten, whilst a value of
 * {@link #getMaximumBites()} indicates that it is all gone :(
 */
public interface Cake extends BlockData {

    /**
     * Gets the value of the 'bites' property.
     *
     * @return the 'bites' value
     */
    int getBites();

    /**
     * Sets the value of the 'bites' property.
     *
     * @param bites the new 'bites' value
     */
    void setBites(int bites);

    /**
     * Gets the maximum allowed value of the 'bites' property.
     *
     * @return the maximum 'bites' value
     */
    int getMaximumBites();
}
