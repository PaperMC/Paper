package org.bukkit;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Raider;
import org.bukkit.persistence.PersistentDataHolder;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a raid event.
 */
@NullMarked
public interface Raid extends PersistentDataHolder {

    /**
     * Get whether this raid started.
     *
     * @return whether raid is started
     */
    boolean isStarted();

    /**
     * Gets the amount of ticks this raid has existed.
     *
     * @return active ticks
     */
    long getActiveTicks();

    /**
     * Gets the Bad Omen level of this raid.
     *
     * @return Bad Omen level (between 0 and 5)
     */
    int getBadOmenLevel();

    /**
     * Sets the Bad Omen level.
     * <br>
     * If the level is higher than 1, there will be an additional wave that as
     * strong as the final wave.
     *
     * @param badOmenLevel new Bad Omen level (from 0-5)
     * @throws IllegalArgumentException if invalid Bad Omen level
     */
    void setBadOmenLevel(int badOmenLevel);

    /**
     * Gets the center location where the raid occurs.
     *
     * @return location
     */
    Location getLocation();

    /**
     * Gets the current status of the raid.
     * <br>
     * Do not use this method to check if the raid has been started, call
     * {@link #isStarted()} instead.
     *
     * @return Raids status
     */
    RaidStatus getStatus();

    /**
     * Gets the number of raider groups which have spawned.
     *
     * @return total spawned groups
     */
    int getSpawnedGroups();

    /**
     * Gets the number of raider groups which would spawn.
     * <br>
     * This also includes the group which spawns in the additional wave (if
     * present).
     *
     * @return total groups
     */
    int getTotalGroups();

    /**
     * Gets the number of waves in this raid (exclude the additional wave).
     *
     * @return number of waves
     */
    int getTotalWaves();

    /**
     * Sets the number of waves in this raid.
     *
     * @param totalWaves number of waves
     * @throws IllegalArgumentException if totalWaves is negative or zero
     * @throws IllegalArgumentException if totalWaves is larger than 7, which is the most waves a vanilla raid can have.
     * @throws IllegalArgumentException if the totalWaves is less than {@link #getSpawnedGroups()}
     */
    void setTotalWaves(int totalWaves);

    /**
     * Gets the sum of all raider's health.
     *
     * @return total raiders health
     */
    float getTotalHealth();

    /**
     * Get the UUID of all heroes in this raid.
     *
     * @return a set of unique ids
     */
    Set<UUID> getHeroes();

    /**
     * Gets all remaining {@link Raider} in the present wave.
     *
     * @return a list of current raiders
     */
    List<Raider> getRaiders();

    /**
     * Gets the id of this raid.
     *
     * @return the raid id
     * @deprecated Raid identifiers are magic internal values and may or may not be present.
     * -1 is returned for raids without an assigned id.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    int getId();

    /**
     * Get the boss bar to be displayed for this raid.
     *
     * @return the boss bar
     */
    BossBar getBossBar();

    /**
     * Represents the status of a {@link Raid}.
     */
    enum RaidStatus {

        /**
         * The raid is in progress.
         */
        ONGOING,
        /**
         * The raid was beaten by heroes.
         */
        VICTORY,
        /**
         * The village has fallen (i.e. all villagers died).
         */
        LOSS,
        /**
         * The raid was terminated.
         */
        STOPPED;
    }
}
