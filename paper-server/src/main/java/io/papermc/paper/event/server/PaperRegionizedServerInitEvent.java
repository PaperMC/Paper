package io.papermc.paper.event.server;

import io.papermc.paper.threadedregions.RegionizedServerInitEvent;
import org.bukkit.craftbukkit.event.server.CraftServerEvent;
import org.bukkit.event.HandlerList;

public class PaperRegionizedServerInitEvent extends CraftServerEvent implements RegionizedServerInitEvent {

    @Override
    public HandlerList getHandlers() {
        return RegionizedServerInitEvent.getHandlerList();
    }
}
