package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.map.MapView;

/**
 * Called when a map is initialized.
 */
public interface MapInitializeEvent extends ServerEventNew {

    /**
     * Gets the map initialized in this event.
     *
     * @return Map for this event
     */
    MapView getMap();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
