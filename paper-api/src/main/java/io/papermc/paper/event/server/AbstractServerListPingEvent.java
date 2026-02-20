package io.papermc.paper.event.server;

import net.kyori.adventure.text.Component;
import org.bukkit.event.server.ServerEvent;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.net.InetAddress;
import java.util.UUID;

public abstract class AbstractServerListPingEvent extends ServerEvent {

    protected static final int MAGIC_PLAYER_COUNT = Integer.MIN_VALUE;

    private final String hostname;
    private final InetAddress address;
    protected final int numPlayers;

    private Component motd;
    private int maxPlayers;

    @ApiStatus.Internal
    protected AbstractServerListPingEvent(@NotNull final String hostname, @NotNull final InetAddress address, @NotNull final Component motd, final int numPlayers, final int maxPlayers) {
        super(true);
        this.hostname = hostname;
        this.address = address;
        this.motd = motd;
        this.numPlayers = numPlayers;
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
     * Get the number of players sent.
     *
     * @return the number of players
     */
    public abstract int getNumPlayers();

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
     * Sets the server-icon sent to the client.
     *
     * @param icon the icon to send to the client
     * @throws IllegalArgumentException if the {@link CachedServerIcon} is not
     *     created by the caller of this event; {@code null} may be accepted for some
     *     implementations
     * @throws UnsupportedOperationException if the caller of this event does
     *     not support setting the server icon
     */
    public abstract void setServerIcon(CachedServerIcon icon) throws IllegalArgumentException, UnsupportedOperationException;


    /**
     * Represents a player that will be displayed in the player sample of the server list.
     *
     * @param name name of the listed player
     * @param id   UUID of the listed player
     */
    public record ListedPlayerInfo(@NotNull String name, @NotNull UUID id) {
    }

}
