package org.bukkit.inventory.meta;

import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a bucket of tropical fish.
 */
public interface TropicalFishBucketMeta extends ItemMeta {

    /**
     * Gets the color of the fish's pattern.
     *
     * @return pattern color
     */
    @NotNull
    DyeColor getPatternColor();

    /**
     * Sets the color of the fish's pattern.
     *
     * @param color pattern color
     */
    void setPatternColor(@NotNull DyeColor color);

    /**
     * Gets the color of the fish's body.
     *
     * @return pattern color
     */
    @NotNull
    DyeColor getBodyColor();

    /**
     * Sets the color of the fish's body.
     *
     * @param color body color
     */
    void setBodyColor(@NotNull DyeColor color);

    /**
     * Gets the fish's pattern.
     *
     * @return pattern
     */
    @NotNull
    TropicalFish.Pattern getPattern();

    /**
     * Sets the fish's pattern.
     *
     * @param pattern new pattern
     */
    void setPattern(@NotNull TropicalFish.Pattern pattern);

    /**
     * Checks for existence of a variant tag indicating a specific fish will be
     * spawned.
     *
     * @return if there is a variant
     * @deprecated The colors and pattern are separate components now
     */
    @Deprecated(forRemoval = true, since = "1.21.10")
    boolean hasVariant();

    @Override
    @NotNull
    TropicalFishBucketMeta clone();
}
