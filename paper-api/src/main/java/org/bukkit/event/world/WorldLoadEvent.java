package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a World is loaded
 *
 * @since 1.0.0 R1
 */
public class WorldLoadEvent extends WorldEvent {
    private static final HandlerList handlers = new HandlerList();

    public WorldLoadEvent(@NotNull final World world) {
        super(world);
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
