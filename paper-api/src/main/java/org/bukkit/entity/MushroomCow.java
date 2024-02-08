package org.bukkit.entity;

import java.util.List;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a mushroom {@link Cow}
 */
public interface MushroomCow extends Cow {

    /**
     * Checks for the presence of custom potion effects to be applied to the
     * next suspicious stew received from milking this {@link MushroomCow}.
     *
     * @return true if custom potion effects are applied to the stew
     */
    boolean hasEffectsForNextStew();

    /**
     * Gets an immutable list containing all custom potion effects applied to
     * the next suspicious stew received from milking this {@link MushroomCow}.
     * <p>
     * Plugins should check that hasCustomEffects() returns true before calling
     * this method.
     *
     * @return an immutable list of custom potion effects
     */
    @NotNull
    List<PotionEffect> getEffectsForNextStew();

    /**
     * Adds a custom potion effect to be applied to the next suspicious stew
     * received from milking this {@link MushroomCow}.
     *
     * @param effect the potion effect to add
     * @param overwrite true if any existing effect of the same type should be
     * overwritten
     * @return true if the effects to be applied to the suspicious stew changed
     * as a result of this call
     */
    boolean addEffectToNextStew(@NotNull PotionEffect effect, boolean overwrite);

    /**
     * Removes a custom potion effect from being applied to the next suspicious
     * stew received from milking this {@link MushroomCow}.
     *
     * @param type the potion effect type to remove
     * @return true if the effects to be applied to the suspicious stew changed
     * as a result of this call
     */
    boolean removeEffectFromNextStew(@NotNull PotionEffectType type);

    /**
     * Checks for a specific custom potion effect type to be applied to the next
     * suspicious stew received from milking this {@link MushroomCow}.
     *
     * @param type the potion effect type to check for
     * @return true if the suspicious stew to be generated has this effect
     */
    boolean hasEffectForNextStew(@NotNull PotionEffectType type);

    /**
     * Removes all custom potion effects to be applied to the next suspicious
     * stew received from milking this {@link MushroomCow}.
     */
    void clearEffectsForNextStew();

    /**
     * Get the variant of this cow.
     *
     * @return cow variant
     */
    @NotNull
    public Variant getVariant();

    /**
     * Set the variant of this cow.
     *
     * @param variant cow variant
     */
    public void setVariant(@NotNull Variant variant);

    /**
     * Represents the variant of a cow - ie its color.
     */
    public enum Variant {
        /**
         * Red mushroom cow.
         */
        RED,
        /**
         * Brown mushroom cow.
         */
        BROWN;
    }
}
