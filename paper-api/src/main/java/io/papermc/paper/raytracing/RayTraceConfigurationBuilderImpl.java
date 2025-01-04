package io.papermc.paper.raytracing;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RayTraceConfigurationBuilderImpl implements RayTraceConfiguration.Builder {

    private double maxDistance;
    private FluidCollisionMode fluidCollisionMode = FluidCollisionMode.NEVER;
    private boolean ignorePassableBlocks;
    private double raySize = 0.0D;
    private Predicate<? super Entity> entityFilter;
    private Predicate<? super Block> blockFilter;


    @Override
    public double maxDistance() {
        return maxDistance;
    }

    @Override
    public @NotNull RayTraceConfiguration.Builder maxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
        return this;
    }

    @Override
    public @Nullable FluidCollisionMode fluidCollisionMode() {
        return fluidCollisionMode;
    }

    @Override
    public @NotNull RayTraceConfiguration.Builder fluidCollisionMode(@Nullable FluidCollisionMode fluidCollisionMode) {
        this.fluidCollisionMode = fluidCollisionMode;
        return this;
    }

    @Override
    public boolean ignorePassableBlocks() {
        return ignorePassableBlocks;
    }

    @Override
    public @NotNull RayTraceConfiguration.Builder ignorePassableBlocks(boolean ignorePassableBlocks) {
        this.ignorePassableBlocks = ignorePassableBlocks;
        return this;
    }

    @Override
    public double raySize() {
        return raySize;
    }

    @Override
    public @NotNull RayTraceConfiguration.Builder raySize(double raySize) {
        this.raySize = raySize;
        return this;
    }

    @Override
    public @Nullable Predicate<? super Entity> entityFilter() {
        return entityFilter;
    }

    @Override
    public @NotNull RayTraceConfiguration.Builder entityFilter(@Nullable Predicate<? super Entity> entityFilter) {
        this.entityFilter = entityFilter;
        return this;
    }

    @Override
    public @Nullable Predicate<? super Block> blockFilter() {
        return blockFilter;
    }

    @Override
    public @NotNull RayTraceConfiguration.Builder blockFilter(@Nullable Predicate<? super Block> blockFilter) {
        this.blockFilter = blockFilter;
        return this;
    }

    @Override
    public @NotNull RayTraceConfiguration target(@NotNull RayTraceConfiguration.Targets first, @NotNull RayTraceConfiguration.Targets... others) {
        List<RayTraceConfiguration.Targets> targets = new ArrayList<>(List.of(others)); // Need to make this immutable later
        targets.add(first);
        return new RayTraceConfigurationImpl(maxDistance, fluidCollisionMode, ignorePassableBlocks, raySize, entityFilter, blockFilter, targets);
    }
}
