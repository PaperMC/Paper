package org.bukkit.block;

import java.util.Collection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.spawner.TrialSpawnerConfiguration;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a captured state of a trial spawner.
 */
@NullMarked
public interface TrialSpawner extends TileState {

    /**
     * Gets the game time in ticks when the cooldown ends. 0 if not currently in cooldown.
     *
     * @return the game time in ticks
     * @see org.bukkit.World#getGameTime()
     */
    long getCooldownEnd();

    /**
     * Sets the game time in ticks when the cooldown ends.
     *
     * @param ticks the game time in ticks for the new cooldown
     */
    void setCooldownEnd(long ticks);

    /**
     * Gets the game time in ticks when the next spawn attempt happens. 0 if not currently active.
     *
     * @return the game time in ticks
     * @see org.bukkit.World#getGameTime()
     */
    long getNextSpawnAttempt();

    /**
     * Sets the game time in ticks when the next spawn attempt happens.
     *
     * @param ticks the game time in ticks for the next mob spawn
     */
    void setNextSpawnAttempt(long ticks);

    /**
     * Gets the length in ticks the spawner will stay in cooldown for.
     *
     * @return the number of ticks
     */
    int getCooldownLength();

    /**
     * Sets the length in ticks the spawner will stay in cooldown for.
     *
     * @param ticks the number of ticks
     */
    void setCooldownLength(int ticks);

    /**
     * Get the maximum distance(squared) a player can be in order for this
     * spawner to be active.
     * <br>
     * If this value is less than or equal to 0, this spawner is always active
     * (given that there are players online).
     * <br>
     * Default value is 16.
     *
     * @return the maximum distance(squared) a player can be in order for this
     * spawner to be active.
     */
    int getRequiredPlayerRange();

    /**
     * Set the maximum distance (squared) a player can be in order for this
     * spawner to be active.
     * <br>
     * Setting this value to less than or equal to 0 will make this spawner
     * always active (given that there are players online).
     *
     * @param requiredPlayerRange the maximum distance (squared) a player can be
     * in order for this spawner to be active.
     */
    void setRequiredPlayerRange(int requiredPlayerRange);

    /**
     * Gets the players this spawner is currently tracking.
     * <p>
     * <b>Note:</b> the returned collection is immutable, use
     * {@link #startTrackingPlayer(Player)} or {@link #stopTrackingPlayer(Player)}
     * instead.
     *
     * @return a collection of players this spawner is tracking or an empty
     *         collection if there aren't any
     */
    Collection<Player> getTrackedPlayers();

    /**
     * Checks if this spawner is currently tracking the provided player.
     *
     * @param player the player
     * @return true if this spawner is tracking the provided player
     */
    boolean isTrackingPlayer(final Player player);

    /**
     * Force this spawner to start tracking the provided player.
     * <p>
     * <b>Note:</b> the spawner may decide to stop tracking this player at any given
     * time.
     *
     * @param player the player
     */
    void startTrackingPlayer(final Player player);

    /**
     * Force this spawner to stop tracking the provided player.
     * <p>
     * <b>Note:</b> the spawner may decide to start tracking this player again at
     * any given time.
     *
     * @param player the player
     */
    void stopTrackingPlayer(final Player player);

    /**
     * Gets a list of entities this spawner is currently tracking.
     * <p>
     * <b>Note:</b> the returned collection is immutable, use
     * {@link #startTrackingEntity(Entity)} or {@link #stopTrackingEntity(Entity)}
     * instead.
     *
     * @return a collection of entities this spawner is tracking or an empty
     *         collection if there aren't any
     */
    Collection<Entity> getTrackedEntities();

    /**
     * Checks if this spawner is currently tracking the provided entity.
     *
     * @param entity the entity
     * @return true if this spawner is tracking the provided entity
     */
    boolean isTrackingEntity(final Entity entity);

    /**
     * Force this spawner to start tracking the provided entity.
     * <p>
     * <b>Note:</b> the spawner may decide to stop tracking this entity at any given
     * time.
     *
     * @param entity the entity
     */
    void startTrackingEntity(final Entity entity);

    /**
     * Force this spawner to stop tracking the provided entity.
     * <p>
     * <b>Note:</b> the spawner may decide to start tracking this entity again at
     * any given time.
     *
     * @param entity the entity
     */
    void stopTrackingEntity(final Entity entity);

    /**
     * Checks if this spawner is using the ominous
     * {@link TrialSpawnerConfiguration}.
     *
     * @return true is using the ominous configuration
     */
    boolean isOminous();

    /**
     * Changes this spawner between the normal and ominous
     * {@link TrialSpawnerConfiguration}.
     *
     * @param ominous true to use the ominous TrialSpawnerConfiguration, false to
     *                use the normal one.
     */
    void setOminous(boolean ominous);

    /**
     * Gets the {@link TrialSpawnerConfiguration} used when {@link #isOminous()} is
     * false.
     *
     * @return the TrialSpawnerConfiguration
     */
    @ApiStatus.Experimental
    TrialSpawnerConfiguration getNormalConfiguration();

    /**
     * Gets the {@link TrialSpawnerConfiguration} used when {@link #isOminous()} is
     * true.
     *
     * @return the TrialSpawnerConfiguration
     */
    @ApiStatus.Experimental
    TrialSpawnerConfiguration getOminousConfiguration();
}
