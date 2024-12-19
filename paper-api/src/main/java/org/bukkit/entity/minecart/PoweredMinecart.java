package org.bukkit.entity.minecart;

import org.bukkit.entity.Minecart;

/**
 * Represents a powered minecart. A powered minecart moves on its own when a
 * player deposits {@link org.bukkit.Material#COAL fuel}.
 *
 * @since 1.5.1 R0.2
 */
public interface PoweredMinecart extends Minecart {

    /**
     * Get the number of ticks until the minecart runs out of fuel.
     *
     * @return Number of ticks until the minecart runs out of fuel
     * @since 1.16.3
     */
    public int getFuel();

    /**
     * Set the number of ticks until the minecart runs out of fuel.
     *
     * @param fuel Number of ticks until the minecart runs out of fuel
     * @since 1.16.3
     */
    public void setFuel(int fuel);

    // Paper start
    /**
     * Get the x push of the minecart.
     *
     * @return The x push of the minecart
     * @since 1.18.2
     */
    public double getPushX();

    /**
     * Get the z push of the minecart.
     *
     * @return The z push of the minecart
     * @since 1.18.2
     */
    public double getPushZ();

    /**
     * Set the x push of the minecart.
     *
     * @param xPush The new x push of the minecart
     * @since 1.18.2
     */
    public void setPushX(double xPush);

    /**
     * Set the z push of the minecart.
     *
     * @param zPush The new z push of the minecart
     * @since 1.18.2
     */
    public void setPushZ(double zPush);
    // Paper end
}
