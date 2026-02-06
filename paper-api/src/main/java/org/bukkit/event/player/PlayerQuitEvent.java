package org.bukkit.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a player leaves a server
 */
public interface PlayerQuitEvent extends PlayerEvent {

    /**
     * Gets the quit message to send to all online players
     *
     * @return string quit message
     */
    @Nullable Component quitMessage();

    /**
     * Sets the quit message to send to all online players
     *
     * @param quitMessage quit message
     */
    void quitMessage(@Nullable Component quitMessage);

    /**
     * Gets the quit message to send to all online players
     *
     * @return string quit message
     * @deprecated in favour of {@link #quitMessage()}
     */
    @Deprecated
    @Nullable String getQuitMessage();

    /**
     * Sets the quit message to send to all online players
     *
     * @param quitMessage quit message
     * @deprecated in favour of {@link #quitMessage(Component)}
     */
    @Deprecated
    void setQuitMessage(@Nullable String quitMessage);

    // todo javadocs?
    QuitReason getReason();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum QuitReason {

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
