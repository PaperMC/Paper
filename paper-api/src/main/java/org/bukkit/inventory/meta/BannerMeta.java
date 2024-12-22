package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.block.banner.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * @since 1.8
 */
public interface BannerMeta extends ItemMeta {

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
     * @throws IndexOutOfBoundsException when index is not in [0, numberOfPatterns()) range
     */
    @NotNull
    Pattern getPattern(int i);

    /**
     * Removes the pattern at the specified index
     *
     * @param i the index
     * @return the removed pattern
     * @throws IndexOutOfBoundsException when index is not in [0, numberOfPatterns()) range
     */
    @NotNull
    Pattern removePattern(int i);

    /**
     * Sets the pattern at the specified index
     *
     * @param i       the index
     * @param pattern the new pattern
     * @throws IndexOutOfBoundsException when index is not in [0, numberOfPatterns()) range
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
