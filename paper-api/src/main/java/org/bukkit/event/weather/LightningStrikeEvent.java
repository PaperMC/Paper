package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Stores data for lightning striking
 *
 * @since 1.0.0 R1
 */
public class LightningStrikeEvent extends WeatherEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    private final LightningStrike bolt;
    private final Cause cause;

    @Deprecated(since = "1.13.1")
    public LightningStrikeEvent(@NotNull final World world, @NotNull final LightningStrike bolt) {
        this(world, bolt, Cause.UNKNOWN);
    }

    public LightningStrikeEvent(@NotNull final World world, @NotNull final LightningStrike bolt, @NotNull final Cause cause) {
        super(world);
        this.bolt = bolt;
        this.cause = cause;
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
     * Gets the bolt which is striking the earth.
     *
     * @return lightning entity
     */
    @NotNull
    public LightningStrike getLightning() {
        return bolt;
    }

    /**
     * Gets the cause of this lightning strike.
     *
     * @return strike cause
     * @since 1.13.1
     */
    @NotNull
    public Cause getCause() {
        return cause;
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
        UNKNOWN;
    }
}
