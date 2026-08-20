package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.EventTmp;
import org.jetbrains.annotations.NotNull;

/**
 * Represents events within a world
 */
public abstract class WorldEvent extends EventTmp {

    protected final World world;

    protected WorldEvent(@NotNull final World world) {
        this(world, false);
    }

    protected WorldEvent(@NotNull World world, boolean isAsync) {
        super(isAsync);
        this.world = world;
    }

    /**
     * Gets the world primarily involved with this event
     *
     * @return World which caused this event
     */
    @NotNull
    public World getWorld() {
        return this.world;
    }
}
