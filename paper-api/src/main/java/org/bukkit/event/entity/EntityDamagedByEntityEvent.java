package org.bukkit.event.entity;

import org.bukkit.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * Stores details for damage events where the damager is a block
 */
public class EntityDamagedByEntityEvent extends EntityDamagedEvent implements Cancellable {

    private Entity damager;

    public EntityDamagedByEntityEvent(Entity damager, Entity damagee, DamageCause cause, int damage)
    {
        super(Event.Type.ENTITY_DAMAGEDBY_ENTITY, damagee, cause, damage);
        this.damager = damager;
    }

    /**
     * Returns the entity that damaged the defender.
     * @return Entity that damaged the defender.
     */
    public Entity getDamager()
    {
        return damager;
    }

}
