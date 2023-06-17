package io.papermc.paper.threadedregions.scheduler;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class FallbackRegionScheduler implements RegionScheduler {

    @Override
    public void execute(@NotNull final Plugin plugin, @NotNull final World world, final int chunkX, final int chunkZ, @NotNull final Runnable run) {
        plugin.getServer().getGlobalRegionScheduler().execute(plugin, run);
    }

    @Override
    public @NotNull ScheduledTask run(@NotNull final Plugin plugin, @NotNull final World world, final int chunkX, final int chunkZ, @NotNull final Consumer<ScheduledTask> task) {
        return plugin.getServer().getGlobalRegionScheduler().run(plugin, task);
    }

    @Override
    public @NotNull ScheduledTask runDelayed(@NotNull final Plugin plugin, @NotNull final World world, final int chunkX, final int chunkZ, @NotNull final Consumer<ScheduledTask> task, final long delayTicks) {
        return plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, task, delayTicks);
    }

    @Override
    public @NotNull ScheduledTask runAtFixedRate(@NotNull final Plugin plugin, @NotNull final World world, final int chunkX, final int chunkZ, @NotNull final Consumer<ScheduledTask> task, final long initialDelayTicks, final long periodTicks) {
        return plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, task, initialDelayTicks, periodTicks);
    }
}
