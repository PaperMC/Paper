package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;

/**
 * Called when a World is loaded
 */
@SuppressWarnings("serial")
public class WorldLoadEvent extends WorldEvent {
    private static final HandlerList handlers = new HandlerList();
    public WorldLoadEvent(World world) {
        super(Type.WORLD_LOAD, world);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
