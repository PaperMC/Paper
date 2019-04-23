package org.bukkit.event.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * An event that is called when a world's spawn changes. The world's previous
 * spawn location is included.
 */
public class SpawnChangeEvent extends WorldEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Location previousLocation;

    public SpawnChangeEvent(@NotNull final World world, @NotNull final Location previousLocation) {
        super(world);
        this.previousLocation = previousLocation;
    }

    /**
     * Gets the previous spawn location
     *
     * @return Location that used to be spawn
     */
    @NotNull
    public Location getPreviousLocation() {
        return previousLocation;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
