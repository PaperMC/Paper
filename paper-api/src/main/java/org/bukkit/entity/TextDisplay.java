package org.bukkit.entity;

import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a text display entity.
 */
public interface TextDisplay extends Display {

    /**
     * Gets the displayed text.
     *
     * @return the displayed text.
     */
    @Nullable
    String getText();

    /**
     * Sets the displayed text.
     *
     * @param text the new text
     */
    void setText(@Nullable String text);

    /**
     * Gets the maximum line width before wrapping.
     *
     * @return the line width
     */
    int getLineWidth();

    /**
     * Sets the maximum line width before wrapping.
     *
     * @param width new line width
     */
    void setLineWidth(int width);

    /**
     * Gets the text background color.
     *
     * @return the background color
     * @deprecated API subject to change
     */
    @Nullable
    @Deprecated
    Color getBackgroundColor();

    /**
     * Sets the text background color.
     *
     * @param color new background color
     * @deprecated API subject to change
     */
    @Deprecated
    void setBackgroundColor(@Nullable Color color);

    /**
     * Gets the text opacity.
     *
     * @return opacity or -1 if not set
     */
    byte getTextOpacity();

    /**
     * Sets the text opacity.
     *
     * @param opacity new opacity or -1 if default
     */
    void setTextOpacity(byte opacity);

    /**
     * Gets if the text is shadowed.
     *
     * @return shadow status
     */
    boolean isShadowed();

    /**
     * Sets if the text is shadowed.
     *
     * @param shadow if shadowed
     */
    void setShadowed(boolean shadow);

    /**
     * Gets if the text is see through.
     *
     * @return see through status
     */
    boolean isSeeThrough();

    /**
     * Sets if the text is see through.
     *
     * @param seeThrough if see through
     */
    void setSeeThrough(boolean seeThrough);

    /**
     * Gets if the text has its default background.
     *
     * @return default background
     */
    boolean isDefaultBackground();

    /**
     * Sets if the text has its default background.
     *
     * @param defaultBackground if default
     */
    void setDefaultBackground(boolean defaultBackground);

    /**
     * Gets the text alignment for this display.
     *
     * @return text alignment
     */
    @NotNull
    TextAlignment getAlignment();

    /**
     * Sets the text alignment for this display.
     *
     * @param alignment new alignment
     */
    void setAlignment(@NotNull TextAlignment alignment);

    /**
     * Represents possible text alignments for this display.
     */
    public enum TextAlignment {

        /**
         * Center aligned text (default).
         */
        CENTER,
        /**
         * Left aligned text.
         */
        LEFT,
        /**
         * Right aligned text.
         */
        RIGHT;
    }
}
