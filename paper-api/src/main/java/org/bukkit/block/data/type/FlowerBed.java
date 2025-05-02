package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;

/**
 * 'flower_amount' represents the number of flowers.
 */
public interface FlowerBed extends Directional {

    /**
     * Gets the value of the 'flower_amount' property.
     *
     * @return the 'flower_amount' value
     */
    int getFlowerAmount();

    /**
     * Sets the value of the 'flower_amount' property.
     *
     * @param flower_amount the new 'flower_amount' value
     */
    void setFlowerAmount(int flower_amount);

    // Paper start
    /**
     * Gets the minimum allowed value of the 'flower_amount' property.
     *
     * @return the minimum 'flower_amount' value
     */
    int getMinimumFlowerAmount();
    // Paper end

    /**
     * Gets the maximum allowed value of the 'flower_amount' property.
     *
     * @return the maximum 'flower_amount' value
     */
    int getMaximumFlowerAmount();
}
