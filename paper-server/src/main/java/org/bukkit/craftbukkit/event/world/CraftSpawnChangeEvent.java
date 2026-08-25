package org.bukkit.craftbukkit.event.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.SpawnChangeEvent;

public class CraftSpawnChangeEvent extends CraftWorldEvent implements SpawnChangeEvent {

    private final Location previousLocation;

    public CraftSpawnChangeEvent(final World world, final Location previousLocation) {
        super(world);
        this.previousLocation = previousLocation;
    }

    @Override
    public Location getPreviousLocation() {
        return this.previousLocation.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return SpawnChangeEvent.getHandlerList();
    }
}
