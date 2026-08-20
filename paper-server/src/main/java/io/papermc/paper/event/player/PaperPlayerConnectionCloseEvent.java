package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import java.net.InetAddress;
import java.util.UUID;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperPlayerConnectionCloseEvent extends CraftEvent implements PlayerConnectionCloseEvent {

    private final UUID playerUniqueId;
    private final String playerName;
    private final InetAddress ipAddress;

    public PaperPlayerConnectionCloseEvent(final UUID playerUniqueId, final String playerName, final InetAddress ipAddress, final boolean async) {
        super(async);
        this.playerUniqueId = playerUniqueId;
        this.playerName = playerName;
        this.ipAddress = ipAddress;
    }

    @Override
    public UUID getPlayerUniqueId() {
        return this.playerUniqueId;
    }

    @Override
    public String getPlayerName() {
        return this.playerName;
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
