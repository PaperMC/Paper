package org.bukkit.event.world;

import org.bukkit.World;

public class WorldInitEvent extends WorldEvent {
    public WorldInitEvent(World world) {
        super(Type.WORLD_INIT, world);
    }
}
