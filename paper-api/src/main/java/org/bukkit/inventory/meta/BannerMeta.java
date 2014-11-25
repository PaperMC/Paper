package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;

public interface BannerMeta extends ItemMeta {

    /**
     * Returns the base color for this banner
     *
     * @return the base color
     */
    DyeColor getBaseColor();

    /**
     * Sets the base color for this banner
     *
     * @param color the base color
     */
    void setBaseColor(DyeColor color);

    /**
     * Returns a list of patterns on this banner
     *
     * @return the patterns
     */
    List<Pattern> getPatterns();

    /**
     * Sets the patterns used on this banner
     *
     * @param patterns the new list of patterns
     */
    void setPatterns(List<Pattern> patterns);

    /**
     * Adds a new pattern on top of the existing
     * patterns
     *
     * @param pattern the new pattern to add
     */
    void addPattern(Pattern pattern);

    /**
     * Returns the pattern at the specified index
     *
     * @param i the index
     * @return the pattern
     */
    Pattern getPattern(int i);

    /**
     * Removes the pattern at the specified index
     *
     * @param i the index
     * @return the removed pattern
     */
    Pattern removePattern(int i);

    /**
     * Sets the pattern at the specified index
     *
     * @param i       the index
     * @param pattern the new pattern
     */
    void setPattern(int i, Pattern pattern);

    /**
     * Returns the number of patterns on this
     * banner
     *
     * @return the number of patterns
     */
    int numberOfPatterns();
}
