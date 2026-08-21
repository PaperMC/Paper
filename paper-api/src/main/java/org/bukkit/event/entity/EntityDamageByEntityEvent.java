package org.bukkit.event.entity;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;

/**
 * Called when an entity is damaged by an entity
 */
public interface EntityDamageByEntityEvent extends EntityDamageEvent {

    /**
     * Returns the entity that damaged the defender.
     *
     * @return Entity that damaged the defender.
     */
    Entity getDamager();

    /**
     * Shows this damage instance was critical.
     * The damage instance can be critical if the attacking player met the respective conditions.
     * Furthermore, arrows may also cause a critical damage event if the arrow {@link org.bukkit.entity.AbstractArrow#isCritical()}.
     *
     * @return if the hit was critical.
     * @see <a href="https://minecraft.wiki/wiki/Damage#Critical_hit">https://minecraft.wiki/wiki/Damage#Critical_hit</a>
     */
    boolean isCritical();

    /**
     * {@inheritDoc}
     * <p>
     * The {@link DamageSource#getDirectEntity()} may be different from the {@link #getDamager()}
     * if the damage source did not originally include a damager entity, but one was included
     * for this event {@link #getDamager()}.
     */
    @Override
    DamageSource getDamageSource();
}
