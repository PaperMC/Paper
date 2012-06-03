package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Stores data for damage events
 */
public class EntityDamageEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private int damage;
    private boolean cancelled;
    private final DamageCause cause;

    public EntityDamageEvent(final Entity damagee, final DamageCause cause, final int damage) {
        super(damagee);
        this.cause = cause;
        this.damage = damage;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Gets the amount of damage caused by the Block
     *
     * @return The amount of damage caused by the Block
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Sets the amount of damage caused by the Block
     *
     * @param damage The amount of damage caused by the Block
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Gets the cause of the damage.
     *
     * @return A DamageCause value detailing the cause of the damage.
     */
    public DamageCause getCause() {
        return cause;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * An enum to specify the cause of the damage
     */
    public enum DamageCause {

        /**
         * Damage caused when an entity contacts a block such as a Cactus.
         * <p />
         * Damage: 1 (Cactus)
         */
        CONTACT,
        /**
         * Damage caused when an entity attacks another entity.
         * <p />
         * Damage: variable
         */
        ENTITY_ATTACK,
        /**
         * Damage caused when attacked by a projectile.
         * <p />
         * Damage: variable
         */
        PROJECTILE,
        /**
         * Damage caused by being put in a block
         * <p />
         * Damage: 1
         */
        SUFFOCATION,
        /**
         * Damage caused when an entity falls a distance greater than 3 blocks
         * <p />
         * Damage: fall height - 3.0
         */
        FALL,
        /**
         * Damage caused by direct exposure to fire
         * <p />
         * Damage: 1
         */
        FIRE,
        /**
         * Damage caused due to burns caused by fire
         * <p />
         * Damage: 1
         */
        FIRE_TICK,
        /**
         * Damage caused due to a snowman melting
         * <p />
         * Damage: 1
         */
        MELTING,
        /**
         * Damage caused by direct exposure to lava
         * <p />
         * Damage: 4
         */
        LAVA,
        /**
         * Damage caused by running out of air while in water
         * <p />
         * Damage: 2
         */
        DROWNING,
        /**
         * Damage caused by being in the area when a block explodes.
         * <p />
         * Damage: variable
         */
        BLOCK_EXPLOSION,
        /**
         * Damage caused by being in the area when an entity, such as a Creeper, explodes.
         * <p />
         * Damage: variable
         */
        ENTITY_EXPLOSION,
        /**
         * Damage caused by falling into the void
         * <p />
         * Damage: 4 for players
         */
        VOID,
        /**
         * Damage caused by being struck by lightning
         * <p />
         * Damage: 5
         */
        LIGHTNING,
        /**
         * Damage caused by committing suicide using the command "/kill"
         * <p />
         * Damage: 1000
         */
        SUICIDE,
        /**
         * Damage caused by starving due to having an empty hunger bar
         * <p />
         * Damage: 1
         */
        STARVATION,
        /**
         * Damage caused due to an ongoing poison effect
         *
         * Damage: 1
         */
        POISON,
        /**
         * Damage caused by being hit by a damage potion or spell
         *
         * Damage: variable
         */
        MAGIC,
        /**
         * Custom damage.
         * <p />
         * Damage: variable
         */
        CUSTOM
    }
}
