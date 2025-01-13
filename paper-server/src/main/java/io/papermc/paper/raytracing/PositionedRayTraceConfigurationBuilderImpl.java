package io.papermc.paper.raytracing;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PositionedRayTraceConfigurationBuilderImpl implements PositionedRayTraceConfigurationBuilder {

    private Location start;
    private Vector direction;
    private double maxDistance;
    private org.bukkit.FluidCollisionMode fluidCollisionMode = org.bukkit.FluidCollisionMode.NEVER;
    private boolean ignorePassableBlocks;
    private double raySize = 0.0D;
    private java.util.function.Predicate<? super org.bukkit.entity.Entity> entityFilter;
    private java.util.function.Predicate<? super org.bukkit.block.Block> blockFilter;
    private List<RayTraceTarget> targets;

    @Override
    public Location start() {
        return this.start;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder start(final Location start) {
        this.start = start;
        return this;
    }

    @Override
    public Vector direction() {
        return this.direction;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder direction(final Vector direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public double maxDistance() {
        return maxDistance;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder maxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
        return this;
    }

    @Override
    public FluidCollisionMode fluidCollisionMode() {
        return fluidCollisionMode;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder fluidCollisionMode(FluidCollisionMode fluidCollisionMode) {
        this.fluidCollisionMode = fluidCollisionMode;
        return this;
    }

    @Override
    public boolean ignorePassableBlocks() {
        return ignorePassableBlocks;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder ignorePassableBlocks(boolean ignorePassableBlocks) {
        this.ignorePassableBlocks = ignorePassableBlocks;
        return this;
    }

    @Override
    public double raySize() {
        return raySize;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder raySize(double raySize) {
        this.raySize = raySize;
        return this;
    }

    @Override
    public Predicate<? super org.bukkit.entity.Entity> entityFilter() {
        return entityFilter;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder entityFilter(Predicate<? super org.bukkit.entity.Entity> entityFilter) {
        this.entityFilter = entityFilter;
        return this;
    }

    @Override
    public Predicate<? super org.bukkit.block.Block> blockFilter() {
        return blockFilter;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder blockFilter(Predicate<? super org.bukkit.block.Block> blockFilter) {
        this.blockFilter = blockFilter;
        return this;
    }

    @Override
    public List<RayTraceTarget> targets() {
        return this.targets;
    }

    @Override
    public PositionedRayTraceConfigurationBuilder targets(final RayTraceTarget first, final RayTraceTarget ... others) {
        List<RayTraceTarget> targets = new ArrayList<>(List.of(others));
        targets.add(first);
        this.targets = targets;
        return this;
    }
}
