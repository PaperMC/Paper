package org.bukkit.event.world;

import org.bukkit.World;

public class WorldSaveEvent extends WorldEvent {
    public WorldSaveEvent(World world) {
        super(Type.WORLD_SAVE, world);
    }
}
