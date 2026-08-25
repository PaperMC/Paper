package io.papermc.paper.threadedregions;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerEventNew;
import org.jspecify.annotations.NullMarked;

/**
 * This event is called after the server is initialised but <b>before</b> the server begins ticking regions in parallel.
 * Plugins may use this as a hook to run post initialisation logic without worrying about the possibility that
 * regions are ticking in parallel.
 */
@NullMarked
public interface RegionizedServerInitEvent extends ServerEventNew {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
