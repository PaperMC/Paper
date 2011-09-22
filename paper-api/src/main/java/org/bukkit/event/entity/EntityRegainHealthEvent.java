package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * Stores data for health-regain events
 */
public class EntityRegainHealthEvent extends EntityEvent implements Cancellable {

    private boolean cancelled;
    private int amount;
    private RegainReason regainReason;

    public EntityRegainHealthEvent(Entity entity, int amount, RegainReason regainReason) {
        super(Event.Type.ENTITY_REGAIN_HEALTH, entity);
        this.amount = amount;
        this.regainReason = regainReason;
    }

    /**
     * Gets the amount of regained health
     *
     * @return The amount of health regained
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of regained health
     *
     * @param amount the amount of health the entity will regain
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Gets the reason for why the entity is regaining health
     *
     * @return A RegainReason detailing the reason for the entity regaining health
     */
    public RegainReason getRegainReason() {
        return regainReason;
    }

    /**
     * An enum to specify the type of health regaining that is occurring
     */
    public enum RegainReason {

        /**
         * When a player regains health from regenerating due to Peaceful mode (difficulty=0)
         */
        REGEN,
        /**
         * When a player regains health from regenerating due to their hunger being satisfied
         */
        SATIATED,
        /**
         * When a player regains health from eating consumables
         */
        EATING,
        /**
         * Any other reason not covered by the reasons above
         */
        CUSTOM
    }
}
