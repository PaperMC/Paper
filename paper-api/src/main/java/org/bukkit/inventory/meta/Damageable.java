package org.bukkit.inventory.meta;

/**
 * Represents an item that has durability and can take damage.
 */
public interface Damageable {

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

    Damageable clone();
}
