package org.bukkit.entity;

/**
 * Represents a Wind Charge.
 */
public interface AbstractWindCharge extends Fireball {

    /**
     * Immediately explode this WindCharge.
     */
    public void explode();

    /**
     * Gets the radius of the explosion created by this wind charge.
     *
     * @return the explosion radius
     */
    float getExplosionRadius();

    /**
     * Sets the radius of the explosion created by this wind charge.
     *
     * @param radius the explosion radius
     */
    void setExplosionRadius(float radius);

    /**
     * Returns whether this wind charge was created by a player.
     *
     * @return true if player created
     */
    boolean isPlayerCreated();

}
