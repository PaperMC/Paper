package org.bukkit.craftbukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldInitEvent;

public class CraftWorldInitEvent extends CraftWorldEvent implements WorldInitEvent {

    public CraftWorldInitEvent(final World world) {
        super(world);
    }

    @Override
    public HandlerList getHandlers() {
        return WorldInitEvent.getHandlerList();
    }
}
