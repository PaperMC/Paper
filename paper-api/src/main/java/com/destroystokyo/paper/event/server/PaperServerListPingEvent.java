package com.destroystokyo.paper.event.server;

import com.destroystokyo.paper.network.StatusClient;
import com.destroystokyo.paper.profile.PlayerProfile;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;
import org.jspecify.annotations.Nullable;

/**
 * Extended version of {@link ServerListPingEvent} that allows full control
 * of the response sent to the client.
 */
public interface PaperServerListPingEvent extends ServerListPingEvent, Cancellable {

    /**
     * Returns the {@link StatusClient} pinging the server.
     *
     * @return The client
     */
    StatusClient getClient();

    /**
     * {@inheritDoc}
     *
     * <p>Returns {@code -1} if players are hidden using
     * {@link #shouldHidePlayers()}.</p>
     */
    @Override
    int getNumPlayers();

    /**
     * Sets the number of players displayed in the server list.
     * <p>
     * Note that this won't have any effect if {@link #shouldHidePlayers()}
     * is enabled.
     *
     * @param numPlayers The number of online players
     */
    void setNumPlayers(int numPlayers);

    /**
     * {@inheritDoc}
     * <p>
     * Returns {@code -1} if players are hidden using
     * {@link #shouldHidePlayers()}.
     */
    @Override
    int getMaxPlayers();

    /**
     * Returns whether all player related information is hidden in the server
     * list. This will cause {@link #getNumPlayers()}, {@link #getMaxPlayers()}
     * and {@link #getPlayerSample()} to be skipped in the response.
     * <p>
     * The Vanilla Minecraft client will display the player count as {@code ???}
     * when this option is enabled.
     *
     * @return {@code true} if the player count is hidden
     */
    boolean shouldHidePlayers();

    /**
     * Sets whether all player related information is hidden in the server
     * list. This will cause {@link #getNumPlayers()}, {@link #getMaxPlayers()}
     * and {@link #getPlayerSample()} to be skipped in the response.
     * <p>
     * The Vanilla Minecraft client will display the player count as {@code ???}
     * when this option is enabled.
     *
     * @param hidePlayers {@code true} if the player count should be hidden
     */
    void setHidePlayers(boolean hidePlayers);

    /**
     * Returns a mutable list of {@link ListedPlayerInfo} that will be displayed
     * as online players on the client.
     * <p>
     * The Vanilla Minecraft client will display them when hovering the
     * player count with the mouse.
     *
     * @return The mutable player sample list
     */
    List<ListedPlayerInfo> getListedPlayers();

    /**
     * Returns a mutable list of {@link PlayerProfile} that will be displayed
     * as online players on the client.
     * <p>
     * The Vanilla Minecraft client will display them when hovering the
     * player count with the mouse.
     *
     * @return The mutable player sample list
     * @deprecated Use {@link #getListedPlayers()}, as this does not contain real player profiles
     */
    @Deprecated(forRemoval = true, since = "1.20.6")
    List<PlayerProfile> getPlayerSample();

    /**
     * Returns the version that will be sent as server version on the client.
     *
     * @return The server version
     */
    String getVersion();

    /**
     * Sets the version that will be sent as server version to the client.
     *
     * @param version The server version
     */
    void setVersion(String version);

    /**
     * Returns the protocol version that will be sent as the protocol version
     * of the server to the client.
     *
     * @return The protocol version of the server, or {@code -1} if the server
     * has not finished initialization yet
     */
    int getProtocolVersion();

    /**
     * Sets the protocol version that will be sent as the protocol version
     * of the server to the client.
     *
     * @param protocolVersion The protocol version of the server
     */
    void setProtocolVersion(int protocolVersion);

    /**
     * Gets the server icon sent to the client.
     *
     * @return The icon to send to the client, or {@code null} for none
     */
    @Nullable CachedServerIcon getServerIcon();

    /**
     * Sets the server icon sent to the client.
     *
     * @param icon The icon to send to the client, or {@code null} for none
     */
    @Override
    void setServerIcon(@Nullable CachedServerIcon icon);

    /**
     * {@inheritDoc}
     * <p>
     * Cancelling this event will cause the connection to be closed immediately,
     * without sending a response to the client.
     */
    @Override
    boolean isCancelled();

    /**
     * {@inheritDoc}
     * <p>
     * Cancelling this event will cause the connection to be closed immediately,
     * without sending a response to the client.
     */
    @Override
    void setCancelled(boolean cancel);

    /**
     * {@inheritDoc}
     * <p>
     * <b>Note:</b> For compatibility reasons, this method will return all
     * online players, not just the ones referenced in {@link #getPlayerSample()}.
     * Removing a player will:
     *
     * <ul>
     *     <li>Decrement the online player count (if and only if) the player
     *     count wasn't changed by another plugin before.</li>
     *     <li>Remove all entries from {@link #getPlayerSample()} that refer to
     *     the removed player (based on their {@link UUID}).</li>
     * </ul>
     * @deprecated the Iterable interface will be removed at some point
     */
    @Override
    @Deprecated(forRemoval = true, since = "1.20.6")
    Iterator<Player> iterator();

    /**
     * Represents a player that will be displayed in the player sample of the server list.
     *
     * @param name name of the listed player
     * @param id   UUID of the listed player
     */
    record ListedPlayerInfo(String name, UUID id) {
    }
}
