package org.bukkit.event.player;

import io.papermc.paper.event.connection.PlayerConnectionValidateLoginEvent;
import java.net.InetAddress;
import net.kyori.adventure.text.Component;
import org.bukkit.Warning;
import org.bukkit.event.HandlerList;

/**
 * Stores details for players attempting to log in.
 * <br>
 * Note that this event is called <i>early</i> in the player initialization
 * process. It is recommended that most options involving the Player
 * <i>entity</i> be postponed to the {@link PlayerJoinEvent} instead.
 *
 * @deprecated Use {@link PlayerConnectionValidateLoginEvent} to handle pre-login logic
 * (e.g. authentication or ban checks), or {@link io.papermc.paper.event.player.PlayerServerFullCheckEvent} to allow
 * players to bypass the server's maximum player limit.
 * Minecraft triggers this twice internally, using this event skips one of the validation checks done by the server.
 * Additionally, this event causes the full player entity to be created much earlier than it would be in Vanilla,
 * leaving it with mostly dysfunctional methods and state.
 */
@Warning(reason = "Listening to this event causes the player to be created early.")
@Deprecated(since = "1.21.6")
public interface PlayerLoginEvent extends PlayerEvent {

    /**
     * Gets the hostname that the player used to connect to the server, or
     * blank if unknown
     *
     * @return The hostname
     */
    String getHostname();

    /**
     * Gets the {@link InetAddress} for the Player associated with this event.
     * This method is provided as a workaround for player.getAddress()
     * returning {@code null} during PlayerLoginEvent.
     *
     * @return The address for this player. For legacy compatibility, this may
     *     be {@code null}.
     */
    InetAddress getAddress();

    /**
     * Gets the connection address of this player, regardless of whether it has
     * been spoofed or not.
     *
     * @return the player's connection address
     * @see #getAddress()
     */
    InetAddress getRealAddress();

    /**
     * Gets the current result of the login, as an enum
     *
     * @return Current Result of the login
     */
    Result getResult();

    /**
     * Sets the new result of the login, as an enum
     *
     * @param result New result to set
     */
    void setResult(Result result);

    /**
     * Gets the current kick message that will be used when the outcome is not allowed
     *
     * @return Current kick message
     */
    Component kickMessage();

    /**
     * Sets the kick message to display when the outcome is not allowed
     *
     * @param message New kick message
     */
    void kickMessage(Component message);

    /**
     * Gets the current kick message that will be used when the outcome is not allowed
     *
     * @return Current kick message
     * @deprecated in favour of {@link #kickMessage()}
     */
    @Deprecated // Paper
    String getKickMessage();

    /**
     * Sets the kick message to display when the outcome is not allowed
     *
     * @param message New kick message
     * @deprecated in favour of {@link #kickMessage(Component)}
     */
    @Deprecated
    void setKickMessage(String message);

    /**
     * Allows the player to log in
     */
    void allow();

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     * @deprecated in favour of {@link #disallow(Result, Component)}
     */
    @Deprecated
    void disallow(Result result, String message);

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     */
    void disallow(Result result, Component message);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * Basic kick reasons for communicating to plugins
     */
    enum Result {

        /**
         * The player is allowed to log in
         */
        ALLOWED,
        /**
         * The player is not allowed to log in, due to the server being full
         */
        KICK_FULL,
        /**
         * The player is not allowed to log in, due to them being banned
         */
        KICK_BANNED,
        /**
         * The player is not allowed to log in, due to them not being on the
         * white list
         */
        KICK_WHITELIST,
        /**
         * The player is not allowed to log in, for reasons undefined
         */
        KICK_OTHER
    }
}
