package org.bukkit.craftbukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldSaveEvent;

public class CraftWorldSaveEvent extends CraftWorldEvent implements WorldSaveEvent {

    public CraftWorldSaveEvent(final World world) {
        super(world);
    }

    @Override
    public HandlerList getHandlers() {
        return WorldSaveEvent.getHandlerList();
    }
}
