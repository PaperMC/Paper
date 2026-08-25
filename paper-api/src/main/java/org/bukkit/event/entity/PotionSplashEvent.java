package org.bukkit.event.entity;

import java.util.Collection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.HandlerList;

/**
 * Called when a splash potion hits an area
 */
public interface PotionSplashEvent extends ProjectileHitEvent {

    @Override
    ThrownPotion getEntity();

    /**
     * Gets the potion which caused this event
     *
     * @return The thrown potion entity
     */
    default ThrownPotion getPotion() {
        return this.getEntity();
    }

    /**
     * Retrieves a list of all effected entities
     *
     * @return A fresh copy of the affected entity list
     */
    Collection<LivingEntity> getAffectedEntities();

    /**
     * Gets the intensity of the potion's effects for given entity; This
     * depends on the distance to the impact center
     *
     * @param entity Which entity to get intensity for
     * @return intensity relative to maximum effect; 0.0: not affected; 1.0:
     *     fully hit by potion effects
     */
    double getIntensity(LivingEntity entity);

    /**
     * Overwrites the intensity for a given entity
     *
     * @param entity For which entity to define a new intensity
     * @param intensity relative to maximum effect
     */
    void setIntensity(LivingEntity entity, double intensity);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
