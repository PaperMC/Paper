package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * Stores details for damage events where the damager is a block
 */
public class EntityDamageByEntityEvent extends EntityDamageEvent implements Cancellable {

    private Entity damager;

    public EntityDamageByEntityEvent(Entity damager, Entity damagee, DamageCause cause, int damage)
    {
        super(Event.Type.ENTITY_DAMAGED, damagee, cause, damage);
        this.damager = damager;
    }

    protected EntityDamageByEntityEvent(Type damageType, Entity damager, Entity damagee, DamageCause cause, int damage)
    {
        super(damageType, damagee, cause, damage);
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
