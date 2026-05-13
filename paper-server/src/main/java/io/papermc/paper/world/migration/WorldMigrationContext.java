package io.papermc.paper.world.migration;

import java.nio.file.Path;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.jspecify.annotations.NullMarked;

@NullMarked
record WorldMigrationContext(
    LevelStorageSource.LevelStorageAccess rootAccess,
    HolderLookup.Provider registryAccess,
    String worldName,
    ResourceKey<LevelStem> stemKey,
    ResourceKey<Level> dimensionKey
) {
    Path baseRoot() {
        return this.rootAccess.levelDirectory.path();
    }

    Path targetDimensionPath() {
        return this.rootAccess.getDimensionPath(this.dimensionKey);
    }

    Path targetDataRoot() {
        return this.targetDimensionPath().resolve(LevelResource.DATA.id());
    }
}
