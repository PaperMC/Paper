package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;

public class WorldSaveEvent extends WorldEvent {
    private static final HandlerList handlers = new HandlerList();

    public WorldSaveEvent(final World world) {
        super(world);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
