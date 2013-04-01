package org.bukkit.entity;

/**
 * Represents a fishing hook.
 */
public interface FishHook extends Projectile {
    /**
     * Gets the chance of a fish biting.
     * <p>
     * 0.0 = No Chance.<br>
     * 1.0 = Instant catch.
     *
     * @return chance the bite chance
     */
    public double getBiteChance();

    /**
     * Sets the chance of a fish biting.
     * <p>
     * 0.0 = No Chance.<br>
     * 1.0 = Instant catch.
     *
     * @param chance the bite chance
     * @throws IllegalArgumentException if the bite chance is not between 0
     *     and 1
     */
    public void setBiteChance(double chance) throws IllegalArgumentException;
}
