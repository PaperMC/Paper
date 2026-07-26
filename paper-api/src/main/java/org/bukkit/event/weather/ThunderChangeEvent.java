package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Stores data for thunder state changing in a world
 */
public class ThunderChangeEvent extends WeatherEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final boolean newThunderState;
    private final Cause cause;

    private boolean cancelled;

    @ApiStatus.Internal
    public ThunderChangeEvent(@NotNull final World world, final boolean newThunderState, @NotNull final Cause cause) {
        super(world);
        this.newThunderState = newThunderState;
        this.cause = cause;
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public ThunderChangeEvent(@NotNull final World world, final boolean newThunderState) {
        this(world, newThunderState, Cause.UNKNOWN);
    }

    /**
     * Gets the state of thunder that the world is being set to
     *
     * @return {@code true} if the weather is being set to thundering, {@code false} otherwise
     */
    public boolean toThunderState() {
        return this.newThunderState;
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
