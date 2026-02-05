package org.bukkit.inventory.meta;

import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a bucket of tropical fish.
 */
@NullMarked
public interface TropicalFishBucketMeta extends ItemMeta {

    /**
     * Gets the color of the fish's pattern.
     * <p>
     * Plugins should check that hasPatternColor() returns {@code true} before
     * calling this method.
     *
     * @return pattern color
     * @throws IllegalStateException if no pattern color is set
     */
    DyeColor getPatternColor();

    /**
     * Sets the color of the fish's pattern.
     *
     * @param color pattern color
     */
    void setPatternColor(DyeColor color);

    /**
     * Gets the color of the fish's body.
     * <p>
     * Plugins should check that hasBodyColor() returns {@code true} before
     * calling this method.
     *
     * @return pattern color
     * @throws IllegalStateException if no body color is set
     */
    DyeColor getBodyColor();

    /**
     * Sets the color of the fish's body.
     *
     * @param color body color
     */
    void setBodyColor(DyeColor color);

    /**
     * Gets the fish's pattern.
     * <p>
     * Plugins should check that hasPattern() returns {@code true} before
     * calling this method.
     *
     * @return pattern
     * @throws IllegalStateException if no pattern is set
     */
    TropicalFish.Pattern getPattern();

    /**
     * Sets the fish's pattern.
     *
     * @param pattern new pattern
     */
    void setPattern(TropicalFish.Pattern pattern);

    /**
     * Checks for the existence of a pattern.
     *
     * @return if there is a pattern
     */
    boolean hasPattern();

    /**
     * Checks for the existence of a body color.
     *
     * @return if there is a body color
     */
    boolean hasBodyColor();

    /**
     * Checks for the existence of a pattern color.
     *
     * @return if there is a pattern color
     */
    boolean hasPatternColor();

    /**
     * Checks for the existence of a variant tag indicating a specific fish will be
     * spawned.
     *
     * @return if there is a variant
     * @deprecated the variant tag is no longer used and instead split into its own set of components
     */
    @Deprecated(forRemoval = true, since = "1.21.10")
    boolean hasVariant();

    @Override
    TropicalFishBucketMeta clone();
}
