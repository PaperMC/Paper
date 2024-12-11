package org.bukkit.craftbukkit.generator;

import java.util.UUID;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
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

    public CraftWorldInfo(ServerLevelData worldDataServer, LevelStorageSource.LevelStorageAccess session, World.Environment environment, DimensionType dimensionManager) {
        this.name = worldDataServer.getLevelName();
        this.uuid = WorldUUID.getUUID(session.levelDirectory.path().toFile());
        this.environment = environment;
        this.seed = ((PrimaryLevelData) worldDataServer).worldGenOptions().seed();
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
        return this.name;
    }

    @Override
    public UUID getUID() {
        return this.uuid;
    }

    @Override
    public World.Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public long getSeed() {
        return this.seed;
    }

    @Override
    public int getMinHeight() {
        return this.minHeight;
    }

    @Override
    public int getMaxHeight() {
        return this.maxHeight;
    }
}
