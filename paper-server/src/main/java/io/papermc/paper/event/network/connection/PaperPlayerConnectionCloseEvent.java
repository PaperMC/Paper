package io.papermc.paper.event.network.connection;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.connection.PlayerConnection;
import io.papermc.paper.connection.PlayerLoginConnection;
import java.net.InetAddress;
import java.util.UUID;
import org.bukkit.event.HandlerList;

public class PaperPlayerConnectionCloseEvent extends PaperConnectionEvent implements PlayerConnectionCloseEvent {

    private final InetAddress ipAddress;

    public PaperPlayerConnectionCloseEvent(final PlayerConnection connection, final InetAddress ipAddress, final boolean async) {
        super(connection, async);
        this.ipAddress = ipAddress;
    }

    @Override
    public UUID getPlayerUniqueId() {
        return switch (this.connection) {
            case final PlayerCommonConnection commonConnection -> commonConnection.getProfile().getId();
            case final PlayerLoginConnection loginConnection -> loginConnection.getAuthenticatedProfile().getId(); /* Should be non-null at this stage */
            default -> throw new IllegalStateException("Unexpected state: " + this.connection.getClass());
        };
    }

    @Override
    public String getPlayerName() {
        return switch (this.connection) {
            case final PlayerCommonConnection commonConnection -> commonConnection.getProfile().getName();
            case final PlayerLoginConnection loginConnection -> loginConnection.getAuthenticatedProfile().getName(); /* Should be non-null at this stage */
            default -> throw new IllegalStateException("Unexpected state: " + this.connection.getClass());
        };
    }

    @Override
    public InetAddress getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerConnectionCloseEvent.getHandlerList();
    }
}
