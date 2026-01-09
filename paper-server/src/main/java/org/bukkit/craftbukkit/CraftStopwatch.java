package org.bukkit.craftbukkit;


import net.minecraft.resources.Identifier;
import net.minecraft.world.Stopwatches;
import org.bukkit.NamespacedKey;
import org.bukkit.Stopwatch;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.jetbrains.annotations.NotNull;

public class CraftStopwatch implements Stopwatch {

    private net.minecraft.world.Stopwatch handle;
    private final net.minecraft.server.MinecraftServer server;
    private final NamespacedKey key;

    public CraftStopwatch(net.minecraft.world.Stopwatch handle, net.minecraft.server.MinecraftServer server, NamespacedKey key) {
        this.handle = handle;
        this.server = server;
        this.key = key;
    }

    @Override
    public long creationTime() {
        return handle.creationTime();
    }

    @Override
    public long accumulatedElapsedTime() {
        return handle.accumulatedElapsedTime();
    }

    @Override
    public long elapsedMilliseconds(final long time) {
        return handle.elapsedMilliseconds(time);
    }

    @Override
    public double elapsedSeconds(final long time) {
        return handle.elapsedSeconds(time);
    }

    @Override
    public void restart() {
        final Stopwatches stopwatches = server.getStopwatches();
        final Identifier id = CraftNamespacedKey.toMinecraft(key);
        if (!stopwatches.update(id,
            stopwatch -> new net.minecraft.world.Stopwatch(Stopwatches.currentTime()))) {
            throw new UnsupportedOperationException("The stopwatch could not be restarted, because it was removed!");
        }
        this.handle = stopwatches.get(id);
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }
}
