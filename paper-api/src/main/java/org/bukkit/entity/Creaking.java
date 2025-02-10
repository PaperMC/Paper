package org.bukkit.entity;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a Creaking.
 */
@NullMarked
public interface Creaking extends Monster {

    /**
     * Gets the home location for this Creaking (where its {@link org.bukkit.block.CreakingHeart} could be found).
     *
     * @return the location of the home if available, null otherwise
     */
    @Nullable
    Location getHome();

    /**
     * Detach the home for this Creaking.
     */
    void detachHome();

    /**
     * Activates this creaking to target and follow a player.
     *
     * @param player the target
     */
    void activate(final Player player);

    /**
     * Deactivates the creaking, clearing its current attack target and
     * marking it as inactive.
     */
    void deactivate();

    /**
     * Gets if this Creaking is active.
     *
     * @return true if active
     */
    boolean isActive();

}
