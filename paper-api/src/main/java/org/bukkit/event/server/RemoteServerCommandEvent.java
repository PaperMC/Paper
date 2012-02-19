package org.bukkit.event.server;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

/**
 * Remote Server Command events
 */
public class RemoteServerCommandEvent extends ServerCommandEvent {
    private static final HandlerList handlers = new HandlerList();

    public RemoteServerCommandEvent(final CommandSender sender, final String command) {
        super(sender, command);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
