package org.bukkit.entity;

import org.bukkit.MinecraftExperimental;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a Camel.
 *
 * @apiNote This entity is part of an experimental feature of Minecraft and
 * hence subject to change.
 */
@MinecraftExperimental
@ApiStatus.Experimental
public interface Camel extends AbstractHorse, Sittable {

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
