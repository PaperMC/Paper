package org.bukkit.block;

import org.bukkit.DyeColor;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.material.Colorable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of either a SignPost or a WallSign.
 */
public interface Sign extends TileState, Colorable {
    // Paper start
    /**
     * Gets all the lines of text currently on the {@link Side#FRONT} of this sign.
     *
     * @return List of components containing each line of text
     * @deprecated A sign may have multiple writable sides now. Use {@link Sign#getSide(Side)} and {@link SignSide#lines()}.
     */
    @NotNull
    @Deprecated
    public java.util.List<net.kyori.adventure.text.Component> lines();

    /**
     * Gets the line of text at the specified index on the {@link Side#FRONT}.
     * <p>
     * For example, getLine(0) will return the first line of text.
     *
     * @param index Line number to get the text from, starting at 0
     * @throws IndexOutOfBoundsException Thrown when the line does not exist
     * @return Text on the given line
     * @deprecated A sign may have multiple writable sides now. Use {@link #getSide(Side)} and {@link SignSide#line(int)}.
     */
    @NotNull
    @Deprecated
    public net.kyori.adventure.text.Component line(int index) throws IndexOutOfBoundsException;

    /**
     * Sets the line of text at the specified index on the {@link Side#FRONT}.
     * <p>
     * For example, setLine(0, "Line One") will set the first line of text to
     * "Line One".
     *
     * @param index Line number to set the text at, starting from 0
     * @param line New text to set at the specified index
     * @throws IndexOutOfBoundsException If the index is out of the range 0..3
     * @deprecated A sign may have multiple writable sides now. Use {@link #getSide(Side)} and {@link SignSide#line(int, net.kyori.adventure.text.Component)}.
     */
    @Deprecated
    public void line(int index, net.kyori.adventure.text.@NotNull Component line) throws IndexOutOfBoundsException;
    // Paper end

    /**
     * Gets all the lines of text currently on the {@link Side#FRONT} of this sign.
     *
     * @return Array of Strings containing each line of text
     * @deprecated  A sign may have multiple writable sides now. Use {@link Sign#getSide(Side)} and {@link SignSide#lines()}.
     */
    @Deprecated(since = "1.20")
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
     * @deprecated A sign may have multiple writable sides now. Use {@link #getSide(Side)} and {@link SignSide#line(int)}.
     */
    @Deprecated(since = "1.20")
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
     * @deprecated A sign may have multiple writable sides now. Use {@link #getSide(Side)} and {@link SignSide#line(int, net.kyori.adventure.text.Component)}.
     */
    @Deprecated(since = "1.20")
    public void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException;

    /**
     * Marks whether this sign can be edited by players.
     *
     * @return if this sign is currently editable
     * @deprecated use {@link #isWaxed()} instead
     */
    @Deprecated(since = "1.20.1")
    public boolean isEditable();

    /**
     * Marks whether this sign can be edited by players.
     *
     * @param editable if this sign is currently editable
     * @deprecated use {@link #setWaxed(boolean)} instead
     */
    @Deprecated(since = "1.20.1")
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
    @Deprecated(since = "1.20")
    public boolean isGlowingText();

    /**
     * Sets whether this sign has glowing text. Only affects the {@link Side#FRONT}.
     *
     * @param glowing if this sign has glowing text
     * @deprecated A sign may have multiple writable sides now. Use {@link #getSide(Side)} and {@link SignSide#setGlowingText(boolean)}.
     */
    @Deprecated(since = "1.20")
    public void setGlowingText(boolean glowing);

    /**
     * {@inheritDoc}
     *
     * @deprecated A sign may have multiple writable sides now. Use {@link #getSide(Side)} and {@link SignSide#getColor()}.
     */
    @NotNull
    @Override
    @Deprecated(since = "1.20")
    public DyeColor getColor();

    /**
     * {@inheritDoc}
     *
     * @deprecated A sign may have multiple writable sides now. Use {@link #getSide(Side)} and {@link SignSide#setColor(org.bukkit.DyeColor)}.
     */
    @Override
    @Deprecated(since = "1.20")
    public void setColor(@NotNull DyeColor color);

    /**
     * Return the side of the sign.
     *
     * @param side the side of the sign
     * @return the selected side of the sign
     */
    @NotNull
    public SignSide getSide(@NotNull Side side);

    /**
     * Gets the side of this sign the given player is currently standing on.
     *
     * @param player the player
     * @return the side the player is standing on
     */
    @NotNull
    public SignSide getTargetSide(@NotNull Player player);

    /**
     * Gets the player that is currently allowed to edit this sign. <br>
     * Edits from other players will be rejected if this value is not null.
     * <br><br>You should prefer {@link #getAllowedEditorUniqueId()} if you don't
     * need the player instance as this method will fetch the player from UUID.
     *
     * @return the player allowed to edit this sign, or null
     */
    @Nullable
    public Player getAllowedEditor();
    // Paper start - More Sign Block API
    /**
     * Gets the allowed editor's UUID.
     * <br>Edits from other players will be rejected if this value is not null.
     *
     * @return the allowed editor's UUID, or null
     */
    @Nullable java.util.UUID getAllowedEditorUniqueId();

    /**
     * Sets the allowed editor's UUID.
     * <br><br><b>Note:</b> the server sets the UUID back to null if the player can't
     * interact with the sign (is either offline or outside the allowed interaction range).
     *
     * @param uuid the allowed editor's UUID
     */
    void setAllowedEditorUniqueId(@Nullable java.util.UUID uuid);

    /**
     * Compute the side facing the specified entity.
     *
     * @param entity the entity
     * @return the side it is facing
     */
    default @NotNull Side getInteractableSideFor(org.bukkit.entity.@NotNull Entity entity) {
        return this.getInteractableSideFor(entity.getLocation());
    }

    /**
     * Compute the side facing the specific position.
     *
     * @param position the position
     * @return the side the position is facing
     */
    default @NotNull Side getInteractableSideFor(io.papermc.paper.math.@NotNull Position position) {
        return this.getInteractableSideFor(position.x(), position.z());
    }

    /**
     * Compute the side facing the specific x and z coordinates.
     *
     * @param x the x coord
     * @param z the z coord
     * @return the side the coordinates are facing
     */
    @NotNull Side getInteractableSideFor(double x, double z);
    // Paper end - More Sign Block API
}
