package org.bukkit.craftbukkit.event.player;

import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.event.network.connection.PaperConnectionEvent;
import org.bukkit.ServerLinks;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerLinksSendEvent;

public class CraftPlayerLinksSendEvent extends PaperConnectionEvent implements PlayerLinksSendEvent {

    private final ServerLinks links;

    public CraftPlayerLinksSendEvent(final PlayerConfigurationConnection connection, final ServerLinks links) {
        super(connection);
        this.links = links;
    }

    @Override
    public PlayerCommonConnection getConnection() {
        return (PlayerCommonConnection) this.connection;
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
