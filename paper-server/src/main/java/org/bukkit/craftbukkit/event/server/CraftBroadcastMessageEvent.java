package org.bukkit.craftbukkit.event.server;

import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.BroadcastMessageEvent;

public class CraftBroadcastMessageEvent extends CraftServerEvent implements BroadcastMessageEvent {

    private final Set<CommandSender> recipients;
    private Component message;

    private boolean cancelled;

    public CraftBroadcastMessageEvent(final boolean isAsync, final Component message, final Set<CommandSender> recipients) {
        super(isAsync);
        this.message = message;
        this.recipients = recipients;
    }

    @Override
    public Component message() {
        return this.message;
    }

    @Override
    public void message(final Component message) {
        this.message = message;
    }

    @Override
    @Deprecated
    public String getMessage() {
        return LegacyComponentSerializer.legacySection().serialize(this.message);
    }

    @Override
    @Deprecated
    public void setMessage(final String message) {
        this.message = LegacyComponentSerializer.legacySection().deserialize(message);
    }

    @Override
    public Set<CommandSender> getRecipients() {
        return this.recipients;
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
        return BroadcastMessageEvent.getHandlerList();
    }
}
