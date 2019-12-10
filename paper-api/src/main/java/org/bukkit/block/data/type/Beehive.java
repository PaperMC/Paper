package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;

/**
 * 'honey_level' represents the amount of honey stored in the hive.
 */
public interface Beehive extends Directional {

    /**
     * Gets the value of the 'honey_level' property.
     *
     * @return the 'honey_level' value
     */
    int getHoneyLevel();

    /**
     * Sets the value of the 'honey_level' property.
     *
     * @param honeyLevel the new 'honey_level' value
     */
    void setHoneyLevel(int honeyLevel);

    /**
     * Gets the maximum allowed value of the 'honey_level' property.
     *
     * @return the maximum 'honey_level' value
     */
    int getMaximumHoneyLevel();
}
