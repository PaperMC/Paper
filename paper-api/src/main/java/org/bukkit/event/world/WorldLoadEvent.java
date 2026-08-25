package org.bukkit.event.world;

import org.bukkit.event.HandlerList;

/**
 * Called when a World is loaded
 */
public interface WorldLoadEvent extends WorldEventNew {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
