package org.bukkit.craftbukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldLoadEvent;

public class CraftWorldLoadEvent extends CraftWorldEvent implements WorldLoadEvent {

    public CraftWorldLoadEvent(final World world) {
        super(world);
    }

    @Override
    public HandlerList getHandlers() {
        return WorldLoadEvent.getHandlerList();
    }
}
