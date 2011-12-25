package org.bukkit.event.world;

import org.bukkit.World;

/**
 * Called when a World is initializing
 */
@SuppressWarnings("serial")
public class WorldInitEvent extends WorldEvent {
    public WorldInitEvent(World world) {
        super(Type.WORLD_INIT, world);
    }
}
