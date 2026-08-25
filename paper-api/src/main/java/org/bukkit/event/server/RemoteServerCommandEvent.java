package org.bukkit.event.server;

import org.bukkit.event.HandlerList;

/**
 * This event is called when a command is received over RCON. See the javadocs
 * of {@link ServerCommandEvent} for more information.
 */
public interface RemoteServerCommandEvent extends ServerCommandEvent {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
