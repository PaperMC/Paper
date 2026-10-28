package io.papermc.paper.event.server;


import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Called when a player joins the server and updates their server list.
 * Displayed players can be checked and removed by removing
 * them from {@link #getListedPlayers()}.
 */
public class ServerListPlayerPingEvent extends AbstractServerListPingEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @NonNull List<ListedPlayerInfo> listedPlayers = new ArrayList<>();
    private final @NonNull Player player;

    private @NonNull String version;
    private @Nullable CachedServerIcon favicon;
    private int protocolVersion;
    private int numPlayers;
    private boolean hidePlayers;

    private boolean cancelled;

    @ApiStatus.Internal
    public ServerListPlayerPingEvent(@NonNull Player player, @NonNull Component motd, int numPlayers, int maxPlayers,
                                     @NonNull String version, int protocolVersion, @Nullable CachedServerIcon favicon) {
        super("", player.getConnection().getClientAddress().getAddress(), motd, numPlayers, maxPlayers);
        this.player = player;
        this.numPlayers = numPlayers;
        this.version = version;
        this.protocolVersion = protocolVersion;
        this.setServerIcon(favicon);
    }

    /**
     * Returns the player that is requesting the server list.
     *
     * @return The server version
     */
    public @NonNull Player getPlayer() {
        return this.player;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Returns {@code -1} if players are hidden using
     * {@link #shouldHidePlayers()}.</p>
     */
    @Override
    public int getNumPlayers() {
        if (this.hidePlayers) {
            return -1;
        }

        return this.numPlayers;
    }

    /**
     * Sets the number of players displayed in the server list.
     * <p>
     * Note that this won't have any effect if {@link #shouldHidePlayers()}
     * is enabled.
     *
     * @param numPlayers The number of online players
     */
    public void setNumPlayers(int numPlayers) {
        if (this.numPlayers != numPlayers) {
            this.numPlayers = numPlayers;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns {@code -1} if players are hidden using
     * {@link #shouldHidePlayers()}.
     */
    @Override
    public int getMaxPlayers() {
        if (this.hidePlayers) {
            return -1;
        }

        return super.getMaxPlayers();
    }

    /**
     * Returns whether all player related information is hidden in the server
     * list. This will cause {@link #getNumPlayers()}, {@link #getMaxPlayers()}
     * to be skipped in the response.
     * <p>
     * The Vanilla Minecraft client will display the player count as {@code ???}
     * when this option is enabled.
     *
     * @return {@code true} if the player count is hidden
     */
    public boolean shouldHidePlayers() {
        return this.hidePlayers;
    }

    /**
     * Sets whether all player related information is hidden in the server
     * list. This will cause {@link #getNumPlayers()}, {@link #getMaxPlayers()}
     * to be skipped in the response.
     * <p>
     * The Vanilla Minecraft client will display the player count as {@code ???}
     * when this option is enabled.
     *
     * @param hidePlayers {@code true} if the player count should be hidden
     */
    public void setHidePlayers(boolean hidePlayers) {
        this.hidePlayers = hidePlayers;
    }

    /**
     * Returns a mutable list of {@link ListedPlayerInfo} that will be displayed
     * as online players on the client.
     * <p>
     * The Vanilla Minecraft client will display them when hovering the
     * player count with the mouse.
     *
     * @return The mutable player sample list
     */
    public @NonNull List<ListedPlayerInfo> getListedPlayers() {
        return this.listedPlayers;
    }

    /**
     * Returns the version that will be sent as server version on the client.
     *
     * @return The server version
     */
    public @NonNull String getVersion() {
        return this.version;
    }

    /**
     * Sets the version that will be sent as server version to the client.
     *
     * @param version The server version
     */
    public void setVersion(@NonNull String version) {
        this.version = requireNonNull(version, "version");
    }

    /**
     * Returns the protocol version that will be sent as the protocol version
     * of the server to the client.
     *
     * @return The protocol version of the server, or {@code -1} if the server
     * has not finished initialization yet
     */
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    /**
     * Sets the protocol version that will be sent as the protocol version
     * of the server to the client.
     *
     * @param protocolVersion The protocol version of the server
     */
    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    /**
     * Gets the server icon sent to the client.
     *
     * @return The icon to send to the client, or {@code null} for none
     */
    public @Nullable CachedServerIcon getServerIcon() {
        return this.favicon;
    }

    /**
     * Sets the server icon sent to the client.
     *
     * @param icon The icon to send to the client, or {@code null} for none
     */
    @Override
    public void setServerIcon(@Nullable CachedServerIcon icon) {
        if (icon != null && icon.isEmpty()) {
            // Represent empty icons as null
            icon = null;
        }

        this.favicon = icon;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Cancelling this event will cause the player to not receive
     * an update to the server status.
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Cancelling this event will cause the player to not receive
     * an update to the server status.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NonNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
