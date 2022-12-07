package org.bukkit.entity;

/**
 * Represents a Camel.
 */
public interface Camel extends AbstractHorse {

    /**
     * Gets whether this camel is dashing (sprinting).
     *
     * @return dashing status
     */
    boolean isDashing();

    /**
     * Sets whether this camel is dashing (sprinting).
     *
     * @param dashing new dashing status
     */
    void setDashing(boolean dashing);
}
