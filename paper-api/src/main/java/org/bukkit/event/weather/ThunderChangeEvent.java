package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Stores data for thunder state changing in a world
 */
public class ThunderChangeEvent extends WeatherEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    private final boolean to;
    // Paper start
    private final Cause cause;

    public ThunderChangeEvent(@NotNull final World world, final boolean to, @NotNull final Cause cause) {
        super(world);
        this.to = to;
        this.cause = cause;
    }

    @Deprecated // Paper end
    public ThunderChangeEvent(@NotNull final World world, final boolean to) {
        super(world);
        this.to = to;
        this.cause = Cause.UNKNOWN; // Paper
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    /**
     * Gets the state of thunder that the world is being set to
     *
     * @return true if the weather is being set to thundering, false otherwise
     */
    public boolean toThunderState() {
        return to;
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
    // Paper start
    /**
     * Gets the cause of the weather change.
     *
     * @return the weather change cause
     */
    @NotNull
    public Cause getCause() {
        return this.cause;
    }

    public enum Cause {
        COMMAND,
        NATURAL,
        SLEEP,
        PLUGIN,
        UNKNOWN
    }
    // Paper end
}
