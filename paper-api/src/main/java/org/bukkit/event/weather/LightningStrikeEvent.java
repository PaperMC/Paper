package org.bukkit.event.weather;

import org.bukkit.entity.LightningStrike;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Stores data for lightning striking
 */
public interface LightningStrikeEvent extends WeatherEvent, Cancellable {

    /**
     * Gets the bolt which is striking the earth.
     *
     * @return lightning entity
     */
    LightningStrike getLightning();

    /**
     * Gets the cause of this lightning strike.
     *
     * @return strike cause
     */
    Cause getCause();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum Cause {
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
