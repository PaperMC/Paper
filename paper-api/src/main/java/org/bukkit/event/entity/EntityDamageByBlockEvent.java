package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

/**
 * Called when an entity is damaged by a block
 */
public class EntityDamageByBlockEvent extends EntityDamageEvent {
    private final Block damager;

    public EntityDamageByBlockEvent(final Block damager, final Entity damagee, final DamageCause cause, final int damage) {
        super(damagee, cause, damage);
        this.damager = damager;
    }

    /**
     * Returns the block that damaged the player.
     *
     * @return Block that damaged the player
     */
    public Block getDamager() {
        return damager;
    }
}
