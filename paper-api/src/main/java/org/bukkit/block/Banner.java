package org.bukkit.block;

import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured state of a banner.
 */
public interface Banner extends TileState {

    /**
     * Returns the base color for this banner
     *
     * @return the base color
     */
    @NotNull
    DyeColor getBaseColor();

    /**
     * Sets the base color for this banner.
     * <b>Only valid for shield pseudo banners, otherwise base depends on block
     * type</b>
     *
     * @param color the base color
     */
    void setBaseColor(@NotNull DyeColor color);

    /**
     * Returns a list of patterns on this banner
     *
     * @return the patterns
     */
    @NotNull
    List<Pattern> getPatterns();

    /**
     * Sets the patterns used on this banner
     *
     * @param patterns the new list of patterns
     */
    void setPatterns(@NotNull List<Pattern> patterns);

    /**
     * Adds a new pattern on top of the existing
     * patterns
     *
     * @param pattern the new pattern to add
     */
    void addPattern(@NotNull Pattern pattern);

    /**
     * Returns the pattern at the specified index
     *
     * @param i the index
     * @return the pattern
     */
    @NotNull
    Pattern getPattern(int i);

    /**
     * Removes the pattern at the specified index
     *
     * @param i the index
     * @return the removed pattern
     */
    @NotNull
    Pattern removePattern(int i);

    /**
     * Sets the pattern at the specified index
     *
     * @param i       the index
     * @param pattern the new pattern
     */
    void setPattern(int i, @NotNull Pattern pattern);

    /**
     * Returns the number of patterns on this
     * banner
     *
     * @return the number of patterns
     */
    int numberOfPatterns();
}
