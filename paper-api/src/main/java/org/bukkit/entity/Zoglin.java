package org.bukkit.entity;

/**
 * Represents a Zoglin.
 */
public interface Zoglin extends Monster, Ageable {

    /**
     * Gets whether the zoglin is a baby
     *
     * @return Whether the zoglin is a baby
     * @deprecated see {@link Ageable#isAdult()}
     */
    @Deprecated
    public boolean isBaby();

    /**
     * Sets whether the zoglin is a baby
     *
     * @param flag Whether the zoglin is a baby
     * @deprecated see {@link Ageable#setBaby()} and {@link Ageable#setAdult()}
     */
    @Deprecated
    public void setBaby(boolean flag);
}
