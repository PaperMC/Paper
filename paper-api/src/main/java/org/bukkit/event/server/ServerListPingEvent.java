package org.bukkit.event.server;

import com.google.common.base.Preconditions;
import java.net.InetAddress;
import java.util.Iterator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.UndefinedNullability;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a server list ping is coming in. Displayed players can be
 * checked and removed by {@link #iterator() iterating} over this event.
 * <br>
 * <b>Note:</b> The players in {@link #iterator()} will not be shown in the
 * server info if {@link Bukkit#getHideOnlinePlayers()} is {@code true}.
 */
public class ServerListPingEvent extends ServerEvent implements Iterable<Player> {

    private static final int MAGIC_PLAYER_COUNT = Integer.MIN_VALUE;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final String hostname;
    private final InetAddress address;
    private final int numPlayers;
    private Component motd;
    private int maxPlayers;

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public ServerListPingEvent(@NotNull final String hostname, @NotNull final InetAddress address, @NotNull final String motd, final int numPlayers, final int maxPlayers) {
        super(true);
        Preconditions.checkArgument(numPlayers >= 0, "Cannot have negative number of players online", numPlayers);
        this.hostname = hostname;
        this.address = address;
        this.motd = LegacyComponentSerializer.legacySection().deserialize(motd);
        this.numPlayers = numPlayers;
        this.maxPlayers = maxPlayers;
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    protected ServerListPingEvent(@NotNull final String hostname, @NotNull final InetAddress address, @NotNull final String motd, final int maxPlayers) {
        super(true);
        this.numPlayers = MAGIC_PLAYER_COUNT;
        this.hostname = hostname;
        this.address = address;
        this.motd = LegacyComponentSerializer.legacySection().deserialize(motd);
        this.maxPlayers = maxPlayers;
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public ServerListPingEvent(@NotNull final InetAddress address, @NotNull final Component motd, final int numPlayers, final int maxPlayers) {
        this("", address, motd, numPlayers, maxPlayers);
    }

    @ApiStatus.Internal
    public ServerListPingEvent(@NotNull final String hostname, @NotNull final InetAddress address, @NotNull final Component motd, final int numPlayers, final int maxPlayers) {
        super(true);
        this.hostname = hostname;
        this.address = address;
        this.motd = motd;
        this.numPlayers = numPlayers;
        this.maxPlayers = maxPlayers;
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    protected ServerListPingEvent(@NotNull final InetAddress address, @NotNull final Component motd, final int maxPlayers) {
        this("", address, motd, maxPlayers);
    }

    /*
     * This constructor is intended for implementations that provide the
     * {@link #iterator()} method, thus provided the {@link #getNumPlayers()}
     * count.
     */
    @ApiStatus.Internal
    protected ServerListPingEvent(final @NotNull String hostname, final @NotNull InetAddress address, final @NotNull Component motd, final int maxPlayers) {
        this.numPlayers = MAGIC_PLAYER_COUNT;
        this.hostname = hostname;
        this.address = address;
        this.motd = motd;
        this.maxPlayers = maxPlayers;
    }

    /**
     * Gets the hostname that the player used to connect to the server, or
     * blank if unknown
     *
     * @return The hostname
     */
    @NotNull
    public String getHostname() {
        return this.hostname;
    }

    /**
     * Get the address the ping is coming from.
     *
     * @return the address
     */
    @NotNull
    public InetAddress getAddress() {
        return this.address;
    }

    /**
     * Get the message of the day message.
     *
     * @return the message of the day
     */
    public @NotNull Component motd() {
        return this.motd;
    }

    /**
     * Change the message of the day message.
     *
     * @param motd the message of the day
     */
    public void motd(@NotNull Component motd) {
        this.motd = motd;
    }

    /**
     * Get the message of the day message.
     *
     * @return the message of the day
     * @deprecated in favour of {@link #motd()}
     */
    @NotNull
    @Deprecated
    public String getMotd() {
        return LegacyComponentSerializer.legacySection().serialize(this.motd);
    }

    /**
     * Change the message of the day message.
     *
     * @param motd the message of the day
     * @deprecated in favour of {@link #motd(Component)}
     */
    @Deprecated
    public void setMotd(@NotNull String motd) {
        this.motd = LegacyComponentSerializer.legacySection().deserialize(motd);
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
        return this.maxPlayers;
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
     * Gets whether the server needs to send a preview of the chat to the
     * client.
     *
     * @return {@code true} if chat preview is enabled, {@code false} otherwise
     * @deprecated chat previews have been removed
     */
    @Contract("-> false")
    @Deprecated(since = "1.19.3", forRemoval = true)
    public boolean shouldSendChatPreviews() {
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
    public void setServerIcon(@UndefinedNullability("implementation dependent") CachedServerIcon icon) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
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
     * {@link Bukkit#getHideOnlinePlayers()} is {@code true}.
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

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
