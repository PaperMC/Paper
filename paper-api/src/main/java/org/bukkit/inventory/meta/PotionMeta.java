package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.Color;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a potion or item that can have custom effects.
 *
 * @since 1.4.5 R1.0
 */
public interface PotionMeta extends ItemMeta {

    /**
     * Sets the underlying potion data
     *
     * @param data PotionData to set the base potion state to
     * @deprecated Upgraded / extended potions are now their own {@link PotionType} use {@link #setBasePotionType} instead.
     * @since 1.9.4
     */
    @Deprecated(since = "1.20.6")
    void setBasePotionData(@Nullable PotionData data);

    /**
     * Returns the potion data about the base potion
     *
     * @return a PotionData object
     * @deprecated Upgraded / extended potions are now their own {@link PotionType} use {@link #getBasePotionType()} instead.
     * @since 1.9.4
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
     * Checks for the presence of a base potion type
     *
     * @return true if a base potion type is present
     * @since 1.20.6
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
     * @since 1.11
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
     * @since 1.11
     */
    @Nullable
    Color getColor();

    /**
     * Sets the potion color. A custom potion color will alter the display of
     * the potion in an inventory slot.
     *
     * @param color the color to set
     * @since 1.11
     */
    void setColor(@Nullable Color color);

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
     * @since 1.21.4
     */
    boolean hasCustomPotionName();

    /**
     * Gets the potion name translation suffix that is set.
     * <p>
     * Plugins should check that {@link #hasCustomPotionName()} returns {@code true}
     * before calling this method.
     *
     * @return the potion name that is set
     * @since 1.21.4
     */
    @Nullable
    String getCustomPotionName();

    /**
     * Sets the potion name translation suffix.
     *
     * @param name the name to set
     * @since 1.21.4
     */
    void setCustomPotionName(@Nullable String name);

    @Override
    PotionMeta clone();
}
