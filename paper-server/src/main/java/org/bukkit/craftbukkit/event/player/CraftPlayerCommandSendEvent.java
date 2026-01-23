package org.bukkit.craftbukkit.event.player;

import java.util.Collection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class CraftPlayerCommandSendEvent extends CraftPlayerEvent implements PlayerCommandSendEvent {

    private final Collection<String> commands;

    public CraftPlayerCommandSendEvent(final Player player, final Collection<String> commands) {
        super(player);
        this.commands = commands;
    }

    @Override
    public Collection<String> getCommands() {
        return this.commands;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerCommandSendEvent.getHandlerList();
    }
}
