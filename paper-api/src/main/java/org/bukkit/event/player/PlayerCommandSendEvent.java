package org.bukkit.event.player;

import java.util.Collection;
import org.bukkit.event.HandlerList;

/**
 * This event is called when the list of available server commands is sent to
 * the player.
 * <br>
 * Commands may be removed from display using this event, but implementations
 * are not required to securely remove all traces of the command. If secure
 * removal of commands is required, then the command should be assigned a
 * permission which is not granted to the player.
 */
public interface PlayerCommandSendEvent extends PlayerEventNew {

    /**
     * Returns a mutable collection of all top level commands to be sent.
     * <br>
     * It is not legal to add entries to this collection, only remove them.
     * Behaviour of adding entries is undefined.
     *
     * @return collection of all commands
     */
    Collection<String> getCommands();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
