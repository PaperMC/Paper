package io.papermc.paper.event.network.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.event.connection.configuration.AsyncPlayerConnectionConfigureEvent;
import io.papermc.paper.event.network.connection.PaperConnectionEvent;
import org.bukkit.event.HandlerList;

public class PaperAsyncPlayerConnectionConfigureEvent extends PaperConnectionEvent implements AsyncPlayerConnectionConfigureEvent {

    public PaperAsyncPlayerConnectionConfigureEvent(final PlayerConfigurationConnection connection) {
        super(connection, true);
    }

    @Override
    public PlayerConfigurationConnection getConnection() {
        return (PlayerConfigurationConnection) this.connection;
    }

    @Override
    public HandlerList getHandlers() {
        return AsyncPlayerConnectionConfigureEvent.getHandlerList();
    }
}
