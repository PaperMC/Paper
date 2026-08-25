package io.papermc.paper.event.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperPlayerConnectionReconfigureEvent extends CraftEvent implements PlayerConnectionReconfigureEvent {

    private final PlayerConfigurationConnection connection;

    public PaperPlayerConnectionReconfigureEvent(final PlayerConfigurationConnection connection) {
        super(!Bukkit.isPrimaryThread());
        this.connection = connection;
    }

    @Override
    public PlayerConfigurationConnection getConnection() {
        return this.connection;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerConnectionReconfigureEvent.getHandlerList();
    }
}
