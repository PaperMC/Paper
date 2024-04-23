package org.bukkit.inventory.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an item that has durability and can take damage.
 */
public interface Damageable extends ItemMeta {

    /**
     * Checks to see if this item has damage
     *
     * @return true if this has damage
     */
    boolean hasDamage();

    /**
     * Gets the damage
     *
     * @return the damage
     */
    int getDamage();

    /**
     * Sets the damage
     *
     * @param damage item damage
     */
    void setDamage(int damage);

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
