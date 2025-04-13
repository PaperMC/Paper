package org.bukkit.event.player;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.ServerLinks;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when the list of links is sent to the player.
 */
@ApiStatus.Experimental
public class PlayerLinksSendEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ServerLinks links;
    private final PlayerConfigurationConnection configurationConnection;

    @ApiStatus.Internal
    public PlayerLinksSendEvent(@NotNull final PlayerConfigurationConnection connection, @NotNull final ServerLinks links) {
        this.configurationConnection = connection;
        this.links = links;
    }

    /**
     * Gets the connection that received the links.
     * @return connection
     */
    public PlayerConfigurationConnection getConfigurationConnection() {
        return configurationConnection;
    }

    /**
     * Gets the links to be sent, for modification.
     *
     * @return the links
     */
    @NotNull
    public ServerLinks getLinks() {
        return this.links;
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
