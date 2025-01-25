package io.papermc.paper.raytracing;

import com.google.common.base.Preconditions;
import java.util.EnumSet;
import java.util.OptionalDouble;
import java.util.function.Predicate;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class PositionedRayTraceConfigurationBuilderImpl implements PositionedRayTraceConfigurationBuilder {

    public @Nullable Location start;
    public @Nullable Vector direction;
    public OptionalDouble maxDistance = OptionalDouble.empty();
    public FluidCollisionMode fluidCollisionMode = FluidCollisionMode.NEVER;
    public boolean ignorePassableBlocks;
    public double raySize = 0.0D;
    public @Nullable Predicate<? super Entity> entityFilter;
    public @Nullable Predicate<? super Block> blockFilter;
    public EnumSet<RayTraceTarget> targets = EnumSet.noneOf(RayTraceTarget.class);

    @Override
    public PositionedRayTraceConfigurationBuilder start(final Location start) {
        Preconditions.checkArgument(start != null, "start must not be null");
        this.start = start.clone();
        return this;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder direction(final Vector direction) {
        Preconditions.checkArgument(direction != null, "direction must not be null");
        this.direction = direction.clone();
        return this;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder maxDistance(final double maxDistance) {
        Preconditions.checkArgument(maxDistance >= 0, "maxDistance must be non-negative");
        this.maxDistance = OptionalDouble.of(maxDistance);
        return this;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder fluidCollisionMode(final FluidCollisionMode fluidCollisionMode) {
        Preconditions.checkArgument(fluidCollisionMode != null, "fluidCollisionMode must not be null");
        this.fluidCollisionMode = fluidCollisionMode;
        return this;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder ignorePassableBlocks(final boolean ignorePassableBlocks) {
        this.ignorePassableBlocks = ignorePassableBlocks;
        return this;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder raySize(final double raySize) {
        Preconditions.checkArgument(raySize >= 0, "raySize must be non-negative");
        this.raySize = raySize;
        return this;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder entityFilter(final Predicate<? super Entity> entityFilter) {
        Preconditions.checkArgument(entityFilter != null, "entityFilter must not be null");
        this.entityFilter = entityFilter;
        return this;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder blockFilter(final Predicate<? super Block> blockFilter) {
        Preconditions.checkArgument(blockFilter != null, "blockFilter must not be null");
        this.blockFilter = blockFilter;
        return this;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder targets(final RayTraceTarget first, final RayTraceTarget... others) {
        Preconditions.checkArgument(first != null, "first must not be null");
        Preconditions.checkArgument(others != null, "others must not be null");
        this.targets = EnumSet.of(first, others);
        return this;
    }
}
