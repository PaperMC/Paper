package org.bukkit.entity;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a Creaking.
 */
@NullMarked
public interface Creaking extends Monster {

    /**
     * Gets the home location for this Creaking (where its {@link org.bukkit.block.CreakingHeart} could be found).
     *
     * @return the location of the home
     */
    Location getHome();

    /**
     * Sets the home location for this Creaking.
     *
     * @param location the location of the home
     */
    void setHome(final Location location);

    /**
     * Activates this Creaking to target and follow a player.
     *
     * @param player the target
     */
    void activate(final Player player);

    /**
     * Deactivates from follow and attack the target.
     */
    void deactivate();

    /**
     * Gets if this Creaking is active.
     *
     * @return true if active
     */
    boolean isActive();

}
