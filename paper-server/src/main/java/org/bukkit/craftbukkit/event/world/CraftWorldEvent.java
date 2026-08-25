package org.bukkit.craftbukkit.event.world;

import org.bukkit.World;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.world.WorldEvent;

public abstract class CraftWorldEvent extends CraftEvent implements WorldEvent {

    protected final World world;

    protected CraftWorldEvent(final World world) {
        this(world, false);
    }

    protected CraftWorldEvent(final World world, final boolean isAsync) {
        super(isAsync);
        this.world = world;
    }

    @Override
    public World getWorld() {
        return this.world;
    }
}
