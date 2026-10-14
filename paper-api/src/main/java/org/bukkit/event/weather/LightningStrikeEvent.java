package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Stores data for lightning striking
 */
public class LightningStrikeEvent extends WeatherEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LightningStrike bolt;
    private final Cause cause;

    private boolean cancelled;

    @ApiStatus.Internal
    @Deprecated(since = "1.13.1", forRemoval = true)
    public LightningStrikeEvent(@NotNull final World world, @NotNull final LightningStrike bolt) {
        this(world, bolt, Cause.UNKNOWN);
    }

    @ApiStatus.Internal
    public LightningStrikeEvent(@NotNull final World world, @NotNull final LightningStrike bolt, @NotNull final Cause cause) {
        super(world);
        this.bolt = bolt;
        this.cause = cause;
    }

    /**
     * Gets the bolt which is striking the earth.
     *
     * @return lightning entity
     */
    @NotNull
    public LightningStrike getLightning() {
        return this.bolt;
    }

    /**
     * Gets the cause of this lightning strike.
     *
     * @return strike cause
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
        /**
         * Triggered by the /summon command.
         */
        COMMAND,
        /**
         * Triggered by a Plugin.
         */
        CUSTOM,
        /**
         * Triggered by a Spawner.
         */
        SPAWNER,
        /**
         * Triggered by an enchanted trident.
         */
        TRIDENT,
        /**
         * Triggered by a skeleton horse trap.
         */
        TRAP,
        /**
         * Triggered by weather.
         */
        WEATHER,
        /**
         * Triggered by an enchantment but not a trident.
         */
        ENCHANTMENT,
        /**
         * Unknown trigger.
         */
        UNKNOWN
    }
}
