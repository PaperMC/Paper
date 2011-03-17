package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * Stores data for damage events
 */
public class EntityDamageEvent extends EntityEvent implements Cancellable {

    private int damage;
    private boolean cancelled;
    private DamageCause cause;

    public EntityDamageEvent(Entity damagee, DamageCause cause, int damage)
    {
        super(Event.Type.ENTITY_DAMAGED, damagee);
        this.cause = cause;
        this.damage = damage;
    }

    protected EntityDamageEvent(Event.Type type, Entity damagee, DamageCause cause, int damage)
    {
        super(type, damagee);
        this.cause = cause;
        this.damage = damage;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * If a damage event is cancelled, the damage will not be deducted from the player's health.
     * This will not fire an event.
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * If a damage event is cancelled, the damage will not be deducted from the player's health.
     * This will not fire an event.
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Gets the amount of damage caused by the Block
     * @return The amount of damage caused by the Block
     */
    public int getDamage()
    {
        return damage;
    }

    /**
     * Sets the amount of damage caused by the Block
     * @return The amount of damage caused by the Block
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Gets the cause of the damage.
     * @return A DamageCause value detailing the cause of the damage.
     */
    public DamageCause getCause()
    {
        return cause;
    }

    /**
     * An enum to specify the cause of the damage
     */
    public enum DamageCause
    {
        /**
         * Damage caused when an entity contacts a block such as a Cactus.
         *
         * Damage: 1 (Cactus)
         */
        CONTACT,
        /**
         * Damage caused when an entity attacks another entity.
         *
         * Damage: variable
         */
        ENTITY_ATTACK,
        /**
         * Damage caused when an entity falls a distance greater than 3 blocks
         *
         * Damage: fall height - 3.0
         */
        SUFFOCATION,
        /**
         * Damage caused by being put in a block
         *
         * Damage: 1
         */
        FALL,
        /**
         * Damage caused by direct exposure to fire
         *
         * Damage: 1
         */
        FIRE,
        /**
         * Damage caused due to burns caused by fire
         *
         * Damage: 1
         */
        FIRE_TICK,
        /**
         * Damage caused by direct exposure to lava
         *
         * Damage: 4
         */
        LAVA,
        /**
         * Damage caused by running out of air while in water
         *
         * Damage: 2
         */
        DROWNING,
        /**
         * Damage caused by being in the area when a block explodes.
         *
         * Damage: variable
         */
        BLOCK_EXPLOSION,
        /**
         * Damage caused by being in the area when an entity, such as a Creeper, explodes.
         *
         * Damage: variable
         */
        ENTITY_EXPLOSION,
        /**
         * Damage caused by falling into the void
         *
         * Damage: 4 for players
         */
        VOID,
        /**
         * Custom damage.
         *
         * Damage: variable
         */
        CUSTOM
    }
}
