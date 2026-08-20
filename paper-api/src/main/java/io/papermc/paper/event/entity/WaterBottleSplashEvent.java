package io.papermc.paper.event.entity;

import java.util.Collection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.PotionSplashEvent;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Called when a splash water potion "splashes" and affects
 * different entities in different ways.
 */
public interface WaterBottleSplashEvent extends PotionSplashEvent {

    /**
     * Gets an immutable collection of entities that
     * will take damage as a result of this event. Use
     * other methods on this class to modify which entities
     * take damage.
     *
     * @return an immutable collection of entities
     * @see #doNotDamageAsWaterSensitive(LivingEntity)
     * @see #damageAsWaterSensitive(LivingEntity)
     */
    @Unmodifiable Collection<LivingEntity> getToDamage();

    /**
     * Removes this entity from the group that
     * will be damaged.
     *
     * @param entity entity to remove
     */
    void doNotDamageAsWaterSensitive(LivingEntity entity);

    /**
     * Adds this entity to the group that
     * will be damaged
     *
     * @param entity entity to add
     */
    void damageAsWaterSensitive(LivingEntity entity);

    /**
     * Get a mutable collection of entities
     * that will be rehydrated by this.
     * <p>
     * As of 1.19.3 this only will contain Axolotls as they
     * are the only entity type that can be rehydrated, but
     * it may change in the future.
     *
     * @return the entities
     */
    Collection<LivingEntity> getToRehydrate();

    /**
     * Get a mutable collection of entities that will
     * be extinguished as a result of this event.
     *
     * @return entities to be extinguished
     */
    Collection<LivingEntity> getToExtinguish();

    /**
     * @return a confusing collection, don't use it
     * @deprecated Use {@link #getToDamage()}
     */
    @Deprecated(since = "1.19.3")
    @Override
    Collection<LivingEntity> getAffectedEntities();

    /**
     * Doesn't make sense for this event as intensity doesn't vary.
     *
     * @return a confusing value
     * @deprecated check if {@link #getToDamage()} contains an entity
     */
    @Deprecated(since = "1.19.3")
    @Override
    double getIntensity(LivingEntity entity);

    /**
     * Doesn't make sense for this event as intensity doesn't vary.
     *
     * @deprecated use {@link #damageAsWaterSensitive(LivingEntity)}
     * or {@link #doNotDamageAsWaterSensitive(LivingEntity)} to change which entities are
     * damaged
     */
    @Deprecated(since = "1.19.3")
    @Override
    void setIntensity(LivingEntity entity, double intensity) ;
}
