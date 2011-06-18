package org.bukkit.entity;

import org.bukkit.util.Vector;

/**
 * Represents a Fireball.
 */
public interface Fireball extends Projectile, Explosive {
    /**
     * Fireballs fly straight and do not take setVelocity(...) well.
     *
     * @param direction
     *            the direction this fireball is flying toward
     */
    public void setDirection(Vector direction);

    /**
     * Retrieve the direction this fireball is heading toward
     *
     * @return the direction
     */
    public Vector getDirection();

}
