package io.papermc.paper.threadedregions;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called after the server is initialised but <b>before</b> the server begins ticking regions in parallel.
 * Plugins may use this as a hook to run post initialisation logic without worrying about the possibility that
 * regions are ticking in parallel.
 */
public class RegionizedServerInitEvent extends ServerEvent {

    private static final HandlerList handlers = new HandlerList();

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
