package org.bukkit.craftbukkit.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jspecify.annotations.Nullable;

public class CraftPlayerJoinEvent extends CraftPlayerEvent implements PlayerJoinEvent {

    private @Nullable Component joinMessage;

    public CraftPlayerJoinEvent(final Player player, final @Nullable Component joinMessage) {
        super(player);
        this.joinMessage = joinMessage;
    }

    @Override
    public @Nullable Component joinMessage() {
        return this.joinMessage;
    }

    @Override
    public void joinMessage(final @Nullable Component joinMessage) {
        this.joinMessage = joinMessage;
    }

    @Override
    @Deprecated
    public @Nullable String getJoinMessage() {
        return LegacyComponentSerializer.legacySection().serializeOrNull(this.joinMessage);
    }

    @Override
    @Deprecated
    public void setJoinMessage(final @Nullable String joinMessage) {
        this.joinMessage = LegacyComponentSerializer.legacySection().deserializeOrNull(joinMessage);
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerJoinEvent.getHandlerList();
    }
}
