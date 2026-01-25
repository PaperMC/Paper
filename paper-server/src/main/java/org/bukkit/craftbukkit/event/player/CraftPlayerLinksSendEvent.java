package org.bukkit.craftbukkit.event.player;

import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.ServerLinks;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerLinksSendEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class CraftPlayerLinksSendEvent extends CraftEvent implements PlayerLinksSendEvent {

    private final ServerLinks links;
    private final PlayerCommonConnection connection;

    public CraftPlayerLinksSendEvent(final PlayerConfigurationConnection connection, final ServerLinks links) {
        this.connection = connection;
        this.links = links;
    }

    @Override
    public PlayerCommonConnection getConnection() {
        return this.connection;
    }

    @Override
    public ServerLinks getLinks() {
        return this.links;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerLinksSendEvent.getHandlerList();
    }
}
