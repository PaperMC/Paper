package org.bukkit.craftbukkit.event.player;

import java.net.InetAddress;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerPreLoginEvent;

public class CraftPlayerPreLoginEvent extends CraftEvent implements PlayerPreLoginEvent {

    private final String name;
    private final InetAddress ipAddress;
    private final UUID uniqueId;
    private PlayerPreLoginEvent.Result result;
    private Component message;

    public CraftPlayerPreLoginEvent(final String name, final InetAddress ipAddress, final UUID uniqueId) {
        this.result = PlayerPreLoginEvent.Result.ALLOWED;
        this.message = Component.empty();
        this.name = name;
        this.ipAddress = ipAddress;
        this.uniqueId = uniqueId;
    }

    @Override
    public PlayerPreLoginEvent.Result getResult() {
        return this.result;
    }

    @Override
    public void setResult(final PlayerPreLoginEvent.Result result) {
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
        this.result = PlayerPreLoginEvent.Result.ALLOWED;
        this.message = Component.empty();
    }

    @Override
    public void disallow(final PlayerPreLoginEvent.Result result, final Component message) {
        this.result = result;
        this.message = message;
    }

    @Override
    public void disallow(final PlayerPreLoginEvent.Result result, final String message) {
        this.disallow(result, LegacyComponentSerializer.legacySection().deserialize(message));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public InetAddress getAddress() {
        return this.ipAddress;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerPreLoginEvent.getHandlerList();
    }
}
