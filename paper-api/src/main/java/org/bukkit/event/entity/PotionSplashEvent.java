package org.bukkit.event.entity;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a splash potion hits an area
 */
public class PotionSplashEvent extends ProjectileHitEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Map<LivingEntity, Double> affectedEntities;

    public PotionSplashEvent(@NotNull final ThrownPotion potion, @NotNull final Map<LivingEntity, Double> affectedEntities) {
        super(potion);

        this.affectedEntities = affectedEntities;
    }

    @NotNull
    @Override
    public ThrownPotion getEntity() {
        return (ThrownPotion) entity;
    }

    /**
     * Gets the potion which caused this event
     *
     * @return The thrown potion entity
     */
    @NotNull
    public ThrownPotion getPotion() {
        return (ThrownPotion) getEntity();
    }

    /**
     * Retrieves a list of all effected entities
     *
     * @return A fresh copy of the affected entity list
     */
    @NotNull
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
    public double getIntensity(@NotNull LivingEntity entity) {
        Double intensity = affectedEntities.get(entity);
        return intensity != null ? intensity : 0.0;
    }

    /**
     * Overwrites the intensity for a given entity
     *
     * @param entity For which entity to define a new intensity
     * @param intensity relative to maximum effect
     */
    public void setIntensity(@NotNull LivingEntity entity, double intensity) {
        Preconditions.checkArgument(entity != null, "You must specify a valid entity.");
        if (intensity <= 0.0) {
            affectedEntities.remove(entity);
        } else {
            affectedEntities.put(entity, Math.min(intensity, 1.0));
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
