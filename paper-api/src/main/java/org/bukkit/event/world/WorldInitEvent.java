package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;

/**
 * Called when a World is initializing
 */
@SuppressWarnings("serial")
public class WorldInitEvent extends WorldEvent {
    private static final HandlerList handlers = new HandlerList();
    public WorldInitEvent(World world) {
        super(Type.WORLD_INIT, world);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
