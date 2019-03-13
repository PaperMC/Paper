package org.bukkit.event.player;

import java.util.Collection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when the list of available server commands is sent to
 * the player.
 * <br>
 * Commands may be removed from display using this event, but implementations
 * are not required to securely remove all traces of the command. If secure
 * removal of commands is required, then the command should be assigned a
 * permission which is not granted to the player.
 */
public class PlayerCommandSendEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Collection<String> commands;

    public PlayerCommandSendEvent(@NotNull final Player player, @NotNull final Collection<String> commands) {
        super(player);
        this.commands = commands;
    }

    /**
     * Returns a mutable collection of all top level commands to be sent.
     * <br>
     * It is not legal to add entries to this collection, only remove them.
     * Behaviour of adding entries is undefined.
     *
     * @return collection of all commands
     */
    @NotNull
    public Collection<String> getCommands() {
        return commands;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
