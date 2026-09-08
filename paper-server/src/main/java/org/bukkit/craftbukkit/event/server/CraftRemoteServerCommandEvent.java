package org.bukkit.craftbukkit.event.server;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.rcon.RconConsoleSource;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.RemoteServerCommandEvent;

public class CraftRemoteServerCommandEvent extends CraftServerCommandEvent implements RemoteServerCommandEvent {

    public CraftRemoteServerCommandEvent(final CommandSender sender, final String command) {
        super(sender, command);
    }

    public CraftRemoteServerCommandEvent(final RconConsoleSource rconConsoleSource, final CommandSourceStack wrapper, final String command) {
        this(rconConsoleSource.getBukkitSender(wrapper), command);
    }

    @Override
    public HandlerList getHandlers() {
        return RemoteServerCommandEvent.getHandlerList();
    }
}
