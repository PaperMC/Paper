package org.bukkit.entity;

import org.bukkit.DyeColor;

/**
 * Tropical fish.
 */
public interface TropicalFish extends Fish {

    /**
     * Gets the color of the fish's pattern.
     *
     * @return pattern color
     */
    DyeColor getPatternColor();

    /**
     * Sets the color of the fish's pattern
     *
     * @param color pattern color
     */
    void setPatternColor(DyeColor color);

    /**
     * Gets the color of the fish's body.
     *
     * @return pattern color
     */
    DyeColor getBodyColor();

    /**
     * Sets the color of the fish's body
     *
     * @param color body color
     */
    void setBodyColor(DyeColor color);

    /**
     * Gets the fish's pattern.
     *
     * @return pattern
     */
    Pattern getPattern();

    /**
     * Sets the fish's pattern
     *
     * @param pattern new pattern
     */
    void setPattern(Pattern pattern);

    /**
     * Enumeration of all different fish patterns. Refer to the
     * <a href="https://minecraft.gamepedia.com/Fish_(mob)">Minecraft Wiki</a>
     * for pictures.
     */
    public static enum Pattern {

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
    }
}
