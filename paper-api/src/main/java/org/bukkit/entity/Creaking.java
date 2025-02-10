package org.bukkit.entity;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a Creaking.
 */
@NullMarked
public interface Creaking extends Monster {

    /**
     * Gets the home location for this Creaking (aka where can be a {@link org.bukkit.block.CreakingHeart}).
     *
     * @return the location of the home.
     */
    Location getHome();

    /**
     * Sets the home location for this Creaking.
     *
     * @param location the location of the home.
     */
    void setHome(final Location location);

    /**
     * Activate this Creaking to target and follow a player.
     *
     * @param player the target.
     */
    void activate(final Player player);

    /**
     * Deactivate this Creaking to the current target player.
     */
    void deactivate();

    /**
     * Gets if this Creaking is active.
     *
     * @return true if is active.
     */
    boolean isActive();

}
