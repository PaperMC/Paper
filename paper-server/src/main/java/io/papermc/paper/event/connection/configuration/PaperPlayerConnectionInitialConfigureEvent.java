package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperPlayerConnectionInitialConfigureEvent extends CraftEvent implements PlayerConnectionInitialConfigureEvent {

    private final PlayerConfigurationConnection connection;

    public PaperPlayerConnectionInitialConfigureEvent(final PlayerConfigurationConnection connection) {
        super(!Bukkit.isPrimaryThread());
        this.connection = connection;
    }

    @Override
    public PlayerConfigurationConnection getConnection() {
        return this.connection;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerConnectionInitialConfigureEvent.getHandlerList();
    }
}
