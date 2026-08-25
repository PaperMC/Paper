package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperAsyncPlayerConnectionConfigureEvent extends CraftEvent implements AsyncPlayerConnectionConfigureEvent {

    private final PlayerConfigurationConnection connection;

    public PaperAsyncPlayerConnectionConfigureEvent(final PlayerConfigurationConnection connection) {
        super(true);
        this.connection = connection;
    }

    @Override
    public PlayerConfigurationConnection getConnection() {
        return this.connection;
    }

    @Override
    public HandlerList getHandlers() {
        return AsyncPlayerConnectionConfigureEvent.getHandlerList();
    }
}
