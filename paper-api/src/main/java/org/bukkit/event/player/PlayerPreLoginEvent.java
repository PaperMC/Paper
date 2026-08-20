package org.bukkit.event.player;

import java.net.InetAddress;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Warning;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Stores details for players attempting to log in
 * <p>
 * When this event is fired, the player's locale is not
 * available. Therefore, any translatable component will be
 * rendered with the default locale, {@link java.util.Locale#US}.
 * <p>
 * Consider rendering any translatable yourself with {@link net.kyori.adventure.translation.GlobalTranslator#render}
 * if the client's language is known.
 *
 * @deprecated This event causes synchronization from the login thread; {@link AsyncPlayerPreLoginEvent} is preferred
 * to keep the secondary threads asynchronous.
 */
@Deprecated(since = "1.3.2")
@Warning(reason = "This event causes a login thread to synchronize with the main thread")
public interface PlayerPreLoginEvent extends Event {

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
    @Deprecated
    String getKickMessage();

    /**
     * Sets the kick message to display when the outcome is not allowed
     *
     * @param message New kick message
     * @deprecated in favour of {@link #kickMessage(Component)}
     */
    @Deprecated
    void setKickMessage(final String message);

    /**
     * Allows the player to log in
     */
    void allow();

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
     * @deprecated in favour of {@link #disallow(org.bukkit.event.player.PlayerPreLoginEvent.Result, Component)}
     */
    @Deprecated
    void disallow(Result result, String message);

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
