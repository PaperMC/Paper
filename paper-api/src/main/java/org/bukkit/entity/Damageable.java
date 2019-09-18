package org.bukkit.entity;

import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an {@link Entity} that has health and can take damage.
 */
public interface Damageable extends Entity {
    /**
     * Deals the given amount of damage to this entity.
     *
     * @param amount Amount of damage to deal
     */
    void damage(double amount);

    /**
     * Deals the given amount of damage to this entity, from a specified
     * entity.
     *
     * @param amount Amount of damage to deal
     * @param source Entity which to attribute this damage from
     */
    void damage(double amount, @Nullable Entity source);

    /**
     * Gets the entity's health from 0 to {@link #getMaxHealth()}, where 0 is dead.
     *
     * @return Health represented from 0 to max
     */
    double getHealth();

    /**
     * Sets the entity's health from 0 to {@link #getMaxHealth()}, where 0 is
     * dead.
     *
     * @param health New health represented from 0 to max
     * @throws IllegalArgumentException Thrown if the health is {@literal < 0 or >}
     *     {@link #getMaxHealth()}
     */
    void setHealth(double health);

    /**
     * Gets the entity's absorption amount.
     *
     * @return absorption amount from 0
     */
    double getAbsorptionAmount();

    /**
     * Sets the entity's absorption amount.
     *
     * @param amount new absorption amount from 0
     * @throws IllegalArgumentException thrown if health is {@literal < 0} or
     * non-finite.
     */
    void setAbsorptionAmount(double amount);

    /**
     * Gets the maximum health this entity has.
     *
     * @return Maximum health
     * @deprecated use {@link Attribute#GENERIC_MAX_HEALTH}.
     */
    @Deprecated
    double getMaxHealth();

    /**
     * Sets the maximum health this entity can have.
     * <p>
     * If the health of the entity is above the value provided it will be set
     * to that value.
     * <p>
     * Note: An entity with a health bar ({@link Player}, {@link EnderDragon},
     * {@link Wither}, etc...} will have their bar scaled accordingly.
     *
     * @param health amount of health to set the maximum to
     * @deprecated use {@link Attribute#GENERIC_MAX_HEALTH}.
     */
    @Deprecated
    void setMaxHealth(double health);

    /**
     * Resets the max health to the original amount.
     * @deprecated use {@link Attribute#GENERIC_MAX_HEALTH}.
     */
    @Deprecated
    void resetMaxHealth();
}
