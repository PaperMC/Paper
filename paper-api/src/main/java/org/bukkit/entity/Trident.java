package org.bukkit.entity;

/**
 * Represents a thrown trident.
 */
// Paper start
public interface Trident extends AbstractArrow, ThrowableProjectile {

    /**
     * Returns whether the trident has an enchanted glow.
     * This can be separate from the underlying item having any enchantments.
     *
     * @return whether the trident has an enchanted glow
     */
    boolean hasGlint();

    /**
     * Sets whether the trident has an enchanted glow.
     * This is separate from the underlying item having any enchantments.
     *
     * @param glint whether the trident should have an enchanted glow
     */
    void setGlint(boolean glint);

    /**
     * Returns the loyalty level of the trident.
     * This can be separate from the underlying item's enchantments.
     *
     * @return loyalty level of the trident
     */
    int getLoyaltyLevel();

    /**
     * Sets the loyalty level of the trident.
     * This is separate from the underlying item's enchantments.
     *
     * @param loyaltyLevel loyalty level
     * @throws IllegalArgumentException if the loyalty level is lower than 0 or greater than 127
     */
    void setLoyaltyLevel(int loyaltyLevel);

    /**
     * Gets if this trident has dealt damage to an
     * entity yet or has hit the floor.
     *
     * If neither of these events have occurred yet, this will
     * return false.
     *
     * @return has dealt damage
     */
    boolean hasDealtDamage();

    /**
     * Sets if this trident has dealt damage to an entity
     * yet or has hit the floor.
     *
     * @param hasDealtDamage has dealt damage or hit the floor
     */
    void setHasDealtDamage(boolean hasDealtDamage);
}
// Paper end
