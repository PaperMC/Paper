package org.bukkit.event.server;

import org.bukkit.event.HandlerList;

/**
 * This event is called when a service is registered.
 * <p>
 * Warning: The order in which register and unregister events are called
 * should not be relied upon.
 */
public interface ServiceRegisterEvent extends ServiceEvent {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
