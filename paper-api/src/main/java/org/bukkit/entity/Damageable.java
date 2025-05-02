package org.bukkit.entity;

import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
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
     * Deals the given amount of damage to this entity from a specified
     * {@link Entity}.
     *
     * @param amount amount of damage to deal
     * @param source entity to which the damage should be attributed
     */
    void damage(double amount, @Nullable Entity source);

    /**
     * Deals the given amount of damage to this entity from a specified
     * {@link DamageSource}.
     *
     * @param amount amount of damage to deal
     * @param damageSource source to which the damage should be attributed
     */
    @ApiStatus.Experimental
    void damage(double amount, @NotNull DamageSource damageSource);

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
     * Heal this entity by the given amount. This will call {@link org.bukkit.event.entity.EntityRegainHealthEvent}.
     *
     * @param amount heal amount
     */
    default void heal(final double amount) {
        this.heal(amount, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.CUSTOM);
    }

    /**
     * Heal this entity by the given amount. This will call {@link org.bukkit.event.entity.EntityRegainHealthEvent}.
     *
     * @param amount heal amount
     * @param reason heal reason
     */
    void heal(double amount, @NotNull org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason reason);

    /**
     * Gets the entity's absorption amount.
     *
     * @return absorption amount from 0
     */
    double getAbsorptionAmount();

    /**
     * Sets the entity's absorption amount.
     * <p>
     * Note: The amount is capped to the value of
     * {@link Attribute#MAX_ABSORPTION}. The effect of this method on
     * that attribute is currently unspecified and subject to change.
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
     * @deprecated use {@link Attribute#MAX_HEALTH}.
     */
    @Deprecated(since = "1.11")
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
     * @deprecated use {@link Attribute#MAX_HEALTH}.
     */
    @Deprecated(since = "1.11")
    void setMaxHealth(double health);

    /**
     * Resets the max health to the original amount.
     * @deprecated use {@link Attribute#MAX_HEALTH}.
     */
    @Deprecated(since = "1.11")
    void resetMaxHealth();
}
