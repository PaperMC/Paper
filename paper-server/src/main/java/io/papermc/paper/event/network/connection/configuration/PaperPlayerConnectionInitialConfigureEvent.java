package io.papermc.paper.event.network.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.event.connection.configuration.PlayerConnectionInitialConfigureEvent;
import io.papermc.paper.event.network.connection.PaperConnectionEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class PaperPlayerConnectionInitialConfigureEvent extends PaperConnectionEvent implements PlayerConnectionInitialConfigureEvent {

    public PaperPlayerConnectionInitialConfigureEvent(final PlayerConfigurationConnection connection) {
        super(connection, !Bukkit.isPrimaryThread());
    }

    @Override
    public PlayerConfigurationConnection getConnection() {
        return (PlayerConfigurationConnection) this.connection;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerConnectionInitialConfigureEvent.getHandlerList();
    }
}
