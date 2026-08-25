package com.destroystokyo.paper.event.server;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is fired when whitelist is toggled
 *
 * @author Mark Vainomaa
 */
public interface WhitelistToggleEvent extends Event {

    /**
     * Gets whether whitelist is going to be enabled or not
     *
     * @return Whether whitelist is going to be enabled or not
     */
    boolean isEnabled();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
