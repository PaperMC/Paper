package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a sign is changed by a player.
 * <p>
 * If a Sign Change event is cancelled, the sign will not be changed.
 */
public class SignChangeEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final Player player;
    private final java.util.List<net.kyori.adventure.text.Component> adventure$lines; // Paper
    private final Side side;

    // Paper start
    public SignChangeEvent(@NotNull final Block theBlock, @NotNull final Player player, @NotNull final java.util.List<net.kyori.adventure.text.Component> adventure$lines, @NotNull Side side) {
        super(theBlock);
        this.player = player;
        this.adventure$lines = adventure$lines;
        this.side = side;
    }

    @Deprecated
    public SignChangeEvent(@NotNull final Block theBlock, @NotNull final Player player, @NotNull final java.util.List<net.kyori.adventure.text.Component> adventure$lines) {
        this(theBlock, player, adventure$lines, Side.FRONT);
    }
    // Paper end

    @Deprecated(since = "1.19.4")
    public SignChangeEvent(@NotNull final Block theBlock, @NotNull final Player thePlayer, @NotNull final String[] theLines) {
        this(theBlock, thePlayer, theLines, Side.FRONT);
    }

    @Deprecated // Paper
    public SignChangeEvent(@NotNull final Block theBlock, @NotNull final Player thePlayer, @NotNull final String[] theLines, @NotNull Side side) {
        super(theBlock);
        this.player = thePlayer;
        // Paper start
        this.adventure$lines = new java.util.ArrayList<>();
        for (String theLine : theLines) {
            this.adventure$lines.add(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(theLine));
        }
        // Paper end
        this.side = side;
    }

    /**
     * Gets the player changing the sign involved in this event.
     *
     * @return the Player involved in this event
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    // Paper start
    /**
     * Gets all of the lines of text from the sign involved in this event.
     *
     * @return the String array for the sign's lines new text
     */
    public @NotNull java.util.List<net.kyori.adventure.text.Component> lines() {
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
    public net.kyori.adventure.text.@Nullable Component line(int index) throws IndexOutOfBoundsException {
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
    public void line(int index, net.kyori.adventure.text.@Nullable Component line) throws IndexOutOfBoundsException {
        this.adventure$lines.set(index, line);
    }
    // Paper end

    /**
     * Gets all of the lines of text from the sign involved in this event.
     *
     * @return the String array for the sign's lines new text
     * @deprecated in favour of {@link #lines()}
     */
    @NotNull
    @Deprecated // Paper
    public String[] getLines() {
        return adventure$lines.stream().map(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection()::serialize).toArray(String[]::new); // Paper
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
    @Deprecated // Paper
    public String getLine(int index) throws IndexOutOfBoundsException {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(this.adventure$lines.get(index)); // Paper
    }

    /**
     * Sets a single line for the sign involved in this event
     *
     * @param index index of the line to set
     * @param line text to set
     * @throws IndexOutOfBoundsException thrown when the provided index is {@literal > 3
     *     or < 0}
     * @deprecated in favour of {@link #line(int, net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setLine(int index, @Nullable String line) throws IndexOutOfBoundsException {
        adventure$lines.set(index, line != null ? net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(line) : null); // Paper
    }

    /**
     * Returns which side is changed.
     *
     * @return the affected side of the sign
     */
    @NotNull
    public Side getSide() {
        return side;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
