package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * Stores data for pigs being zapped
 */
@SuppressWarnings("serial")
public class PigZapEvent extends EntityEvent implements Cancellable {

    private boolean canceled;
    private Entity pigzombie;
    private Entity bolt;

    public PigZapEvent(Entity pig, Entity bolt, Entity pigzombie) {
        super(Type.PIG_ZAP, pig);
        this.bolt = bolt;
        this.pigzombie = pigzombie;
    }

    public boolean isCancelled() {
        return canceled;
    }

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
