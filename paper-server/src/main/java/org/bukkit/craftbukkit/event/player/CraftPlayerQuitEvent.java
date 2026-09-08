package org.bukkit.craftbukkit.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.Nullable;

public class CraftPlayerQuitEvent extends CraftPlayerEvent implements PlayerQuitEvent {

    private final QuitReason reason;
    private @Nullable Component quitMessage;

    public CraftPlayerQuitEvent(final Player player, final @Nullable Component quitMessage, final QuitReason quitReason) {
        super(player);
        this.quitMessage = quitMessage;
        this.reason = quitReason;
    }

    public CraftPlayerQuitEvent(final ServerPlayer player, final @Nullable Component quitMessage, final QuitReason quitReason) {
        this(player.getBukkitEntity(), quitMessage, quitReason);
    }

    @Override
    public @Nullable Component quitMessage() {
        return this.quitMessage;
    }

    @Override
    public void quitMessage(final @Nullable Component quitMessage) {
        this.quitMessage = quitMessage;
    }

    @Override
    @Deprecated
    public @Nullable String getQuitMessage() {
        return LegacyComponentSerializer.legacySection().serializeOrNull(this.quitMessage);
    }

    @Override
    @Deprecated
    public void setQuitMessage(final @Nullable String quitMessage) {
        this.quitMessage = LegacyComponentSerializer.legacySection().deserializeOrNull(quitMessage);
    }

    @Override
    public QuitReason getReason() {
        return this.reason;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerQuitEvent.getHandlerList();
    }
}
