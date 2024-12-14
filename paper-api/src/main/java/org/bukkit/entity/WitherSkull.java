package org.bukkit.entity;

/**
 * Represents a wither skull {@link Fireball}.
 *
 * @since 1.4.5 R1.0
 */
public interface WitherSkull extends Fireball {

    /**
     * Sets the charged status of the wither skull.
     *
     * @param charged whether it should be charged
     * @since 1.7.10
     */
    public void setCharged(boolean charged);

    /**
     * Gets whether or not the wither skull is charged.
     *
     * @return whether the wither skull is charged
     * @since 1.7.10
     */
    public boolean isCharged();
}
