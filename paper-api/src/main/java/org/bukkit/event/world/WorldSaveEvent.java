package org.bukkit.event.world;

import org.bukkit.World;

@SuppressWarnings("serial")
public class WorldSaveEvent extends WorldEvent {
    public WorldSaveEvent(World world) {
        super(Type.WORLD_SAVE, world);
    }
}
