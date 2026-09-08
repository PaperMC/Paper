package org.bukkit.craftbukkit.event.world;

import net.minecraft.world.level.LevelAccessor;
import org.bukkit.World;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.world.WorldEvent;

public abstract class CraftWorldEvent extends CraftEvent implements WorldEvent {

    protected final World world;

    protected CraftWorldEvent(final World world) {
        this.world = world;
    }

    protected CraftWorldEvent(final World world, final boolean isAsync) {
        super(isAsync);
        this.world = world;
    }

    protected CraftWorldEvent(final LevelAccessor level) {
        this(level.getMinecraftWorld().getWorld());
    }

    protected CraftWorldEvent(final LevelAccessor level, final boolean isAsync) {
        this(level.getMinecraftWorld().getWorld(), isAsync);
    }

    @Override
    public World getWorld() {
        return this.world;
    }
}
