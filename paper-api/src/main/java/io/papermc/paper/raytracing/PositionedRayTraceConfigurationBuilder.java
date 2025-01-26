package io.papermc.paper.raytracing;

import java.util.function.Predicate;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * A builder for configuring a raytrace with a starting location
 * and direction.
 */
@NullMarked
public interface PositionedRayTraceConfigurationBuilder {

    /**
     * Sets the starting location.
     *
     * @param start the new starting location
     * @return a reference to this object
     */
    @Contract(value = "_ -> this", mutates = "this")
    PositionedRayTraceConfigurationBuilder start(Location start);

    /**
     * Sets the direction.
     *
     * @param direction the new direction
     * @return a reference to this object
     */
    @Contract(value = "_ -> this", mutates = "this")
    PositionedRayTraceConfigurationBuilder direction(Vector direction);

    /**
     * Sets the maximum distance.
     *
     * @param maxDistance the new maxDistance
     * @return a reference to this object
     */
    @Contract(value = "_ -> this", mutates = "this")
    PositionedRayTraceConfigurationBuilder maxDistance(@NonNegative double maxDistance);

    /**
     * Sets the FluidCollisionMode when looking for block collisions.
     * <p>
     * If collisions with passable blocks are ignored, fluid collisions are
     * ignored as well regardless of the fluid collision mode.
     *
     * @param fluidCollisionMode the new FluidCollisionMode
     * @return a reference to this object
     */
    @Contract(value = "_ -> this", mutates = "this")
    PositionedRayTraceConfigurationBuilder fluidCollisionMode(FluidCollisionMode fluidCollisionMode);

    /**
     * Sets whether the raytrace should ignore passable blocks when looking for
     * block collisions.
     * <p>
     * If collisions with passable blocks are ignored, fluid collisions are
     * ignored as well regardless of the fluid collision mode.
     * <p>
     * Portal blocks are only considered passable if the ray starts within them.
     * Apart from that collisions with portal blocks will be considered even if
     * collisions with passable blocks are otherwise ignored.
     *
     * @param ignorePassableBlocks if the raytrace should ignore passable blocks
     * @return a reference to this object
     */
    @Contract(value = "_ -> this", mutates = "this")
    PositionedRayTraceConfigurationBuilder ignorePassableBlocks(boolean ignorePassableBlocks);

    /**
     * Sets the size of the raytrace when looking for entity collisions.
     *
     * @param raySize the new raytrace size
     * @return a reference to this object
     */
    @Contract(value = "_ -> this", mutates = "this")
    PositionedRayTraceConfigurationBuilder raySize(@NonNegative double raySize);

    /**
     * Sets the current entity filter when looking for entity collisions.
     *
     * @param entityFilter predicate for entities the ray can potentially collide with
     * @return a reference to this object
     */
    @Contract(value = "_ -> this", mutates = "this")
    PositionedRayTraceConfigurationBuilder entityFilter(Predicate<? super Entity> entityFilter);

    /**
     * Sets the current block filter when looking for block collisions.
     *
     * @param blockFilter predicate for blocks the ray can potentially collide with
     * @return a reference to this object
     */
    @Contract(value = "_ -> this", mutates = "this")
    PositionedRayTraceConfigurationBuilder blockFilter(Predicate<? super Block> blockFilter);

    /**
     * Sets the targets for the rayTrace.
     *
     * @param first the first target
     * @param others the other targets
     * @return a reference to this object
     */
    @Contract(value = "_, _ -> this", mutates = "this")
    PositionedRayTraceConfigurationBuilder targets(RayTraceTarget first, RayTraceTarget... others);
}
