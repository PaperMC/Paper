package io.papermc.paper.threadedregions;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;
import org.jspecify.annotations.NullMarked;

/**
 * This event is called after the server is initialised but <b>before</b> the server begins ticking regions in parallel.
 * Plugins may use this as a hook to run post initialisation logic without worrying about the possibility that
 * regions are ticking in parallel.
 */
@NullMarked
public class RegionizedServerInitEvent extends ServerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
