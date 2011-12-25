package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * Called when a Creeper is struck by lightning.
 * <p />
 * If a Creeper Power event is cancelled, the Creeper will not be powered.
 */
@SuppressWarnings("serial")
public class CreeperPowerEvent extends EntityEvent implements Cancellable {

    private boolean canceled;
    private PowerCause cause;
    private Entity bolt;

    public CreeperPowerEvent(Entity creeper, Entity bolt, PowerCause cause) {
        super(Type.CREEPER_POWER, creeper);
        this.bolt = bolt;
        this.cause = cause;
    }

    public CreeperPowerEvent(Entity creeper, PowerCause cause) {
        super(Type.CREEPER_POWER, creeper);
        this.cause = cause;
        this.bolt = null;
    }

    public boolean isCancelled() {
        return canceled;
    }

    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    /**
     * Gets the lightning bolt which is striking the Creeper.
     *
     * @return The Entity for the lightning bolt which is striking the Creeper
     */
    public Entity getLightning() {
        return bolt;
    }

    /**
     * Gets the cause of the creeper being (un)powered.
     *
     * @return A PowerCause value detailing the cause of change in power.
     */
    public PowerCause getCause() {
        return cause;
    }

    /**
     * An enum to specify the cause of the change in power
     */
    public enum PowerCause {

        /**
         * Power change caused by a lightning bolt
         * Powered state: true
         */
        LIGHTNING,
        /**
         * Power change caused by something else (probably a plugin)
         * Powered state: true
         */
        SET_ON,
        /**
         * Power change caused by something else (probably a plugin)
         * Powered state: false
         */
        SET_OFF
    }
}
