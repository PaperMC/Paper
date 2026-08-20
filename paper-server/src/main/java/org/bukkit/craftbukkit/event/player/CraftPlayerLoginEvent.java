package org.bukkit.craftbukkit.event.player;

import java.net.InetAddress;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerLoginEvent;

public class CraftPlayerLoginEvent extends CraftPlayerEvent implements PlayerLoginEvent {

    private final String hostname;
    private final InetAddress address;
    private final InetAddress realAddress;
    private PlayerLoginEvent.Result result = PlayerLoginEvent.Result.ALLOWED;
    private Component message = Component.empty();

    public CraftPlayerLoginEvent(final Player player, final String hostname, final InetAddress address, final InetAddress realAddress) {
        super(player);
        this.hostname = hostname;
        this.address = address;
        this.realAddress = realAddress;
    }

    @Override
    public String getHostname() {
        return this.hostname;
    }

    @Override
    public InetAddress getAddress() {
        return this.address;
    }

    @Override
    public InetAddress getRealAddress() {
        return this.realAddress;
    }

    @Override
    public PlayerLoginEvent.Result getResult() {
        return this.result;
    }

    @Override
    public void setResult(final PlayerLoginEvent.Result result) {
        this.result = result;
    }

    @Override
    public Component kickMessage() {
        return this.message;
    }

    @Override
    public void kickMessage(final Component message) {
        this.message = message;
    }

    @Override
    public String getKickMessage() {
        return LegacyComponentSerializer.legacySection().serialize(this.message);
    }

    @Override
    public void setKickMessage(final String message) {
        this.message = LegacyComponentSerializer.legacySection().deserialize(message);
    }

    @Override
    public void allow() {
        this.result = PlayerLoginEvent.Result.ALLOWED;
        this.message = Component.empty();
    }

    @Override
    public void disallow(final PlayerLoginEvent.Result result, final String message) {
        this.disallow(result, LegacyComponentSerializer.legacySection().deserialize(message));
    }

    @Override
    public void disallow(final PlayerLoginEvent.Result result, final Component message) {
        this.result = result;
        this.message = message;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerLoginEvent.getHandlerList();
    }
}
