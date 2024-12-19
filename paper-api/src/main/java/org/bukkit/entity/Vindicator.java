package org.bukkit.entity;

/**
 * Represents a Vindicator.
 *
 * @since 1.11
 */
public interface Vindicator extends Illager {

    /**
     * Returns whether a vindicator is in "Johnny" mode.
     *
     * When this mode is active, vindicators will be hostile to all mobs.
     *
     * @return true if johnny
     * @since 1.13.1
     */
    boolean isJohnny();

    /**
     * Sets the Johnny state of a vindicator.
     *
     * @param johnny new johnny state
     * @since 1.13.1
     */
    void setJohnny(boolean johnny);
}
