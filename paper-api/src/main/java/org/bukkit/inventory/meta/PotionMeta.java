package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.Color;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Represents a potion or item that can have custom effects.
 */
public interface PotionMeta extends ItemMeta {

    /**
     * Sets the underlying potion data
     *
     * @param data PotionData to set the base potion state to
     * @deprecated Upgraded / extended potions are now their own {@link PotionType} use {@link #setBasePotionType} instead.
     */
    @Deprecated(since = "1.20.6", forRemoval = true)
    void setBasePotionData(@Nullable PotionData data);

    /**
     * Returns the potion data about the base potion
     *
     * @return a PotionData object
     * @deprecated Upgraded / extended potions are now their own {@link PotionType} use {@link #getBasePotionType()} instead.
     */
    @Nullable
    @Deprecated(since = "1.20.6", forRemoval = true)
    PotionData getBasePotionData();

    /**
     * Sets the underlying potion type
     *
     * @param type PotionType to set the base potion state to
     */
    void setBasePotionType(@Nullable PotionType type);

    /**
     * Returns the potion type about the base potion
     *
     * @return a PotionType object
     */
    @Nullable
    PotionType getBasePotionType();

    /**
     * Checks for the presence of a base potion type
     *
     * @return true if a base potion type is present
     */
    boolean hasBasePotionType();

    /**
     * Checks for the presence of custom potion effects.
     *
     * @return true if custom potion effects are applied
     */
    boolean hasCustomEffects();

    /**
     * Gets an immutable list containing all custom potion effects applied to
     * this potion.
     * <p>
     * Plugins should check that hasCustomEffects() returns true before calling
     * this method.
     *
     * @return the immutable list of custom potion effects
     */
    @NotNull
    List<PotionEffect> getCustomEffects();

    /**
     * All effects that this potion meta holds.
     * <p>
     * This is a combination of the base potion type and any custom effects.
     *
     * @return an unmodifiable list of all effects.
     */
    @NotNull
    @Unmodifiable List<PotionEffect> getAllEffects();

    /**
     * Adds a custom potion effect to this potion.
     *
     * @param effect the potion effect to add
     * @param overwrite true if any existing effect of the same type should be
     * overwritten
     * @return true if the potion meta changed as a result of this call
     */
    boolean addCustomEffect(@NotNull PotionEffect effect, boolean overwrite);

    /**
     * Removes a custom potion effect from this potion.
     *
     * @param type the potion effect type to remove
     * @return true if the potion meta changed as a result of this call
     */
    boolean removeCustomEffect(@NotNull PotionEffectType type);

    /**
     * Checks for a specific custom potion effect type on this potion.
     *
     * @param type the potion effect type to check for
     * @return true if the potion has this effect
     */
    boolean hasCustomEffect(@NotNull PotionEffectType type);

    /**
     * Moves a potion effect to the top of the potion effect list.
     * <p>
     * This causes the client to display the potion effect in the potion's name.
     *
     * @param type the potion effect type to move
     * @return true if the potion meta changed as a result of this call
     * @deprecated use {@link #setBasePotionType(org.bukkit.potion.PotionType)}
     */
    @Deprecated(since = "1.9")
    boolean setMainEffect(@NotNull PotionEffectType type);

    /**
     * Removes all custom potion effects from this potion.
     *
     * @return true if the potion meta changed as a result of this call
     */
    boolean clearCustomEffects();

    /**
     * Checks for existence of a potion color.
     *
     * @return true if this has a custom potion color
     */
    boolean hasColor();

    /**
     * Gets the potion color that is set. A custom potion color will alter the
     * display of the potion in an inventory slot.
     * <p>
     * Plugins should check that hasColor() returns <code>true</code> before
     * calling this method.
     *
     * @return the potion color that is set
     */
    @Nullable
    Color getColor();

    /**
     * Sets the potion color. A custom potion color will alter the display of
     * the potion in an inventory slot.
     *
     * @param color the color to set
     */
    void setColor(@Nullable Color color);

    /**
     * Computes the effective colour of this potion meta.
     * <p>
     * This blends all custom effects, or uses a default fallback color.
     *
     * @return the effective potion color
     */
    @NotNull
    Color computeEffectiveColor();

    /**
     * Checks for existence of a custom potion name translation suffix.
     *
     * @deprecated conflicting name, use {@link #hasCustomPotionName()}
     * @return true if this has a custom potion name
     */
    @Deprecated(forRemoval = true, since = "1.21.4")
    default boolean hasCustomName() {
        return this.hasCustomPotionName();
    }

    /**
     * Gets the potion name translation suffix that is set.
     * <p>
     * Plugins should check that {@link #hasCustomPotionName()} returns {@code true}
     * before calling this method.
     *
     * @deprecated conflicting name, use {@link #getCustomPotionName()}
     * @return the potion name that is set
     */
    @Deprecated(forRemoval = true, since = "1.21.4")
    @Nullable
    default String getCustomName() {
        return this.getCustomPotionName();
    }

    /**
     * Sets the potion name translation suffix.
     *
     * @deprecated conflicting name, use {@link #setCustomPotionName(String)}
     * @param name the name to set
     */
    @Deprecated(forRemoval = true, since = "1.21.4")
    default void setCustomName(@Nullable String name) {
        this.setCustomPotionName(name);
    }

    /**
     * Checks for existence of a custom potion name translation suffix.
     *
     * @return true if this has a custom potion name
     */
    boolean hasCustomPotionName();

    /**
     * Gets the potion name translation suffix that is set.
     * <p>
     * Plugins should check that {@link #hasCustomPotionName()} returns {@code true}
     * before calling this method.
     *
     * @return the potion name that is set
     */
    @Nullable
    String getCustomPotionName();

    /**
     * Sets the potion name translation suffix.
     *
     * @param name the name to set
     */
    void setCustomPotionName(@Nullable String name);

    @Override
    PotionMeta clone();
}
