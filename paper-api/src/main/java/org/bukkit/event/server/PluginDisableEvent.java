package org.bukkit.event.server;

import org.bukkit.event.HandlerList;

/**
 * Called when a plugin is disabled.
 */
public interface PluginDisableEvent extends PluginEvent {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
