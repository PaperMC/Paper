package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player leaves a server
 */
public class PlayerQuitEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private net.kyori.adventure.text.Component quitMessage; // Paper
    private final QuitReason reason; // Paper

    @Deprecated // Paper
    public PlayerQuitEvent(@NotNull final Player who, @Nullable final String quitMessage) {
        // Paper start
        this(who, quitMessage, null);
    }
    @Deprecated // Paper
    public PlayerQuitEvent(@NotNull final Player who, @Nullable final String quitMessage, @Nullable QuitReason quitReason) {
        super(who);
        this.quitMessage = quitMessage != null ? net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(quitMessage) : null; // Paper
        this.reason = quitReason == null ? QuitReason.DISCONNECTED : quitReason;
    }
    // Paper start
    @Deprecated
    public PlayerQuitEvent(@NotNull final Player who, @Nullable final net.kyori.adventure.text.Component quitMessage) {
        this(who, quitMessage, null);
    }
    public PlayerQuitEvent(@NotNull final Player who, @Nullable final net.kyori.adventure.text.Component quitMessage, @Nullable QuitReason quitReason) {
        super(who);
        this.quitMessage = quitMessage;
        this.reason = quitReason == null ? QuitReason.DISCONNECTED : quitReason;
    }

    /**
     * Gets the quit message to send to all online players
     *
     * @return string quit message
     */
    public net.kyori.adventure.text.@Nullable Component quitMessage() {
        return quitMessage;
    }

    /**
     * Sets the quit message to send to all online players
     *
     * @param quitMessage quit message
     */
    public void quitMessage(net.kyori.adventure.text.@Nullable Component quitMessage) {
        this.quitMessage = quitMessage;
    }
    // Paper end

    /**
     * Gets the quit message to send to all online players
     *
     * @return string quit message
     * @deprecated in favour of {@link #quitMessage()}
     */
    @Nullable
    @Deprecated // Paper
    public String getQuitMessage() {
        return this.quitMessage == null ? null : net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(this.quitMessage); // Paper
    }

    /**
     * Sets the quit message to send to all online players
     *
     * @param quitMessage quit message
     * @deprecated in favour of {@link #quitMessage(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setQuitMessage(@Nullable String quitMessage) {
        this.quitMessage = quitMessage != null ? net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(quitMessage) : null; // Paper
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

    // Paper start
    @NotNull
    public QuitReason getReason() {
        return this.reason;
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
        ERRONEOUS_STATE,
    }
    // Paper end
}
