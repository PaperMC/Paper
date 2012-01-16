package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;

@SuppressWarnings("serial")
public class WorldSaveEvent extends WorldEvent {
    private static final HandlerList handlers = new HandlerList();
    public WorldSaveEvent(World world) {
        super(Type.WORLD_SAVE, world);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
