package io.papermc.paper.event.network.connection.configuration;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.event.connection.configuration.PlayerConnectionReconfigureEvent;
import io.papermc.paper.event.network.connection.PaperConnectionEvent;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class PaperPlayerConnectionReconfigureEvent extends PaperConnectionEvent implements PlayerConnectionReconfigureEvent {

    public PaperPlayerConnectionReconfigureEvent(final ServerConfigurationPacketListener packetListener) {
        super(packetListener.paperConnection(), !Bukkit.isPrimaryThread());
    }

    @Override
    public PlayerConfigurationConnection getConnection() {
        return (PlayerConfigurationConnection) this.connection;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerConnectionReconfigureEvent.getHandlerList();
    }
}
