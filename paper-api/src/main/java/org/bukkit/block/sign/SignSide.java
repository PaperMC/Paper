package org.bukkit.block.sign;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.material.Colorable;

/**
 * Represents a side of a sign.
 */
public interface SignSide extends Colorable {

    /**
     * Gets all the lines of text currently on the sign.
     *
     * @return List of components containing each line of text
     */
    List<Component> lines();

    /**
     * Gets the line of text at the specified index.
     * <p>
     * For example, getLine(0) will return the first line of text.
     *
     * @param index Line number to get the text from, starting at 0
     * @return Text on the given line
     * @throws IndexOutOfBoundsException Thrown when the line does not exist
     */
    Component line(int index) throws IndexOutOfBoundsException;

    /**
     * Sets the line of text at the specified index.
     * <p>
     * For example, setLine(0, "Line One") will set the first line of text to
     * "Line One".
     *
     * @param index Line number to set the text at, starting from 0
     * @param line  New text to set at the specified index
     * @throws IndexOutOfBoundsException If the index is out of the range 0..3
     */
    void line(int index, Component line) throws IndexOutOfBoundsException;

    /**
     * Gets all the lines of text currently on this side of the sign.
     *
     * @return Array of Strings containing each line of text
     * @deprecated in favour of {@link #lines()}
     */
    @Deprecated
    String[] getLines();

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
    @Deprecated
    String getLine(int index) throws IndexOutOfBoundsException;

    /**
     * Sets the line of text at the specified index on this side of the sign.
     * <p>
     * For example, setLine(0, "Line One") will set the first line of text to
     * "Line One".
     *
     * @param index Line number to set the text at, starting from 0
     * @param line  New text to set at the specified index
     * @throws IndexOutOfBoundsException If the index is out of the range 0..3
     * @deprecated in favour of {@link #line(int, Component)}
     */
    @Deprecated
    void setLine(int index, String line) throws IndexOutOfBoundsException;

    /**
     * Gets whether this side of the sign has glowing text.
     *
     * @return if this side of the sign has glowing text
     */
    boolean isGlowingText();

    /**
     * Sets whether this side of the sign has glowing text.
     *
     * @param glowing if this side of the sign has glowing text
     */
    void setGlowingText(boolean glowing);
}
