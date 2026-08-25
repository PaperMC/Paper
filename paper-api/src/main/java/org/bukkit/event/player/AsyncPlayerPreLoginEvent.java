package org.bukkit.event.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.connection.PlayerLoginConnection;
import java.net.InetAddress;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Stores details for players attempting to log in.
 * <p>
 * This event is asynchronous, and not run using main thread.
 * <p>
 * This event is fired after the server has successfully completed
 * Mojang authentication. The event is still fired if the server is in offline mode.
 * <p>
 * When this event is fired, the player's locale is not
 * available. Therefore, any translatable component will be
 * rendered with the default locale, {@link java.util.Locale#US}.
 * <p>
 * Consider rendering any translatable yourself with {@link net.kyori.adventure.translation.GlobalTranslator#render}
 * if the client's language is known.
 */
public interface AsyncPlayerPreLoginEvent extends Event {

    /**
     * Gets the current result of the login, as an enum
     *
     * @return Current Result of the login
     */
    Result getLoginResult();

    /**
     * Gets the current result of the login, as an enum
     *
     * @return Current Result of the login
     * @see #getLoginResult()
     * @deprecated This method uses a deprecated enum from {@link
     *     PlayerPreLoginEvent}
     */
    @Deprecated(since = "1.3.2")
    PlayerPreLoginEvent.Result getResult();

    /**
     * Sets the new result of the login, as an enum
     *
     * @param result New result to set
     */
    void setLoginResult(Result result);

    /**
     * Sets the new result of the login, as an enum
     *
     * @param result New result to set
     * @see #setLoginResult(Result)
     * @deprecated This method uses a deprecated enum from {@link
     *     PlayerPreLoginEvent}
     */
    @Deprecated(since = "1.3.2")
    void setResult(PlayerPreLoginEvent.Result result);

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
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     */
    void disallow(Result result, Component message);

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     * @deprecated This method uses a deprecated enum from {@link
     *     PlayerPreLoginEvent}
     * @see #disallow(Result, String)
     */
    @Deprecated
    void disallow(PlayerPreLoginEvent.Result result, Component message);

    /**
     * Gets the current kick message that will be used when the outcome is not allowed
     *
     * @return Current kick message
     * @deprecated in favour of {@link #kickMessage()}
     */
    @Deprecated
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
     * @deprecated in favour of {@link #disallow(AsyncPlayerPreLoginEvent.Result, Component)}
     */
    @Deprecated
    void disallow(Result result, String message);

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     * @see #disallow(Result, String)
     * @deprecated This method uses a deprecated enum from {@link
     *     PlayerPreLoginEvent}
     */
    @Deprecated(since = "1.3.2")
    void disallow(PlayerPreLoginEvent.Result result, String message);

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    String getName();

    /**
     * Gets the player IP address.
     *
     * @return The IP address
     */
    InetAddress getAddress();

    /**
     * Gets the player's unique ID.
     *
     * @return The unique ID
     */
    UUID getUniqueId();

    /**
     * Gets the PlayerProfile of the player logging in
     * @return The Profile
     */
    PlayerProfile getPlayerProfile();

    /**
     * Changes the PlayerProfile the player will login as
     * @param profile The profile to use
     */
    void setPlayerProfile(PlayerProfile profile);

    /**
     * Gets the raw address of the player logging in
     * @return The address
     */
    InetAddress getRawAddress();

    /**
     * Gets the hostname that the player used to connect to the server, or
     * blank if unknown
     *
     * @return The hostname
     */
    String getHostname();

    /**
     * Gets if this connection has been transferred from another server.
     *
     * @return {@code true} if the connection has been transferred
     */
    boolean isTransferred();

    /**
     * Gets the connection for the player logging in.
     * @return connection
     */
    PlayerLoginConnection getConnection();

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
        KICK_OTHER;
    }
}
