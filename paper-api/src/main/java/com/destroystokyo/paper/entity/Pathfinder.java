package com.destroystokyo.paper.entity;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Handles pathfinding operations for an Entity
 */
@NullMarked
public interface Pathfinder {

    /**
     * @return The entity that is controlled by this pathfinder
     */
    Mob getEntity();

    /**
     * Instructs the Entity to stop trying to navigate to its current desired location
     */
    void stopPathfinding();

    /**
     * If the entity is currently trying to navigate to a destination, this will return true
     *
     * @return true if the entity is navigating to a destination
     */
    boolean hasPath();

    /**
     * @return The location the entity is trying to navigate to, or null if there is no destination
     */
    @Nullable PathResult getCurrentPath();

    /**
     * Calculates a destination for the Entity to navigate to, but does not set it
     * as the current target. Useful for calculating what would happen before setting it.
     *
     * @param loc Location to navigate to
     * @return The closest Location the Entity can get to for this navigation, or null if no path could be calculated
     */
    @Nullable PathResult findPath(Location loc);

    /**
     * Calculates a destination for the Entity to navigate to to reach the target entity,
     * but does not set it as the current target.
     * Useful for calculating what would happen before setting it.
     * <p>
     * The behavior of this PathResult is subject to the games pathfinding rules, and may
     * result in the pathfinding automatically updating to follow the target Entity.
     * <p>
     * However, this behavior is not guaranteed, and is subject to the game's behavior.
     *
     * @param target the Entity to navigate to
     * @return The closest Location the Entity can get to for this navigation, or null if no path could be calculated
     */
    @Nullable PathResult findPath(LivingEntity target);

    /**
     * Calculates a destination for the Entity to navigate to, and sets it with default speed
     * as the current target.
     *
     * @param loc Location to navigate to
     * @return If the pathfinding was successfully started
     */
    default boolean moveTo(Location loc) {
        return this.moveTo(loc, 1);
    }

    /**
     * Calculates a destination for the Entity to navigate to, with desired speed
     * as the current target.
     *
     * @param loc   Location to navigate to
     * @param speed Speed multiplier to navigate at, where 1 is 'normal'
     * @return If the pathfinding was successfully started
     */
    default boolean moveTo(Location loc, double speed) {
        PathResult path = this.findPath(loc);
        return path != null && this.moveTo(path, speed);
    }

    /**
     * Calculates a destination for the Entity to navigate to to reach the target entity,
     * and sets it with default speed.
     * <p>
     * The behavior of this PathResult is subject to the games pathfinding rules, and may
     * result in the pathfinding automatically updating to follow the target Entity.
     * <p>
     * However, this behavior is not guaranteed, and is subject to the game's behavior.
     *
     * @param target the Entity to navigate to
     * @return If the pathfinding was successfully started
     */
    default boolean moveTo(LivingEntity target) {
        return this.moveTo(target, 1);
    }

    /**
     * Calculates a destination for the Entity to navigate to to reach the target entity,
     * and sets it with specified speed.
     * <p>
     * The behavior of this PathResult is subject to the games pathfinding rules, and may
     * result in the pathfinding automatically updating to follow the target Entity.
     * <p>
     * However, this behavior is not guaranteed, and is subject to the game's behavior.
     *
     * @param target the Entity to navigate to
     * @param speed  Speed multiplier to navigate at, where 1 is 'normal'
     * @return If the pathfinding was successfully started
     */
    default boolean moveTo(LivingEntity target, double speed) {
        PathResult path = this.findPath(target);
        return path != null && this.moveTo(path, speed);
    }

    /**
     * Takes the result of a previous pathfinding calculation and sets it
     * as the active pathfinding with default speed.
     *
     * @param path The Path to start following
     * @return If the pathfinding was successfully started
     */
    default boolean moveTo(PathResult path) {
        return this.moveTo(path, 1);
    }

    /**
     * Takes the result of a previous pathfinding calculation and sets it
     * as the active pathfinding,
     *
     * @param path  The Path to start following
     * @param speed Speed multiplier to navigate at, where 1 is 'normal'
     * @return If the pathfinding was successfully started
     */
    boolean moveTo(PathResult path, double speed);

    /**
     * Checks if this pathfinder allows passing through closed doors.
     *
     * @return if this pathfinder allows passing through closed doors
     */
    boolean canOpenDoors();

    /**
     * Allows this pathfinder to pass through closed doors, or not
     *
     * @param canOpenDoors if the mob can pass through closed doors, or not
     */
    void setCanOpenDoors(boolean canOpenDoors);

    /**
     * Checks if this pathfinder allows passing through open doors.
     *
     * @return if this pathfinder allows passing through open doors
     */
    boolean canPassDoors();

    /**
     * Allows this pathfinder to pass through open doors, or not
     *
     * @param canPassDoors if the mob can pass through open doors, or not
     */
    void setCanPassDoors(boolean canPassDoors);

    /**
     * Checks if this pathfinder assumes that the mob can float
     *
     * @return if this pathfinder assumes that the mob can float
     */
    boolean canFloat();

    /**
     * Makes this pathfinder assume that the mob can float, or not
     *
     * @param canFloat if the mob can float, or not
     */
    void setCanFloat(boolean canFloat);

    /**
     * Represents the result of a pathfinding calculation
     */
    interface PathResult {

        /**
         * All currently calculated points to follow along the path to reach the destination location
         * <p>
         * Will return points the entity has already moved past, see {@link #getNextPointIndex()}
         *
         * @return List of points
         */
        List<Location> getPoints();

        /**
         * @return Returns the index of the current point along the points returned in {@link #getPoints()} the entity
         * is trying to reach. This value will be higher than the maximum index of {@link #getPoints()} if this path finding is done.
         */
        int getNextPointIndex();

        /**
         * @return The next location in the path points the entity is trying to reach, or null if there is no next point
         */
        @Nullable Location getNextPoint();

        /**
         * @return The closest point the path can get to the target location
         */
        @Nullable Location getFinalPoint();

        /**
         * Checks whether the final point can be reached
         *
         * @return whether the final point can be reached
         * @see #getFinalPoint()
         */
        boolean canReachFinalPoint();
    }
}
