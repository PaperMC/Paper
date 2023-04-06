package org.bukkit.block.data.type;

import org.bukkit.MinecraftExperimental;
import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.ApiStatus;

/**
 * 'flower_amount' represents the number of petals.
 */
@MinecraftExperimental
@ApiStatus.Experimental
public interface PinkPetals extends Directional {

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

    /**
     * Gets the maximum allowed value of the 'flower_amount' property.
     *
     * @return the maximum 'flower_amount' value
     */
    int getMaximumFlowerAmount();
}
