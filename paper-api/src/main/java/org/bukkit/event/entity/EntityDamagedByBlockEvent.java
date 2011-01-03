package org.bukkit.event.entity;

import org.bukkit.Block;
import org.bukkit.Entity;
import org.bukkit.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * Stores details for damage events where the damager is a block
 */
public class EntityDamagedByBlockEvent extends EntityEvent implements Cancellable {

    private Block damager;
    private int damage;
    private boolean cancelled;

    public EntityDamagedByBlockEvent(Block damager, Entity damagee, int damage)
    {
        super(Event.Type.ENTITY_DAMAGEDBY_BLOCK, damagee);
        this.damager = damager;
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
     * Returns the block that damaged the player.
     * @return Block that damaged the player
     */
    public Block getDamager()
    {
        return damager;
    }

    /**
     * Gets the amount of damage caused by the Block
     * @return The amount of damage caused by the Block
     */
    public int getDamage()
    {
        return damage;
    }
}
