package org.bukkit.event.world;

import org.bukkit.World;

public class WorldLoadEvent extends WorldEvent {
    public WorldLoadEvent(World world) {
        super(Type.WORLD_LOAD, world);
    }
}
