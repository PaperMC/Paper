package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Stores data for weather changing in a world
 */
public class WeatherChangeEvent extends WeatherEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    private final boolean to;
    private final Cause cause;

    @ApiStatus.Internal
    public WeatherChangeEvent(@NotNull final World world, final boolean to, @NotNull Cause cause) {
        super(world);
        this.to = to;
        this.cause = cause;
    }

    @Deprecated(forRemoval = true)
    public WeatherChangeEvent(@NotNull final World world, final boolean to) {
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
     * Gets the state of weather that the world is being set to
     *
     * @return true if the weather is being set to raining, false otherwise
     */
    public boolean toWeatherState() {
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
        return cause;
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
