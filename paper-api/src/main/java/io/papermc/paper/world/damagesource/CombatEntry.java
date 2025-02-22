package io.papermc.paper.world.damagesource;

import io.papermc.paper.InternalAPIBridge;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a combat entry
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface CombatEntry {

    /**
     * Gets the damage source
     *
     * @return the damage source
     */
    DamageSource getDamageSource();

    /**
     * Gets the amount of damage caused
     *
     * @return the amount of damage caused
     */
    float getDamage();

    /**
     * Gets the fall location at the time of this damage
     *
     * @return the fall location
     */
    @Nullable FallLocationType getFallLocation();

    /**
     * Gets the fall distance at the time of this damage
     *
     * @return the fall distance
     */
    float getFallDistance();

    /**
     * Creates a new combat entry.
     * <br>
     * The fall location and fall distance will be calculated from the entity's current state.
     *
     * @param entity entity
     * @param damageSource damage source
     * @param damage damage amount
     * @return combat entry
     * @see #combatEntry(DamageSource, float, FallLocationType, float)
     */
    static CombatEntry combatEntry(LivingEntity entity, DamageSource damageSource, float damage) {
        return InternalAPIBridge.get().createCombatEntry(entity, damageSource, damage);
    }

    /**
     * Creates a new combat entry
     *
     * @param damageSource damage source
     * @param damage damage amount
     * @param fallLocationType fall location type
     * @param fallDistance fall distance
     * @return a new combat entry
     * @see LivingEntity#calculateFallLocationType()
     * @see Entity#getFallDistance()
     */
    static CombatEntry combatEntry(DamageSource damageSource, float damage, @Nullable FallLocationType fallLocationType, float fallDistance) {
        return InternalAPIBridge.get().createCombatEntry(damageSource, damage, fallLocationType, fallDistance);
    }

}
