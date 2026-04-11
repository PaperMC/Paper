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
     * @param flowerAmount the new 'flower_amount' value
     */
    void setFlowerAmount(int flowerAmount);

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
