package org.bukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a World is initializing.
 * <p>
 * To get every world it is recommended to add following to the plugin.yml.
 * <pre>load: STARTUP</pre>
 *
 * @since 1.0.0 R1
 */
public class WorldInitEvent extends WorldEvent {
    private static final HandlerList handlers = new HandlerList();

    public WorldInitEvent(@NotNull final World world) {
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
