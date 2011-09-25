package org.bukkit.event.entity;

import org.bukkit.entity.Entity;

/**
 * Called when an entity is damaged by an entity
 */
public class EntityDamageByEntityEvent extends EntityDamageEvent {

    private Entity damager;

    public EntityDamageByEntityEvent(Entity damager, Entity damagee, DamageCause cause, int damage) {
        super(Type.ENTITY_DAMAGE, damagee, cause, damage);
        this.damager = damager;
    }

    /**
     * Returns the entity that damaged the defender.
     *
     * @return Entity that damaged the defender.
     */
    public Entity getDamager() {
        return damager;
    }
}
