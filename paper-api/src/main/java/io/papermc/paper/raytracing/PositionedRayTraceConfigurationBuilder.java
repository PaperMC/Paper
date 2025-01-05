package io.papermc.paper.raytracing;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.List;
import java.util.function.Predicate;

/**
 * Holds information about how to cast a raytrace.
 */
@NullMarked
public interface PositionedRayTraceConfigurationBuilder {

    /**
     * Gets the starting location.
     *
     * @return the starting location
     */
    @Nullable
    Location start();

    /**
     * Sets the starting location.
     *
     * @param start the new starting location
     * @return a reference to this object
     */
    @Contract("_ -> this")
    PositionedRayTraceConfigurationBuilder start(Location start);

    /**
     * Gets the direction.
     *
     * @return the direction
     */
    @Nullable
    Vector direction();

    /**
     * Sets the direction.
     *
     * @param direction the new direction
     * @return a reference to this object
     */
    @Contract("_ -> this")
    PositionedRayTraceConfigurationBuilder direction(Vector direction);

    /**
     * Gets the maximum distance.
     *
     * @return the maximum distance
     */
    double maxDistance();

    /**
     * Sets the maximum distance.
     *
     * @param maxDistance the new maxDistance
     * @return a reference to this object
     */
    @Contract("_ -> this")
    PositionedRayTraceConfigurationBuilder maxDistance(double maxDistance);

    /**
     * Gets the FluidCollisionMode when looking for block collisions.
     *
     * @return the FluidCollisionMode
     */
    FluidCollisionMode fluidCollisionMode();

    /**
     * Sets the FluidCollisionMode when looking for block collisions.
     *
     * @param fluidCollisionMode the new FluidCollisionMode
     * @return a reference to this object
     */
    @Contract("_ -> this")
    PositionedRayTraceConfigurationBuilder fluidCollisionMode(FluidCollisionMode fluidCollisionMode);

    /**
     * Gets if the raytrace will ignore passable blocks when looking for block collisions.
     *
     * @return if the raytrace will ignore passable blocks
     */
    boolean ignorePassableBlocks();

    /**
     * Gets if the raytrace will ignore passable blocks when looking for block collisions.
     *
     * @param ignorePassableBlocks if the raytrace should ignore passable blocks
     * @return a reference to this object
     */
    @Contract("_ -> this")
    PositionedRayTraceConfigurationBuilder ignorePassableBlocks(boolean ignorePassableBlocks);

    /**
     * Gets the size of the raytrace when looking for entity collisions.
     *
     * @return the raytrace size
     */
    double raySize();

    /**
     * Sets the size of the raytrace when looking for entity collisions.
     *
     * @param raySize the new raytrace size
     * @return a reference to this object
     */
    @Contract("_ -> this")
    PositionedRayTraceConfigurationBuilder raySize(double raySize);

    /**
     * Gets the current entity filter when looking for entity collisions.
     *
     * @return predicate for entities the ray can potentially collide with, or null to consider all entities
     */
    @Nullable
    Predicate<? super Entity> entityFilter();

    /**
     * Sets the current entity filter when looking for entity collisions.
     *
     * @param entityFilter predicate for entities the ray can potentially collide with, or null to consider all entities
     * @return a reference to this object
     */
    @Contract("_ -> this")
    PositionedRayTraceConfigurationBuilder entityFilter(@Nullable Predicate<? super Entity> entityFilter);

    /**
     * Gets the current block filter when looking for block collisions.
     *
     * @return predicate for blocks the ray can potentially collide with, or null to consider all blocks
     */
    @Nullable
    Predicate<? super Block> blockFilter();

    /**
     * Sets the current block filter when looking for block collisions.
     *
     * @param blockFilter predicate for blocks the ray can potentially collide with, or null to consider all blocks
     * @return a reference to this object
     */
    @Contract("_ -> this")
    PositionedRayTraceConfigurationBuilder blockFilter(@Nullable Predicate<? super Block> blockFilter);

    /**
     * Gets the current set targets.
     *
     * @return the targets
     */
    @Nullable
    List<RayTraceTargets> targets();

    /**
     * Sets the targets for the rayTrace.
     *
     * @return a reference to this object
     */
    @Contract("_, _ -> this")
    PositionedRayTraceConfigurationBuilder targets(RayTraceTargets first, RayTraceTargets... others);
}
