package org.bukkit.entity;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Fireball.
 */
public interface Fireball extends Projectile, Explosive {

    /**
     * Sets the direction the fireball should be flying towards.
     * The direction vector will be normalized and the default speed will be applied.
     * <br>
     * To also change the speed of the fireball, use {@link #setVelocity(Vector)}.
     * <b>Note:</b> that the client may not respect non-default speeds and will therefore
     * mispredict the location of the fireball, causing visual stutter.
     * <br>
     * <b>Also Note:</b> that this method and {@link #setVelocity(Vector)} will override each other.
     *
     * @param direction the direction this fireball should be flying towards
     * @see #setVelocity(Vector)
     */
    public void setDirection(@NotNull Vector direction);

    /**
     * Retrieve the direction this fireball is heading toward
     * The returned vector is not normalized.
     *
     * @return the direction
     */
    @NotNull
    public Vector getDirection();

}
