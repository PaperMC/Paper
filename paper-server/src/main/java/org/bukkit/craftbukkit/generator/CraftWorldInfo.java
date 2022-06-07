package org.bukkit.craftbukkit.generator;

import java.util.UUID;
import net.minecraft.world.level.dimension.DimensionManager;
import net.minecraft.world.level.storage.Convertable;
import net.minecraft.world.level.storage.IWorldDataServer;
import net.minecraft.world.level.storage.WorldDataServer;
import org.bukkit.World;
import org.bukkit.craftbukkit.util.WorldUUID;
import org.bukkit.generator.WorldInfo;

public class CraftWorldInfo implements WorldInfo {

    private final String name;
    private final UUID uuid;
    private final World.Environment environment;
    private final long seed;
    private final int minHeight;
    private final int maxHeight;

    public CraftWorldInfo(IWorldDataServer worldDataServer, Convertable.ConversionSession session, World.Environment environment, DimensionManager dimensionManager) {
        this.name = worldDataServer.getLevelName();
        this.uuid = WorldUUID.getUUID(session.levelDirectory.path().toFile());
        this.environment = environment;
        this.seed = ((WorldDataServer) worldDataServer).worldGenSettings().seed();
        this.minHeight = dimensionManager.minY();
        this.maxHeight = dimensionManager.minY() + dimensionManager.height();
    }

    public CraftWorldInfo(String name, UUID uuid, World.Environment environment, long seed, int minHeight, int maxHeight) {
        this.name = name;
        this.uuid = uuid;
        this.environment = environment;
        this.seed = seed;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUID() {
        return uuid;
    }

    @Override
    public World.Environment getEnvironment() {
        return environment;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public int getMinHeight() {
        return minHeight;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }
}
