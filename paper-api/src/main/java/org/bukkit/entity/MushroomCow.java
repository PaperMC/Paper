package org.bukkit.entity;

import java.util.List;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a mushroom {@link Cow}
 */
public interface MushroomCow extends AbstractCow, io.papermc.paper.entity.Shearable { // Paper

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
     * @deprecated use {@link #addEffectToNextStew(io.papermc.paper.potion.SuspiciousEffectEntry, boolean)} as PotionEffect suggests that all attributes are used. In fact, only the PotionEffectType and the duration are used.
     * @param effect the potion effect to add
     * @param overwrite true if any existing effect of the same type should be
     * overwritten
     * @return true if the effects to be applied to the suspicious stew changed
     * as a result of this call
     */
    @Deprecated(forRemoval = true, since = "1.20.2") // Paper - add overloads to use suspicious effect entry to mushroom cow and suspicious stew meta
    boolean addEffectToNextStew(@NotNull PotionEffect effect, boolean overwrite);

    // Paper start - add overloads to use suspicious effect entry to mushroom cow and suspicious stew meta
    /**
     * Adds a suspicious effect entry to be applied to the next suspicious stew
     * received from milking this {@link MushroomCow}.
     *
     * @param suspiciousEffectEntry the suspicious effect entry to add
     * @param overwrite true if any existing effect of the same type should be
     * overwritten
     * @return true if the effects to be applied to the suspicious stew changed
     * as a result of this call
     */
    boolean addEffectToNextStew(@NotNull io.papermc.paper.potion.SuspiciousEffectEntry suspiciousEffectEntry, boolean overwrite);
    // Paper end - add overloads to use suspicious effect entry to mushroom cow and suspicious stew meta

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
    // Paper start
    /**
     * Gets how long the effect applied to stew
     * from this mushroom cow is.
     *
     * @return duration of the effect (in ticks)
     * @deprecated Mushroom cows can now hold multiple effects, use {@link #getStewEffects()}
     */
    @Deprecated(forRemoval = true, since = "1.20.2")
    @org.jetbrains.annotations.Contract("-> fail")
    default int getStewEffectDuration() {
        throw new UnsupportedOperationException("Mushroom cows can now hold multiple effects. Use #getStewEffects");
    }

    /**
     * Sets how long the effect applied to stew
     * from this mushroom cow is.
     *
     * @param duration duration of the effect (in ticks)
     * @deprecated Mushroom cows can now hold multiple effects, use {@link #setStewEffects(java.util.List)}
     */
    @Deprecated(forRemoval = true, since = "1.20.2")
    @org.jetbrains.annotations.Contract("_ -> fail")
    default void setStewEffectDuration(int duration) {
        throw new UnsupportedOperationException("Mushroom cows can now hold multiple effects. Use #setStewEffects");
    }

    /**
     * Gets the type of effect applied to stew
     * from this mushroom cow is.
     *
     * @return effect type, or null if an effect is currently not set
     * @deprecated Mushroom cows can now hold multiple effects, use {@link #getStewEffects()}
     * @throws UnsupportedOperationException
     */
    @Deprecated(forRemoval = true, since = "1.20.2")
    @org.jetbrains.annotations.Contract("-> fail")
    default org.bukkit.potion.PotionEffectType getStewEffectType() {
        throw new UnsupportedOperationException("Mushroom cows can now hold multiple effects. Use #getStewEffects");
    }

    /**
     * Sets the type of effect applied to stew
     * from this mushroom cow is.
     *
     * @param type new effect type
     *             or null if this cow does not give effects
     * @deprecated Mushroom cows can now hold multiple effects, use {@link #setStewEffects(java.util.List)}
     * @throws UnsupportedOperationException
     */
    @Deprecated(forRemoval = true, since = "1.20.2")
    @org.jetbrains.annotations.Contract("_ -> fail")
    default void setStewEffect(@org.jetbrains.annotations.Nullable org.bukkit.potion.PotionEffectType type) {
        throw new UnsupportedOperationException("Mushroom cows can now hold multiple effects. Use #setStewEffects");
    }

    /**
     * Returns an immutable collection of the effects applied to stew
     * items for this mushroom cow.
     *
     * @return immutable effect entry collection
     */
    java.util.@NotNull @org.jetbrains.annotations.Unmodifiable List<io.papermc.paper.potion.SuspiciousEffectEntry> getStewEffects();

    /**
     * Sets effects applied to stew items for this mushroom cow.
     *
     * @param effects effect entry list
     */
    void setStewEffects(java.util.@NotNull List<io.papermc.paper.potion.SuspiciousEffectEntry> effects);
    // Paper end
}
