package org.bukkit.block;

import java.util.Collection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.spawner.TrialSpawnerConfiguration;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured state of a trial spawner.
 *
 * @since 1.20.4
 */
public interface TrialSpawner extends TileState {

    /**
     * Gets the length in ticks the spawner will stay in cooldown for.
     *
     * @return the number of ticks
     * @since 1.21
     */
    public int getCooldownLength();

    /**
     * Sets the length in ticks the spawner will stay in cooldown for.
     *
     * @param ticks the number of ticks
     * @since 1.21
     */
    public void setCooldownLength(int ticks);

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
     * @since 1.21
     */
    public int getRequiredPlayerRange();

    /**
     * Set the maximum distance (squared) a player can be in order for this
     * spawner to be active.
     * <br>
     * Setting this value to less than or equal to 0 will make this spawner
     * always active (given that there are players online).
     *
     * @param requiredPlayerRange the maximum distance (squared) a player can be
     * in order for this spawner to be active.
     * @since 1.21
     */
    public void setRequiredPlayerRange(int requiredPlayerRange);

    /**
     * Gets the players this spawner is currently tracking.
     * <p>
     * <b>Note:</b> the returned collection is immutable, use
     * {@link #startTrackingPlayer(Player)} or {@link #stopTrackingPlayer(Player)}
     * instead.
     *
     * @return a collection of players this spawner is tracking or an empty
     *         collection if there aren't any
     * @since 1.21
     */
    @NotNull
    public Collection<Player> getTrackedPlayers();

    /**
     * Checks if this spawner is currently tracking the provided player.
     *
     * @param player the player
     * @return true if this spawner is tracking the provided player
     * @since 1.21
     */
    public boolean isTrackingPlayer(@NotNull Player player);

    /**
     * Force this spawner to start tracking the provided player.
     * <p>
     * <b>Note:</b> the spawner may decide to stop tracking this player at any given
     * time.
     *
     * @param player the player
     * @since 1.21
     */
    public void startTrackingPlayer(@NotNull Player player);

    /**
     * Force this spawner to stop tracking the provided player.
     * <p>
     * <b>Note:</b> the spawner may decide to start tracking this player again at
     * any given time.
     *
     * @param player the player
     * @since 1.21
     */
    public void stopTrackingPlayer(@NotNull Player player);

    /**
     * Gets a list of entities this spawner is currently tracking.
     * <p>
     * <b>Note:</b> the returned collection is immutable, use
     * {@link #startTrackingEntity(Entity)} or {@link #stopTrackingEntity(Entity)}
     * instead.
     *
     * @return a collection of entities this spawner is tracking or an empty
     *         collection if there aren't any
     * @since 1.21
     */
    @NotNull
    public Collection<Entity> getTrackedEntities();

    /**
     * Checks if this spawner is currently tracking the provided entity.
     *
     * @param entity the entity
     * @return true if this spawner is tracking the provided entity
     * @since 1.21
     */
    public boolean isTrackingEntity(@NotNull Entity entity);

    /**
     * Force this spawner to start tracking the provided entity.
     * <p>
     * <b>Note:</b> the spawner may decide to stop tracking this entity at any given
     * time.
     *
     * @param entity the entity
     * @since 1.21
     */
    public void startTrackingEntity(@NotNull Entity entity);

    /**
     * Force this spawner to stop tracking the provided entity.
     * <p>
     * <b>Note:</b> the spawner may decide to start tracking this entity again at
     * any given time.
     *
     * @param entity the entity
     * @since 1.21
     */
    public void stopTrackingEntity(@NotNull Entity entity);

    /**
     * Checks if this spawner is using the ominous
     * {@link TrialSpawnerConfiguration}.
     *
     * @return true is using the ominous configuration
     * @since 1.21
     */
    public boolean isOminous();

    /**
     * Changes this spawner between the normal and ominous
     * {@link TrialSpawnerConfiguration}.
     *
     * @param ominous true to use the ominous TrialSpawnerConfiguration, false to
     *                use the normal one.
     * @since 1.21
     */
    public void setOminous(boolean ominous);

    /**
     * Gets the {@link TrialSpawnerConfiguration} used when {@link #isOminous()} is
     * false.
     *
     * @return the TrialSpawnerConfiguration
     * @since 1.21
     */
    @NotNull
    public TrialSpawnerConfiguration getNormalConfiguration();

    /**
     * Gets the {@link TrialSpawnerConfiguration} used when {@link #isOminous()} is
     * true.
     *
     * @return the TrialSpawnerConfiguration
     * @since 1.21
     */
    @NotNull
    public TrialSpawnerConfiguration getOminousConfiguration();
}
