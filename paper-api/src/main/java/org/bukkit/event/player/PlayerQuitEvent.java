package org.bukkit.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player leaves a server
 */
public class PlayerQuitEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final QuitReason reason;
    private Component quitMessage;

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public PlayerQuitEvent(@NotNull final Player player, @Nullable final String quitMessage) {
        this(player, quitMessage, null);
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public PlayerQuitEvent(@NotNull final Player player, @Nullable final String quitMessage, @Nullable QuitReason quitReason) {
        super(player);
        this.quitMessage = quitMessage != null ? LegacyComponentSerializer.legacySection().deserialize(quitMessage) : null;
        this.reason = quitReason == null ? QuitReason.DISCONNECTED : quitReason;
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public PlayerQuitEvent(@NotNull final Player player, @Nullable final Component quitMessage) {
        this(player, quitMessage, null);
    }

    @ApiStatus.Internal
    public PlayerQuitEvent(@NotNull final Player player, @Nullable final Component quitMessage, @Nullable QuitReason quitReason) {
        super(player);
        this.quitMessage = quitMessage;
        this.reason = quitReason == null ? QuitReason.DISCONNECTED : quitReason;
    }

    /**
     * Gets the quit message to send to all online players
     *
     * @return string quit message
     */
    public @Nullable Component quitMessage() {
        return this.quitMessage;
    }

    /**
     * Sets the quit message to send to all online players
     *
     * @param quitMessage quit message
     */
    public void quitMessage(@Nullable Component quitMessage) {
        this.quitMessage = quitMessage;
    }

    /**
     * Gets the quit message to send to all online players
     *
     * @return string quit message
     * @deprecated in favour of {@link #quitMessage()}
     */
    @Nullable
    @Deprecated
    public String getQuitMessage() {
        return this.quitMessage == null ? null : LegacyComponentSerializer.legacySection().serialize(this.quitMessage);
    }

    /**
     * Sets the quit message to send to all online players
     *
     * @param quitMessage quit message
     * @deprecated in favour of {@link #quitMessage(Component)}
     */
    @Deprecated
    public void setQuitMessage(@Nullable String quitMessage) {
        this.quitMessage = quitMessage != null ? LegacyComponentSerializer.legacySection().deserialize(quitMessage) : null;
    }

    @NotNull
    public QuitReason getReason() {
        return this.reason;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum QuitReason {

        /**
         * The player left on their own behalf.
         * <p>
         * This does not mean they pressed the disconnect button in their client, but rather that the client severed the
         * connection themselves. This may occur if no keep-alive packet is received on their side, among other things.
         */
        DISCONNECTED,
        /**
         * The player was kicked from the server.
         */
        KICKED,
        /**
         * The player has timed out.
         */
        TIMED_OUT,
        /**
         * The player's connection has entered an erroneous state.
         * <p>
         * Reasons for this may include invalid packets, invalid data, and uncaught exceptions in the packet handler,
         * among others.
         */
        ERRONEOUS_STATE
    }
}
