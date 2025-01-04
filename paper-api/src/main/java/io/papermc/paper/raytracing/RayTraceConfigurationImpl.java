package io.papermc.paper.raytracing;

import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.function.Predicate;

public record RayTraceConfigurationImpl(double maxDistance, @Nullable FluidCollisionMode fluidCollisionMode,
                                        boolean ignorePassableBlocks, double raySize,
                                        @Nullable Predicate<? super Entity> entityFilter, @Nullable Predicate<? super Block> blockFilter,
                                        @NotNull List<Targets> targets) implements RayTraceConfiguration {
}
