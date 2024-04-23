package org.bukkit.inventory.meta.components;

import java.util.List;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

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

    /**
     * Gets the time in seconds it will take for this item to be eaten.
     *
     * @return eat time
     */
    float getEatSeconds();

    /**
     * Sets the time in seconds it will take for this item to be eaten.
     *
     * @param eatSeconds new eat time
     */
    void setEatSeconds(float eatSeconds);

    /**
     * Gets the effects which may be applied by this item when eaten.
     *
     * @return food effects
     */
    @NotNull
    List<FoodEffect> getEffects();

    /**
     * Sets the effects which may be applied by this item when eaten.
     *
     * @param effects new effects
     */
    void setEffects(@NotNull List<FoodEffect> effects);

    /**
     * Adds an effect which may be applied by this item when eaten.
     *
     * @param effect the effect
     * @param probability the probability of the effect being applied
     * @return the added effect
     */
    @NotNull
    FoodEffect addEffect(@NotNull PotionEffect effect, float probability);

    /**
     * An effect which may be applied by this item when eaten.
     */
    public interface FoodEffect extends ConfigurationSerializable {

        /**
         * Gets the effect which may be applied.
         *
         * @return the effect
         */
        @NotNull
        PotionEffect getEffect();

        /**
         * Sets the effect which may be applied.
         *
         * @param effect the new effect
         */
        void setEffect(@NotNull PotionEffect effect);

        /**
         * Gets the probability of this effect being applied.
         *
         * @return probability
         */
        float getProbability();

        /**
         * Sets the probability of this effect being applied.
         *
         * @param probability between 0 and 1 inclusive.
         */
        void setProbability(float probability);
    }
}
