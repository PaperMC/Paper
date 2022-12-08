package io.papermc.paper.event.entity;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.PotionSplashEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when a splash water potion "splashes" and affects
 * different entities in different ways.
 */
@NullMarked
public class WaterBottleSplashEvent extends PotionSplashEvent {

    private final Set<LivingEntity> rehydrate;
    private final Set<LivingEntity> extinguish;

    @ApiStatus.Internal
    public WaterBottleSplashEvent(
        final ThrownPotion potion,
        final @Nullable Entity hitEntity,
        final @Nullable Block hitBlock,
        final @Nullable BlockFace hitFace,
        final Map<LivingEntity, Double> affectedEntities,
        final Set<LivingEntity> rehydrate,
        final Set<LivingEntity> extinguish
    ) {
        super(potion, hitEntity, hitBlock, hitFace, affectedEntities);
        this.rehydrate = rehydrate;
        this.extinguish = extinguish;
    }

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
    public @Unmodifiable Collection<LivingEntity> getToDamage() {
        return this.affectedEntities.entrySet().stream().filter(entry -> entry.getValue() > 0).map(Map.Entry::getKey).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Removes this entity from the group that
     * will be damaged.
     *
     * @param entity entity to remove
     */
    public void doNotDamageAsWaterSensitive(final LivingEntity entity) {
        this.affectedEntities.remove(entity);
    }

    /**
     * Adds this entity to the group that
     * will be damaged
     *
     * @param entity entity to add
     */
    public void damageAsWaterSensitive(final LivingEntity entity) {
        this.affectedEntities.put(entity, 1.0);
    }

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
    public Collection<LivingEntity> getToRehydrate() {
        return this.rehydrate;
    }

    /**
     * Get a mutable collection of entities that will
     * be extinguished as a result of this event.
     *
     * @return entities to be extinguished
     */
    public Collection<LivingEntity> getToExtinguish() {
        return this.extinguish;
    }

    /**
     * @return a confusing collection, don't use it
     * @deprecated Use {@link #getToDamage()}
     */
    @Deprecated(since = "1.19.3")
    @Override
    public Collection<LivingEntity> getAffectedEntities() {
        return super.getAffectedEntities();
    }

    /**
     * Doesn't make sense for this event as intensity doesn't vary.
     *
     * @return a confusing value
     * @deprecated check if {@link #getToDamage()} contains an entity
     */
    @Deprecated(since = "1.19.3")
    @Override
    public double getIntensity(final LivingEntity entity) {
        return super.getIntensity(entity);
    }

    /**
     * Doesn't make sense for this event as intensity doesn't vary.
     *
     * @deprecated use {@link #damageAsWaterSensitive(LivingEntity)}
     * or {@link #doNotDamageAsWaterSensitive(LivingEntity)} to change which entities are
     * damaged
     */
    @Deprecated(since = "1.19.3")
    @Override
    public void setIntensity(final LivingEntity entity, final double intensity) {
        super.setIntensity(entity, intensity);
    }
}
