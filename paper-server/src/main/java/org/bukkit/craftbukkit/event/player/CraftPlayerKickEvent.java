package org.bukkit.craftbukkit.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerKickEvent;

public class CraftPlayerKickEvent extends CraftPlayerEvent implements PlayerKickEvent {

    private Component kickReason;
    private Component leaveMessage;
    private final Cause cause;

    private boolean cancelled;

    public CraftPlayerKickEvent(final Player player, final Component kickReason, final Component leaveMessage, final Cause cause) {
        super(player);
        this.kickReason = kickReason;
        this.leaveMessage = leaveMessage;
        this.cause = cause;
    }

    @Override
    public Component reason() {
        return this.kickReason;
    }

    @Override
    public void reason(final Component kickReason) {
        this.kickReason = kickReason;
    }

    @Override
    @Deprecated
    public String getReason() {
        return LegacyComponentSerializer.legacySection().serialize(this.kickReason);
    }

    @Override
    @Deprecated
    public void setReason(final String kickReason) {
        this.kickReason = LegacyComponentSerializer.legacySection().deserialize(kickReason);
    }

    @Override
    public Component leaveMessage() {
        return this.leaveMessage;
    }

    @Override
    public void leaveMessage(final Component leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    @Override
    @Deprecated
    public String getLeaveMessage() {
        return LegacyComponentSerializer.legacySection().serialize(this.leaveMessage);
    }

    @Override
    @Deprecated
    public void setLeaveMessage(final String leaveMessage) {
        this.leaveMessage = LegacyComponentSerializer.legacySection().deserialize(leaveMessage);
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerKickEvent.getHandlerList();
    }
}
