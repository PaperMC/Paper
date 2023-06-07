package org.bukkit.block.data;

/**
 * 'hatch' is the number of entities which may hatch from these eggs.
 */
public interface Hatchable extends BlockData {

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
