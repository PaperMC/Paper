package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/**
 * 'hatch' is the number of turtles which may hatch from these eggs.
 * <br>
 * 'eggs' is the number of eggs which appear in this block.
 */
public interface TurtleEgg extends BlockData {

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

    /**
     * Gets the value of the 'hatch' property.
     *
     * @return the 'hatch' value
     */
    int getHatch();

    /**
     * Sets the value of the 'hatch' property.
     *
     * @param hatch the new 'hatch' value
     */
    void setHatch(int hatch);

    /**
     * Gets the maximum allowed value of the 'hatch' property.
     *
     * @return the maximum 'hatch' value
     */
    int getMaximumHatch();
}
