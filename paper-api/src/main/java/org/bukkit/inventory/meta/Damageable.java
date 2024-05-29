package org.bukkit.inventory.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an item that has durability and can take damage.
 */
public interface Damageable extends ItemMeta {

    /**
     * Checks to see if this item has damage greater than 0.
     *
     * @return true if this has damage > 0
     */
    boolean hasDamage();

    /**
     * Gets the damage
     * <p>
     * Call {@link #hasDamageValue()} to be sure
     * a damage value is set.
     *
     * @return the damage
     */
    int getDamage();

    /**
     * Sets the damage
     *
     * @param damage item damage
     * @see #resetDamage() to reset and clear the damage data component
     */
    void setDamage(int damage);

    // Paper start
    /**
     * Checks if any damage value, including 0,
     * is set on this meta.
     *
     * @return true if any value is set
     */
    boolean hasDamageValue();

    /**
     * Clears the damage component from the meta. Differs
     * from {@code setDamage(0)} in that it removes the component
     * instead of adding the component with a value of 0.
     */
    void resetDamage();
    // Paper end

    /**
     * Checks to see if this item has a maximum amount of damage.
     *
     * @return true if this has maximum amount of damage
     */
    boolean hasMaxDamage();

    /**
     * Gets the maximum amount of damage.
     *
     * Plugins should check {@link #hasMaxDamage()} before calling this method.
     *
     * @return the maximum amount of damage
     */
    int getMaxDamage();

    /**
     * Sets the maximum amount of damage.
     *
     * @param maxDamage maximum amount of damage
     */
    void setMaxDamage(@Nullable Integer maxDamage);

    @NotNull
    @Override
    Damageable clone();
}
