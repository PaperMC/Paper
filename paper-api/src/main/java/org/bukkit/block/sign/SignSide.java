package org.bukkit.block.sign;

import org.bukkit.material.Colorable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a side of a sign.
 *
 * @since 1.19.4
 */
public interface SignSide extends Colorable {
    // Paper start
    /**
     * Gets all the lines of text currently on the sign.
     *
     * @return List of components containing each line of text
     */
    @NotNull
    public java.util.List<net.kyori.adventure.text.Component> lines();

    /**
     * Gets the line of text at the specified index.
     * <p>
     * For example, getLine(0) will return the first line of text.
     *
     * @param index Line number to get the text from, starting at 0
     * @throws IndexOutOfBoundsException Thrown when the line does not exist
     * @return Text on the given line
     */
    @NotNull
    public net.kyori.adventure.text.Component line(int index) throws IndexOutOfBoundsException;

    /**
     * Sets the line of text at the specified index.
     * <p>
     * For example, setLine(0, "Line One") will set the first line of text to
     * "Line One".
     *
     * @param index Line number to set the text at, starting from 0
     * @param line New text to set at the specified index
     * @throws IndexOutOfBoundsException If the index is out of the range 0..3
     */
    public void line(int index, net.kyori.adventure.text.@NotNull Component line) throws IndexOutOfBoundsException;
    // Paper end

    /**
     * Gets all the lines of text currently on this side of the sign.
     *
     * @return Array of Strings containing each line of text
     * @deprecated in favour of {@link #lines()}
     */
    @NotNull
    @Deprecated // Paper
    public String[] getLines();

    /**
     * Gets the line of text at the specified index on this side of the sign.
     * <p>
     * For example, getLine(0) will return the first line of text.
     *
     * @param index Line number to get the text from, starting at 0
     * @return Text on the given line
     * @throws IndexOutOfBoundsException Thrown when the line does not exist
     * @deprecated in favour of {@link #line(int)}
     */
    @NotNull
    @Deprecated // Paper
    public String getLine(int index) throws IndexOutOfBoundsException;

    /**
     * Sets the line of text at the specified index on this side of the sign.
     * <p>
     * For example, setLine(0, "Line One") will set the first line of text to
     * "Line One".
     *
     * @param index Line number to set the text at, starting from 0
     * @param line New text to set at the specified index
     * @throws IndexOutOfBoundsException If the index is out of the range 0..3
     * @deprecated in favour of {@link #line(int, net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException;

    /**
     * Gets whether this side of the sign has glowing text.
     *
     * @return if this side of the sign has glowing text
     */
    public boolean isGlowingText();

    /**
     * Sets whether this side of the sign has glowing text.
     *
     * @param glowing if this side of the sign has glowing text
     */
    public void setGlowingText(boolean glowing);
}
