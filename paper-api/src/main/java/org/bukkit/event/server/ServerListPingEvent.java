package org.bukkit.event.server;

import java.net.InetAddress;
import java.util.Iterator;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.UndefinedNullability;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.Contract;

/**
 * Called when a server list ping is coming in. Displayed players can be
 * checked and removed by {@link #iterator() iterating} over this event.
 * <br>
 * <b>Note:</b> The players in {@link #iterator()} will not be shown in the
 * server info if {@link Bukkit#getHideOnlinePlayers()} is {@code true}.
 */
public interface ServerListPingEvent extends ServerEventNew, Iterable<Player> {

    /**
     * Gets the hostname that the player used to connect to the server, or
     * blank if unknown
     *
     * @return The hostname
     */
    String getHostname();

    /**
     * Get the address the ping is coming from.
     *
     * @return the address
     */
    InetAddress getAddress();

    /**
     * Get the message of the day message.
     *
     * @return the message of the day
     */
    Component motd();

    /**
     * Change the message of the day message.
     *
     * @param motd the message of the day
     */
    void motd(Component motd);

    /**
     * Get the message of the day message.
     *
     * @return the message of the day
     * @deprecated in favour of {@link #motd()}
     */
    @Deprecated
    String getMotd();

    /**
     * Change the message of the day message.
     *
     * @param motd the message of the day
     * @deprecated in favour of {@link #motd(Component)}
     */
    @Deprecated
    void setMotd(String motd);

    /**
     * Get the number of players sent.
     *
     * @return the number of players
     */
    int getNumPlayers();

    /**
     * Get the maximum number of players sent.
     *
     * @return the maximum number of players
     */
    int getMaxPlayers();

    /**
     * Set the maximum number of players sent.
     *
     * @param maxPlayers the maximum number of player
     */
    void setMaxPlayers(int maxPlayers);

    /**
     * Gets whether the server needs to send a preview of the chat to the
     * client.
     *
     * @return {@code true} if chat preview is enabled, {@code false} otherwise
     * @deprecated chat previews have been removed
     */
    @Contract("-> false")
    @Deprecated(since = "1.19.3", forRemoval = true)
    default boolean shouldSendChatPreviews() {
        return false;
    }

    /**
     * Sets the server-icon sent to the client.
     *
     * @param icon the icon to send to the client
     * @throws IllegalArgumentException if the {@link CachedServerIcon} is not
     *     created by the caller of this event; {@code null} may be accepted for some
     *     implementations
     * @throws UnsupportedOperationException if the caller of this event does
     *     not support setting the server icon
     */
    void setServerIcon(@UndefinedNullability("implementation dependent") CachedServerIcon icon) throws UnsupportedOperationException;

    /**
     * {@inheritDoc}
     * <p>
     * Calling the {@link Iterator#remove()} method will force that particular
     * player to not be displayed on the player list, decrease the size
     * returned by {@link #getNumPlayers()}, and will not be returned again by
     * any new iterator.
     * <br>
     * <b>Note:</b> The players here will not be shown in the server info if
     * {@link Bukkit#getHideOnlinePlayers()} is {@code true}.
     *
     * @throws UnsupportedOperationException if the caller of this event does
     *     not support removing players
     * @deprecated the Iterable interface will be removed at some point
     */
    @Override
    @Deprecated(forRemoval = true, since = "1.20.6")
    Iterator<Player> iterator() throws UnsupportedOperationException;

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
