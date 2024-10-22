package org.bukkit.inventory.meta.components;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a component which can turn any item into food.
 */
@ApiStatus.Experimental
public interface FoodComponent extends ConfigurationSerializable {

    /**
     * Gets the food restored by this item when eaten.
     *
     * @return nutrition value
     */
    int getNutrition();

    /**
     * Sets the food restored by this item when eaten.
     *
     * @param nutrition new nutrition value, must be non-negative
     */
    void setNutrition(int nutrition);

    /**
     * Gets the saturation restored by this item when eaten.
     *
     * @return saturation value
     */
    float getSaturation();

    /**
     * Sets the saturation restored by this item when eaten.
     *
     * @param saturation new saturation value
     */
    void setSaturation(float saturation);

    /**
     * Gets if this item can be eaten even when not hungry.
     *
     * @return true if always edible
     */
    boolean canAlwaysEat();

    /**
     * Sets if this item can be eaten even when not hungry.
     *
     * @param canAlwaysEat whether always edible
     */
    void setCanAlwaysEat(boolean canAlwaysEat);
}
