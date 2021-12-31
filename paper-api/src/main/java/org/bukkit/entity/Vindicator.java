package org.bukkit.entity;

/**
 * Represents a Vindicator.
 */
public interface Vindicator extends Illager {

    /**
     * Returns whether a vindicator is in "Johnny" mode.
     *
     * When this mode is active, vindicators will be hostile to all mobs.
     *
     * @return true if johnny
     */
    boolean isJohnny();

    /**
     * Sets the Johnny state of a vindicator.
     *
     * @param johnny new johnny state
     */
    void setJohnny(boolean johnny);
}
