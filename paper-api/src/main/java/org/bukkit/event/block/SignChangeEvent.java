package org.bukkit.event.block;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.block.Block;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Called when a sign is changed by a player.
 * <p>
 * If this event is cancelled, the sign will not be changed.
 */
@NullMarked
public class SignChangeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final List<@Nullable Component> adventure$lines;
    private final Side side;

    private boolean cancelled;

    @ApiStatus.Internal
    public SignChangeEvent(final Block sign, final Player player, final List<@Nullable Component> adventure$lines, Side side) {
        super(sign);
        this.player = player;
        this.adventure$lines = adventure$lines;
        this.side = side;
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public SignChangeEvent(final Block sign, final Player player, final List<@Nullable Component> adventure$lines) {
        this(sign, player, adventure$lines, Side.FRONT);
    }

    @ApiStatus.Internal
    @Deprecated(since = "1.19.4", forRemoval = true)
    public SignChangeEvent(final Block sign, final Player thePlayer, final @Nullable String[] theLines) {
        this(sign, thePlayer, theLines, Side.FRONT);
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public SignChangeEvent(final Block sign, final Player thePlayer, final @Nullable String[] theLines, Side side) {
        super(sign);
        this.player = thePlayer;
        this.adventure$lines = new ArrayList<>();
        for (String theLine : theLines) {
            this.adventure$lines.add(theLine == null ? null : LegacyComponentSerializer.legacySection().deserialize(theLine));
        }
        this.side = side;
    }

    /**
     * Gets the player changing the sign involved in this event.
     *
     * @return the Player involved in this event
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets all the lines of text from the sign involved in this event.
     *
     * @return the String array for the sign's lines new text
     */
    public List<@Nullable Component> lines() {
        return this.adventure$lines;
    }

    /**
     * Gets a single line of text from the sign involved in this event.
     *
     * @param index index of the line to get
     * @return the String containing the line of text associated with the
     *     provided index
     * @throws IndexOutOfBoundsException thrown when the provided index is {@literal > 3
     *     or < 0}
     */
    public @Nullable Component line(int index) throws IndexOutOfBoundsException {
        return this.adventure$lines.get(index);
    }

    /**
     * Sets a single line for the sign involved in this event
     *
     * @param index index of the line to set
     * @param line text to set
     * @throws IndexOutOfBoundsException thrown when the provided index is {@literal > 3
     *     or < 0}
     */
    public void line(int index, @Nullable Component line) throws IndexOutOfBoundsException {
        this.adventure$lines.set(index, line);
    }

    /**
     * Gets all the lines of text from the sign involved in this event.
     *
     * @return the String array for the sign's lines new text
     * @deprecated in favour of {@link #lines()}
     */
    @Deprecated
    public @Nullable String[] getLines() {
        return this.adventure$lines.stream().map(line -> line == null ? null : LegacyComponentSerializer.legacySection().serialize(line)).toArray(String[]::new);
    }

    /**
     * Gets a single line of text from the sign involved in this event.
     *
     * @param index index of the line to get
     * @return the String containing the line of text associated with the
     *     provided index
     * @throws IndexOutOfBoundsException thrown when the provided index is {@literal > 3
     *     or < 0}
     * @deprecated in favour of {@link #line(int)}
     */
    @Nullable
    @Deprecated
    public String getLine(int index) throws IndexOutOfBoundsException {
        Component line = line(index);
        return line == null ? null : LegacyComponentSerializer.legacySection().serialize(line);
    }

    /**
     * Sets a single line for the sign involved in this event
     *
     * @param index index of the line to set
     * @param line text to set
     * @throws IndexOutOfBoundsException thrown when the provided index is {@literal > 3
     *     or < 0}
     * @deprecated in favour of {@link #line(int, Component)}
     */
    @Deprecated
    public void setLine(int index, @Nullable String line) throws IndexOutOfBoundsException {
        this.adventure$lines.set(index, line != null ? LegacyComponentSerializer.legacySection().deserialize(line) : null);
    }

    /**
     * Returns which side is changed.
     *
     * @return the affected side of the sign
     */
    public Side getSide() {
        return this.side;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
