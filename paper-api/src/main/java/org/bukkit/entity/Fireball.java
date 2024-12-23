package org.bukkit.entity;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Fireball.
 *
 * @since 1.0.0
 */
public interface Fireball extends Projectile, Explosive {

    /**
     * Sets the direction the fireball should be flying towards.
     * <br>
     * This is a convenience method, it will change the velocity direction and
     * acceleration direction, while keeping the power the same.
     * <br>
     * <b>Note:</b> This method only uses the direction of the vector and will
     * normalize (a copy of) it.
     * <br>
     * <b>Special Case:</b> When the given direction is
     * {@link Vector#isZero() zero}, the velocity and acceleration will also be
     * set to zero without keeping the power.
     *
     * @param direction the direction this fireball should be flying towards
     * @see #setVelocity(Vector)
     * @see #setAcceleration(Vector)
     */
    public void setDirection(@NotNull Vector direction);

    /**
     * Retrieve the direction this fireball is heading toward.
     * The returned vector is not normalized.
     *
     * @return the direction
     * @see #getAcceleration()
     * @deprecated badly named method, returns the value of
     * {@link #getAcceleration()}
     */
    @NotNull
    @Deprecated(since = "1.20.6")
    public Vector getDirection();

    /**
     * Sets the acceleration of the fireball.
     *
     * The acceleration gets applied to the velocity every tick, depending on
     * the specific type of the fireball a damping / drag factor is applied so
     * that the velocity does not grow into infinity.
     * <br>
     * <b>Note:</b> that the client may not respect non-default acceleration
     * power and will therefore mispredict the location of the fireball, causing
     * visual stutter.
     *
     * @param acceleration the acceleration
     * @since 1.20.6
     */
    void setAcceleration(@NotNull Vector acceleration);

    /**
     * Retrieve the acceleration of this fireball.
     *
     * @return the acceleration
     * @since 1.20.6
     */
    @NotNull
    Vector getAcceleration();

    // Paper start - Expose power on fireball projectiles
    /**
     * Sets the power of a fireball. The power determines the direction and magnitude of its acceleration.
     *
     * @param power the power
     * @deprecated use #setAcceleration(Vector) instead.
     * @since 1.20.4
     */
    @Deprecated
    public void setPower(@NotNull Vector power);

    /**
     * Gets the power of a fireball. The power determines the direction and magnitude of its acceleration.
     *
     * @return the power
     * @deprecated Use #getAcceleration instead.
     * @since 1.20.4
     */
    @Deprecated
    @NotNull
    public Vector getPower();
    // Paper end - Expose power on fireball projectiles
}
