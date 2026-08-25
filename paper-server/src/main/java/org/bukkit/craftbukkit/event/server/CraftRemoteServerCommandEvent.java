package org.bukkit.craftbukkit.event.server;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.RemoteServerCommandEvent;

public class CraftRemoteServerCommandEvent extends CraftServerCommandEvent implements RemoteServerCommandEvent {

    public CraftRemoteServerCommandEvent(final CommandSender sender, final String command) {
        super(sender, command);
    }

    @Override
    public HandlerList getHandlers() {
        return RemoteServerCommandEvent.getHandlerList();
    }
}
