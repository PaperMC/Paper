package org.bukkit.event.entity;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a Creeper is struck by lightning.
 * <p>
 * If this event is cancelled, the Creeper will not be powered.
 */
public interface CreeperPowerEvent extends EntityEventNew, Cancellable {

    @Override
    Creeper getEntity();

    /**
     * Gets the lightning bolt which is striking the Creeper.
     *
     * @return The Entity for the lightning bolt which is striking the Creeper
     */
    @Nullable LightningStrike getLightning();

    /**
     * Gets the cause of the creeper being (un)powered.
     *
     * @return A PowerCause value detailing the cause of change in power.
     */
    PowerCause getCause();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * An enum to specify the cause of the change in power
     */
    enum PowerCause {

        /**
         * Power change caused by a lightning bolt
         * <p>
         * Powered state: {@code true}
         */
        LIGHTNING,
        /**
         * Power change caused by something else (probably a plugin)
         * <p>
         * Powered state: {@code true}
         */
        SET_ON,
        /**
         * Power change caused by something else (probably a plugin)
         * <p>
         * Powered state: {@code false}
         */
        SET_OFF
    }
}
