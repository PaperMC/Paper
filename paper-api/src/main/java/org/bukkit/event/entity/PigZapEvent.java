package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * Stores data for pigs being zapped
 */
public class PigZapEvent extends EntityEvent implements Cancellable {

    private boolean canceled;
    private Entity pig;
    private Entity pigzombie;
    private Entity bolt;

    public PigZapEvent(Entity pig, Entity bolt, Entity pigzombie) {
        super(Type.PIG_ZAP, pig);
        this.pig = pig;
        this.bolt = bolt;
        this.pigzombie = pigzombie;
    }

    /**
     * Gets the cancellation state of this event. A canceled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is canceled
     */
    public boolean isCancelled() {
        return canceled;
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    /**
     * Gets the bolt which is striking the pig.
     *
     * @return lightning entity
     */
    public Entity getLightning() {
        return bolt;
    }

    /**
     * Gets the zombie pig that will replace the pig,
     * provided the event is not cancelled first.
     *
     * @return resulting entity
     */
    public Entity getPigZombie() {
        return pigzombie;
    }
}
