package io.papermc.paper.entity;

import org.bukkit.entity.Fish;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a fish that can school with other fish.
 */
@NullMarked
public interface SchoolableFish extends Fish {

    /**
     * Forces this fish to follow the given fish.
     *
     * @param leader fish to follow
     */
    void startFollowing(SchoolableFish leader);

    /**
     * Causes the fish to stop following their current
     * leader.
     */
    void stopFollowing();

    /**
     * Gets the amount of fish currently following this fish.
     *
     * @return school size
     */
    int getSchoolSize();

    /**
     * Gets the maximum number of fish that will naturally follow this fish.
     *
     * @return max school size
     */
    int getMaxSchoolSize();

    /**
     * Gets the fish that this entity is currently following.
     *
     * @return following fish
     */
    @Nullable SchoolableFish getSchoolLeader();

}
