package org.bukkit.entity.minecart;

import org.bukkit.entity.Minecart;

/**
 * Represents a powered minecart. A powered minecart moves on its own when a
 * player deposits {@link org.bukkit.Material#COAL fuel}.
 */
public interface PoweredMinecart extends Minecart {

    /**
     * Get the number of ticks until the minecart runs out of fuel.
     *
     * @return Number of ticks until the minecart runs out of fuel
     */
    public int getFuel();

    /**
     * Set the number of ticks until the minecart runs out of fuel.
     *
     * @param fuel Number of ticks until the minecart runs out of fuel
     */
    public void setFuel(int fuel);

    // Paper start
    /**
     * Get the x push of the minecart.
     *
     * @return The x push of the minecart
     */
    public double getPushX();

    /**
     * Get the z push of the minecart.
     *
     * @return The z push of the minecart
     */
    public double getPushZ();

    /**
     * Set the x push of the minecart.
     *
     * @param xPush The new x push of the minecart
     */
    public void setPushX(double xPush);

    /**
     * Set the z push of the minecart.
     *
     * @param zPush The new z push of the minecart
     */
    public void setPushZ(double zPush);
    // Paper end
}
