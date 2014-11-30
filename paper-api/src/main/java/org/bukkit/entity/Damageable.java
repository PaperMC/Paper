package org.bukkit.entity;

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
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     * 
     * @param amount Amount of damage to deal
     */
    @Deprecated
    void _INVALID_damage(int amount);

    /**
     * Deals the given amount of damage to this entity, from a specified
     * entity.
     *
     * @param amount Amount of damage to deal
     * @param source Entity which to attribute this damage from
     */
    void damage(double amount, Entity source);

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @param amount Amount of damage to deal
     * @param source Entity which to attribute this damage from
     */
    @Deprecated
    void _INVALID_damage(int amount, Entity source);

    /**
     * Gets the entity's health from 0 to {@link #getMaxHealth()}, where 0 is dead.
     *
     * @return Health represented from 0 to max
     */
    double getHealth();

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     * 
     * @return Health represented from 0 to max
     */
    @Deprecated
    int _INVALID_getHealth();

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
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @param health New health represented from 0 to max
     * @throws IllegalArgumentException Thrown if the health is {@literal < 0 or >}
     *     {@link #getMaxHealth()}
     */
    @Deprecated
    void _INVALID_setHealth(int health);

    /**
     * Gets the maximum health this entity has.
     *
     * @return Maximum health
     */
    double getMaxHealth();

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     * 
     * @return Maximum health
     */
    @Deprecated
    int _INVALID_getMaxHealth();

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
     */
    void setMaxHealth(double health);

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @param health amount of health to set the maximum to
     */
    @Deprecated
    void _INVALID_setMaxHealth(int health);

    /**
     * Resets the max health to the original amount.
     */
    void resetMaxHealth();
}
