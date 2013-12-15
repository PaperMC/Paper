package org.bukkit.event.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a splash potion hits an area
 */
public class PotionSplashEvent extends ProjectileHitEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Map<LivingEntity, Double> affectedEntities;

    public PotionSplashEvent(final ThrownPotion potion, final Map<LivingEntity, Double> affectedEntities) {
        super(potion);

        this.affectedEntities = affectedEntities;
    }

    @Override
    public ThrownPotion getEntity() {
        return (ThrownPotion) entity;
    }

    /**
     * Gets the potion which caused this event
     *
     * @return The thrown potion entity
     */
    public ThrownPotion getPotion() {
        return (ThrownPotion) getEntity();
    }

    /**
     * Retrieves a list of all effected entities
     *
     * @return A fresh copy of the affected entity list
     */
    public Collection<LivingEntity> getAffectedEntities() {
        return new ArrayList<LivingEntity>(affectedEntities.keySet());
    }

    /**
     * Gets the intensity of the potion's effects for given entity; This
     * depends on the distance to the impact center
     *
     * @param entity Which entity to get intensity for
     * @return intensity relative to maximum effect; 0.0: not affected; 1.0:
     *     fully hit by potion effects
     */
    public double getIntensity(LivingEntity entity) {
        Double intensity = affectedEntities.get(entity);
        return intensity != null ? intensity : 0.0;
    }

    /**
     * Overwrites the intensity for a given entity
     *
     * @param entity For which entity to define a new intensity
     * @param intensity relative to maximum effect
     */
    public void setIntensity(LivingEntity entity, double intensity) {
        Validate.notNull(entity, "You must specify a valid entity.");
        if (intensity <= 0.0) {
            affectedEntities.remove(entity);
        } else {
            affectedEntities.put(entity, Math.min(intensity, 1.0));
        }
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
