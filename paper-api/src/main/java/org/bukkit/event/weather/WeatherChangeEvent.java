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

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final boolean newWeatherState;
    private final Cause cause;

    private boolean cancelled;

    @ApiStatus.Internal
    public WeatherChangeEvent(@NotNull final World world, final boolean newWeatherState, @NotNull Cause cause) {
        super(world);
        this.newWeatherState = newWeatherState;
        this.cause = cause;
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public WeatherChangeEvent(@NotNull final World world, final boolean newWeatherState) {
        this(world, newWeatherState, Cause.UNKNOWN);
    }

    /**
     * Gets the state of weather that the world is being set to
     *
     * @return {@code true} if the weather is being set to raining, {@code false} otherwise
     */
    public boolean toWeatherState() {
        return this.newWeatherState;
    }

    /**
     * Gets the cause of the weather change.
     *
     * @return the weather change cause
     */
    @NotNull
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum Cause {
        COMMAND,
        NATURAL,
        SLEEP,
        PLUGIN,
        UNKNOWN
    }
}
