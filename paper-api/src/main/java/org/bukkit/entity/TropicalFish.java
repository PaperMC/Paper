package org.bukkit.entity;

import org.bukkit.DyeColor;
import org.jetbrains.annotations.NotNull;

/**
 * Tropical fish.
 */
public interface TropicalFish extends io.papermc.paper.entity.SchoolableFish { // Paper - Schooling Fish API

    /**
     * Gets the color of the fish's pattern.
     *
     * @return pattern color
     */
    @NotNull
    DyeColor getPatternColor();

    /**
     * Sets the color of the fish's pattern
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
     * Sets the color of the fish's body
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
    Pattern getPattern();

    /**
     * Sets the fish's pattern
     *
     * @param pattern new pattern
     */
    void setPattern(@NotNull Pattern pattern);

    /**
     * Enumeration of all different fish patterns. Refer to the
     * <a href="https://minecraft.wiki/w/Fish">Minecraft Wiki</a>
     * for pictures.
     */
    public static enum Pattern {

        // Start generate - TropicalFishPattern
        // @GeneratedFrom 1.21.6-pre1
        KOB,
        SUNSTREAK,
        SNOOPER,
        DASHER,
        BRINELY,
        SPOTTY,
        FLOPPER,
        STRIPEY,
        GLITTER,
        BLOCKFISH,
        BETTY,
        CLAYFISH;
        // End generate - TropicalFishPattern
    }
}
