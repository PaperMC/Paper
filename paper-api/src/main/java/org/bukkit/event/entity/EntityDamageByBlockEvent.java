package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * Stores details for damage events where the damager is a block
 */
public class EntityDamageByBlockEvent extends EntityDamageEvent implements Cancellable {

    private Block damager;

    public EntityDamageByBlockEvent(Block damager, Entity damagee, DamageCause cause, int damage) {
        super(Type.ENTITY_DAMAGE, damagee, cause, damage);
        this.damager = damager;
    }

    /**
     * Returns the block that damaged the player.
     * @return Block that damaged the player
     */
    public Block getDamager()
    {
        return damager;
    }

}
