package io.papermc.paper.raytracing;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PositionedRayTraceConfigurationBuilderImpl implements PositionedRayTraceConfigurationBuilder {

    private World world;
    private Location start;
    private Vector direction;
    private double maxDistance;
    private org.bukkit.FluidCollisionMode fluidCollisionMode = org.bukkit.FluidCollisionMode.NEVER;
    private boolean ignorePassableBlocks;
    private double raySize = 0.0D;
    private java.util.function.Predicate<? super org.bukkit.entity.Entity> entityFilter;
    private java.util.function.Predicate<? super org.bukkit.block.Block> blockFilter;
    private List<RayTraceTargets> targets;

    public PositionedRayTraceConfigurationBuilderImpl(World world) {
        this.world = world;
    }


    @Override
    public @NotNull Location start() {
        return this.start;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder start(final @NotNull Location start) {
        this.start = start;
        return this;
    }

    @Override
    public @NotNull Vector direction() {
        return this.direction;
    }

    @Override
    public @NotNull PositionedRayTraceConfigurationBuilder direction(final @NotNull Vector direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public double maxDistance() {
        return maxDistance;
    }

    @Override
    public @NotNull PositionedRayTraceConfigurationBuilder maxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
        return this;
    }

    @Override
    public @NotNull FluidCollisionMode fluidCollisionMode() {
        return fluidCollisionMode;
    }

    @Override
    public @NotNull PositionedRayTraceConfigurationBuilder fluidCollisionMode(@NotNull FluidCollisionMode fluidCollisionMode) {
        this.fluidCollisionMode = fluidCollisionMode;
        return this;
    }

    @Override
    public boolean ignorePassableBlocks() {
        return ignorePassableBlocks;
    }

    @Override
    public @NotNull PositionedRayTraceConfigurationBuilder ignorePassableBlocks(boolean ignorePassableBlocks) {
        this.ignorePassableBlocks = ignorePassableBlocks;
        return this;
    }

    @Override
    public double raySize() {
        return raySize;
    }

    @Override
    public @NotNull PositionedRayTraceConfigurationBuilder raySize(double raySize) {
        this.raySize = raySize;
        return this;
    }

    @Override
    public @Nullable Predicate<? super org.bukkit.entity.Entity> entityFilter() {
        return entityFilter;
    }

    @Override
    public @NotNull PositionedRayTraceConfigurationBuilder entityFilter(@Nullable Predicate<? super org.bukkit.entity.Entity> entityFilter) {
        this.entityFilter = entityFilter;
        return this;
    }

    @Override
    public @Nullable Predicate<? super org.bukkit.block.Block> blockFilter() {
        return blockFilter;
    }

    @Override
    public @NotNull PositionedRayTraceConfigurationBuilder blockFilter(@Nullable Predicate<? super org.bukkit.block.Block> blockFilter) {
        this.blockFilter = blockFilter;
        return this;
    }

    @Override
    public @Nullable List<RayTraceTargets> targets() {
        return this.targets;
    }

    @Override
    public @NotNull PositionedRayTraceConfigurationBuilder targets(final @NotNull RayTraceTargets first, final @NotNull RayTraceTargets... others) {
        List<RayTraceTargets> targets = new ArrayList<>(List.of(others));
        targets.add(first);
        this.targets = targets;
        return this;
    }

    public RayTraceResult cast() {
        if (targets.contains(RayTraceTargets.ENTITIES)) {
            if(targets.contains(RayTraceTargets.BLOCKS))
                return world.rayTrace(this.start(), this.direction(), this.maxDistance(), this.fluidCollisionMode(), this.ignorePassableBlocks(), this.raySize(), this.entityFilter(), this.blockFilter());
            return world.rayTraceEntities(this.start(), this.direction(), this.maxDistance(), this.raySize(), this.entityFilter());
        }
        return world.rayTraceBlocks(this.start(), this.direction(), this.maxDistance(), this.fluidCollisionMode(), this.ignorePassableBlocks(), this.blockFilter());
    }
}
