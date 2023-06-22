package org.bukkit.block;

import org.bukkit.DyeColor;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.material.Colorable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured state of either a SignPost or a WallSign.
 */
public interface Sign extends TileState, Colorable {

    /**
     * Gets all the lines of text currently on the {@link Side#FRONT} of this sign.
     *
     * @return Array of Strings containing each line of text
     * @deprecated  A sign may have multiple writable sides now. Use {@link Sign#getSide(Side)} and {@link SignSide#getLines()}.
     */
    @Deprecated
    @NotNull
    public String[] getLines();

    /**
     * Gets the line of text at the specified index.
     * <p>
     * For example, getLine(0) will return the first line of text on the {@link Side#FRONT}.
     *
     * @param index Line number to get the text from, starting at 0
     * @return Text on the given line
     * @throws IndexOutOfBoundsException Thrown when the line does not exist
     * @deprecated A sign may have multiple writable sides now. Use {@link #getSide(Side)} and {@link SignSide#getLine(int)}.
     */
    @Deprecated
    @NotNull
    public String getLine(int index) throws IndexOutOfBoundsException;

    /**
     * Sets the line of text at the specified index.
     * <p>
     * For example, setLine(0, "Line One") will set the first line of text to
     * "Line One".
     *
     * @param index Line number to set the text at, starting from 0
     * @param line New text to set at the specified index
     * @throws IndexOutOfBoundsException If the index is out of the range 0..3
     * @deprecated A sign may have multiple writable sides now. Use {@link #getSide(Side)} and {@link SignSide#setLine(int, String)}.
     */
    @Deprecated
    public void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException;

    /**
     * Marks whether this sign can be edited by players.
     *
     * @return if this sign is currently editable
     * @deprecated use {@link #isWaxed()} instead
     */
    @Deprecated
    public boolean isEditable();

    /**
     * Marks whether this sign can be edited by players.
     *
     * @param editable if this sign is currently editable
     * @deprecated use {@link #setWaxed(boolean)} instead
     */
    @Deprecated
    public void setEditable(boolean editable);

    /**
     * Gets whether or not this sign has been waxed. If a sign has been waxed, it
     * cannot be edited by a player.
     *
     * @return if this sign is waxed
     */
    public boolean isWaxed();

    /**
     * Sets whether or not this sign has been waxed. If a sign has been waxed, it
     * cannot be edited by a player.
     *
     * @param waxed if this sign is waxed
     */
    public void setWaxed(boolean waxed);

    /**
     * Gets whether this sign has glowing text. Only affects the {@link Side#FRONT}.
     *
     * @return if this sign has glowing text
     * @deprecated A sign may have multiple writable sides now. Use {@link #getSide(Side)} and {@link SignSide#isGlowingText()}.
     */
    @Deprecated
    public boolean isGlowingText();

    /**
     * Sets whether this sign has glowing text. Only affects the {@link Side#FRONT}.
     *
     * @param glowing if this sign has glowing text
     * @deprecated A sign may have multiple writable sides now. Use {@link #getSide(Side)} and {@link SignSide#setGlowingText(boolean)}.
     */
    @Deprecated
    public void setGlowingText(boolean glowing);

    /**
     * {@inheritDoc}
     *
     * @deprecated A sign may have multiple writable sides now. Use {@link #getSide(Side)} and {@link SignSide#getColor()}.
     */
    @NotNull
    @Override
    @Deprecated
    public DyeColor getColor();

    /**
     * {@inheritDoc}
     *
     * @deprecated A sign may have multiple writable sides now. Use {@link #getSide(Side)} and {@link SignSide#setColor(org.bukkit.DyeColor)}.
     */
    @Override
    @Deprecated
    public void setColor(@NotNull DyeColor color);

    /**
     * Return the side of the sign.
     *
     * @param side the side of the sign
     * @return the selected side of the sign
     */
    @NotNull
    public SignSide getSide(@NotNull Side side);
}
