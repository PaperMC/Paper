package org.bukkit.event.world;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;

/**
 * An event that is called when a world's spawn changes. The world's previous
 * spawn location is included.
 */
public interface SpawnChangeEvent extends WorldEventNew {

    /**
     * Gets the previous spawn location
     *
     * @return Location that used to be spawn
     */
    Location getPreviousLocation();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
