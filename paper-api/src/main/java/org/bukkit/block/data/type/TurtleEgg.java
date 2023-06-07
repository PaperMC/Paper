package org.bukkit.block.data.type;

import org.bukkit.block.data.Hatchable;

/**
 * 'eggs' is the number of eggs which appear in this block.
 */
public interface TurtleEgg extends Hatchable {

    /**
     * Gets the value of the 'eggs' property.
     *
     * @return the 'eggs' value
     */
    int getEggs();

    /**
     * Sets the value of the 'eggs' property.
     *
     * @param eggs the new 'eggs' value
     */
    void setEggs(int eggs);

    /**
     * Gets the minimum allowed value of the 'eggs' property.
     *
     * @return the minimum 'eggs' value
     */
    int getMinimumEggs();

    /**
     * Gets the maximum allowed value of the 'eggs' property.
     *
     * @return the maximum 'eggs' value
     */
    int getMaximumEggs();
}
