package org.bukkit.entity;

import java.util.List;
import org.bukkit.Color;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 1.0.0 R1
 */
public interface Arrow extends AbstractArrow {

    /**
     * Sets the underlying potion data
     *
     * @param data PotionData to set the base potion state to
     * @deprecated Upgraded / extended potions are now their own {@link PotionType} use {@link #setBasePotionType} instead.
     * @since 1.14
     */
    @Deprecated(since = "1.20.6")
    void setBasePotionData(@Nullable PotionData data);

    /**
     * Returns the potion data about the base potion
     *
     * @return a PotionData object
     * @deprecated Upgraded / extended potions are now their own {@link PotionType} use {@link #getBasePotionType()} instead.
     * @since 1.14
     */
    @Nullable
    @Deprecated(since = "1.20.6")
    PotionData getBasePotionData();

    /**
     * Sets the underlying potion type
     *
     * @param type PotionType to set the base potion state to
     * @since 1.20.2
     */
    void setBasePotionType(@Nullable PotionType type);

    /**
     * Returns the potion type about the base potion
     *
     * @return a PotionType object
     * @since 1.20.2
     */
    @Nullable
    PotionType getBasePotionType();

    /**
     * Gets the color of this arrow.
     *
     * @return arrow {@link Color} or null if not color is set
     * @since 1.14
     */
    @Nullable
    Color getColor();

    /**
     * Sets the color of this arrow. Will be applied as a tint to its particles.
     *
     * @param color arrow color, null to clear the color
     * @since 1.14
     */
    void setColor(@Nullable Color color);

    /**
     * Checks for the presence of custom potion effects.
     *
     * @return true if custom potion effects are applied
     * @since 1.14
     */
    boolean hasCustomEffects();

    /**
     * Gets an immutable list containing all custom potion effects applied to
     * this arrow.
     * <p>
     * Plugins should check that hasCustomEffects() returns true before calling
     * this method.
     *
     * @return the immutable list of custom potion effects
     * @since 1.14
     */
    @NotNull
    List<PotionEffect> getCustomEffects();

    /**
     * Adds a custom potion effect to this arrow.
     *
     * @param effect the potion effect to add
     * @param overwrite true if any existing effect of the same type should be
     * overwritten
     * @return true if the effect was added as a result of this call
     * @since 1.14
     */
    boolean addCustomEffect(@NotNull PotionEffect effect, boolean overwrite);

    /**
     * Removes a custom potion effect from this arrow.
     *
     * @param type the potion effect type to remove
     * @return true if the effect was removed as a result of this call
     * @throws IllegalArgumentException if this operation would leave the Arrow
     * in a state with no Custom Effects and PotionType.UNCRAFTABLE
     * @since 1.14
     */
    boolean removeCustomEffect(@NotNull PotionEffectType type);

    /**
     * Checks for a specific custom potion effect type on this arrow.
     *
     * @param type the potion effect type to check for
     * @return true if the potion has this effect
     * @since 1.14
     */
    boolean hasCustomEffect(@Nullable PotionEffectType type);

    /**
     * Removes all custom potion effects from this arrow.
     *
     * @throws IllegalArgumentException if this operation would leave the Arrow
     * in a state with no Custom Effects and PotionType.UNCRAFTABLE
     * @since 1.14
     */
    void clearCustomEffects();
}
