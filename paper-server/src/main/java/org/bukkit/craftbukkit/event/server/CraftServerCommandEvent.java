package org.bukkit.craftbukkit.event.server;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerCommandEvent;

public class CraftServerCommandEvent extends CraftServerEvent implements ServerCommandEvent {

    private final CommandSender sender;
    private String command;

    private boolean cancelled;

    public CraftServerCommandEvent(final CommandSender sender, final String command) {
        this.sender = sender;
        this.command = command;
    }

    @Override
    public CommandSender getSender() {
        return this.sender;
    }

    @Override
    public String getCommand() {
        return this.command;
    }

    @Override
    public void setCommand(final String command) {
        this.command = command;
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
        return ServerCommandEvent.getHandlerList();
    }
}
