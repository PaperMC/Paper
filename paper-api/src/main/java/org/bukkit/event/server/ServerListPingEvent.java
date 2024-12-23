package org.bukkit.event.server;

import com.google.common.base.Preconditions;
import java.net.InetAddress;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.UndefinedNullability;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a server list ping is coming in. Displayed players can be
 * checked and removed by {@link #iterator() iterating} over this event.
 * <br>
 * <b>Note:</b> The players in {@link #iterator()} will not be shown in the
 * server info if {@link Bukkit#getHideOnlinePlayers()} is true.
 *
 * @since 1.0.0
 */
public class ServerListPingEvent extends ServerEvent implements Iterable<Player> {
    private static final int MAGIC_PLAYER_COUNT = Integer.MIN_VALUE;
    private static final HandlerList handlers = new HandlerList();
    private final String hostname;
    private final InetAddress address;
    private net.kyori.adventure.text.Component motd; // Paper
    private final int numPlayers;
    private int maxPlayers;

    public ServerListPingEvent(@NotNull final String hostname, @NotNull final InetAddress address, @NotNull final String motd, final int numPlayers, final int maxPlayers) {
        super(true);
        Preconditions.checkArgument(numPlayers >= 0, "Cannot have negative number of players online", numPlayers);
        this.hostname = hostname;
        this.address = address;
        this.motd = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(motd); // Paper
        this.numPlayers = numPlayers;
        this.maxPlayers = maxPlayers;
    }

    /**
     * This constructor is intended for implementations that provide the
     * {@link #iterator()} method, thus provided the {@link #getNumPlayers()}
     * count.
     *
     * @param hostname The hostname that was used to connect to the server
     * @param address the address of the pinger
     * @param motd the message of the day
     * @param maxPlayers the max number of players
     * @deprecated in favour of {@link #ServerListPingEvent(String, java.net.InetAddress, net.kyori.adventure.text.Component, int)}
     */
    @Deprecated // Paper
    protected ServerListPingEvent(@NotNull final String hostname, @NotNull final InetAddress address, @NotNull final String motd, final int maxPlayers) {
        super(true);
        this.numPlayers = MAGIC_PLAYER_COUNT;
        this.hostname = hostname;
        this.address = address;
        this.motd = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(motd); // Paper
        this.maxPlayers = maxPlayers;
    }
    // Paper start
    @Deprecated
    public ServerListPingEvent(@NotNull final InetAddress address, @NotNull final net.kyori.adventure.text.Component motd, final int numPlayers, final int maxPlayers) {
        this("", address, motd, numPlayers, maxPlayers);
    }
    public ServerListPingEvent(@NotNull final String hostname, @NotNull final InetAddress address, @NotNull final net.kyori.adventure.text.Component motd, final int numPlayers, final int maxPlayers) {
        super(true);
        Preconditions.checkArgument(numPlayers >= 0, "Cannot have negative number of players online (%s)", numPlayers);
        this.hostname = hostname;
        this.address = address;
        this.motd = motd;
        this.numPlayers = numPlayers;
        this.maxPlayers = maxPlayers;
    }
    /**
     * This constructor is intended for implementations that provide the
     * {@link #iterator()} method, thus provided the {@link #getNumPlayers()}
     * count.
     *
     * @param address the address of the pinger
     * @param motd the message of the day
     * @param maxPlayers the max number of players
     * @deprecated in favour of {@link #ServerListPingEvent(String, java.net.InetAddress, net.kyori.adventure.text.Component, int)}
     */
    @Deprecated
    protected ServerListPingEvent(@NotNull final InetAddress address, @NotNull final net.kyori.adventure.text.Component motd, final int maxPlayers) {
        this("", address, motd, maxPlayers);
    }

    /**
     * This constructor is intended for implementations that provide the
     * {@link #iterator()} method, thus provided the {@link #getNumPlayers()}
     * count.
     *
     * @param hostname The hostname that was used to connect to the server
     * @param address the address of the pinger
     * @param motd the message of the day
     * @param maxPlayers the max number of players
     */
    protected ServerListPingEvent(final @NotNull String hostname, final @NotNull InetAddress address, final net.kyori.adventure.text.@NotNull Component motd, final int maxPlayers) {
        this.numPlayers = MAGIC_PLAYER_COUNT;
        this.hostname = hostname;
        this.address = address;
        this.motd = motd;
        this.maxPlayers = maxPlayers;
    }
    /**
     * Get the message of the day message.
     *
     * @return the message of the day
     * @since 1.16.5
     */
    public net.kyori.adventure.text.@NotNull Component motd() {
        return motd;
    }
    /**
     * Change the message of the day message.
     *
     * @param motd the message of the day
     * @since 1.16.5
     */
    public void motd(net.kyori.adventure.text.@NotNull Component motd) {
        this.motd = motd;
    }
    // Paper end

    /**
     * Gets the hostname that the player used to connect to the server, or
     * blank if unknown
     *
     * @return The hostname
     * @since 1.19.2
     */
    @NotNull
    public String getHostname() {
        return hostname;
    }

    /**
     * Get the address the ping is coming from.
     *
     * @return the address
     */
    @NotNull
    public InetAddress getAddress() {
        return address;
    }

    /**
     * Get the message of the day message.
     *
     * @return the message of the day
     * @deprecated in favour of {@link #motd()}
     */
    @NotNull
    @Deprecated // Paper
    public String getMotd() {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(this.motd); // Paper
    }

    /**
     * Change the message of the day message.
     *
     * @param motd the message of the day
     * @deprecated in favour of {@link #motd(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setMotd(@NotNull String motd) {
        this.motd = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(motd); // Paper
    }

    /**
     * Get the number of players sent.
     *
     * @return the number of players
     */
    public int getNumPlayers() {
        int numPlayers = this.numPlayers;
        if (numPlayers == MAGIC_PLAYER_COUNT) {
            numPlayers = 0;
            for (@SuppressWarnings("unused") final Player player : this) {
                numPlayers++;
            }
        }
        return numPlayers;
    }

    /**
     * Get the maximum number of players sent.
     *
     * @return the maximum number of players
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Gets whether the server needs to send a preview of the chat to the
     * client.
     *
     * @return true if chat preview is enabled, false otherwise
     * @deprecated chat previews have been removed
     * @since 1.19
     */
    @Deprecated(since = "1.19.3")
    public boolean shouldSendChatPreviews() {
        return false;
    }

    /**
     * Set the maximum number of players sent.
     *
     * @param maxPlayers the maximum number of player
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * Sets the server-icon sent to the client.
     *
     * @param icon the icon to send to the client
     * @throws IllegalArgumentException if the {@link CachedServerIcon} is not
     *     created by the caller of this event; null may be accepted for some
     *     implementations
     * @throws UnsupportedOperationException if the caller of this event does
     *     not support setting the server icon
     * @since 1.7.2
     */
    public void setServerIcon(@UndefinedNullability("implementation dependent") CachedServerIcon icon) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @since 1.1.0
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Calling the {@link Iterator#remove()} method will force that particular
     * player to not be displayed on the player list, decrease the size
     * returned by {@link #getNumPlayers()}, and will not be returned again by
     * any new iterator.
     * <br>
     * <b>Note:</b> The players here will not be shown in the server info if
     * {@link Bukkit#getHideOnlinePlayers()} is true.
     *
     * @throws UnsupportedOperationException if the caller of this event does
     *     not support removing players
     * @deprecated the Iterable interface will be removed at some point
     */
    @NotNull
    @Override
    @Deprecated(forRemoval = true, since = "1.20.6")
    public Iterator<Player> iterator() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
