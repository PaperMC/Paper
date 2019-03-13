package org.bukkit.entity;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Fireball.
 */
public interface Fireball extends Projectile, Explosive {

    /**
     * Fireballs fly straight and do not take setVelocity(...) well.
     *
     * @param direction the direction this fireball is flying toward
     */
    public void setDirection(@NotNull Vector direction);

    /**
     * Retrieve the direction this fireball is heading toward
     *
     * @return the direction
     */
    @NotNull
    public Vector getDirection();

}
