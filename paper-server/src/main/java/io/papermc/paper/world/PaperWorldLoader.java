package io.papermc.paper.world;

import com.google.common.io.Files;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtException;
import net.minecraft.nbt.ReportedNbtException;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.Main;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.validation.ContentValidationException;
import org.apache.commons.io.FileUtils;
import org.bukkit.World;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public record PaperWorldLoader(MinecraftServer server, String levelId) {
    private static final Logger LOGGER = LogUtils.getClassLogger();

    public static PaperWorldLoader create(final MinecraftServer server, final String levelId) {
        return new PaperWorldLoader(server, levelId);
    }

    public record WorldLoadingInfo(
        int dimension,
        String name,
        String worldType,
        ResourceKey<LevelStem> stemKey,
        boolean enabled
    ) {}

    private WorldLoadingInfo getWorldInfo(
        final String levelId,
        final LevelStem stem
    ) {
        final ResourceKey<LevelStem> stemKey = this.server.registryAccess().lookupOrThrow(Registries.LEVEL_STEM).getResourceKey(stem).orElseThrow();
        int dimension = 0;
        boolean enabled = true;
        if (stemKey == LevelStem.NETHER) {
            dimension = -1;
            enabled = this.server.server.getAllowNether();
        } else if (stemKey == LevelStem.END) {
            dimension = 1;
            enabled = this.server.server.getAllowEnd();
        } else if (stemKey != LevelStem.OVERWORLD) {
            dimension = -999;
        }
        String worldType = dimension == -999
            ? stemKey.location().getNamespace() + "_" + stemKey.location().getPath()
            : World.Environment.getEnvironment(dimension).toString().toLowerCase(Locale.ROOT);
        String name = stemKey == LevelStem.OVERWORLD
            ? levelId
            : levelId + "_" + worldType;
        return new WorldLoadingInfo(dimension, name, worldType, stemKey, enabled);
    }

    private void migrateWorldFolder(final WorldLoadingInfo info) {
        // Migration of old CB world folders...
        if (info.dimension() == 0) {
            return;
        }

        File newWorld = LevelStorageSource.getStorageFolder(new File(info.name()).toPath(), info.stemKey()).toFile();
        File oldWorld = LevelStorageSource.getStorageFolder(new File(this.levelId).toPath(), info.stemKey()).toFile();
        File oldLevelDat = new File(new File(this.levelId), "level.dat"); // The data folders exist on first run as they are created in the PersistentCollection constructor above, but the level.dat won't

        if (!newWorld.isDirectory() && oldWorld.isDirectory() && oldLevelDat.isFile()) {
            LOGGER.info("---- Migration of old " + info.worldType() + " folder required ----");
            LOGGER.info("Unfortunately due to the way that Minecraft implemented multiworld support in 1.6, Bukkit requires that you move your " + info.worldType() + " folder to a new location in order to operate correctly.");
            LOGGER.info("We will move this folder for you, but it will mean that you need to move it back should you wish to stop using Bukkit in the future.");
            LOGGER.info("Attempting to move " + oldWorld + " to " + newWorld + "...");

            if (newWorld.exists()) {
                LOGGER.warn("A file or folder already exists at " + newWorld + "!");
                LOGGER.info("---- Migration of old " + info.worldType() + " folder failed ----");
            } else if (newWorld.getParentFile().mkdirs()) {
                if (oldWorld.renameTo(newWorld)) {
                    LOGGER.info("Success! To restore " + info.worldType() + " in the future, simply move " + newWorld + " to " + oldWorld);
                    // Migrate world data too.
                    try {
                        Files.copy(oldLevelDat, new File(new File(info.name()), "level.dat"));
                        FileUtils.copyDirectory(new File(new File(this.levelId), "data"), new File(new File(info.name()), "data"));
                    } catch (IOException exception) {
                        LOGGER.warn("Unable to migrate world data.");
                    }
                    LOGGER.info("---- Migration of old " + info.worldType() + " folder complete ----");
                } else {
                    LOGGER.warn("Could not move folder " + oldWorld + " to " + newWorld + "!");
                    LOGGER.info("---- Migration of old " + info.worldType() + " folder failed ----");
                }
            } else {
                LOGGER.warn("Could not create path for " + newWorld + "!");
                LOGGER.info("---- Migration of old " + info.worldType() + " folder failed ----");
            }
        }
    }

    // Loosely modeled on code in net.minecraft.server.Main
    public void loadInitialWorlds() {
        for (final LevelStem stem : this.server.registryAccess().lookupOrThrow(Registries.LEVEL_STEM)) {
            final WorldLoadingInfo info = this.getWorldInfo(this.levelId, stem);
            this.migrateWorldFolder(info);
            if (!info.enabled()) {
                continue;
            }

            LevelStorageSource.LevelStorageAccess levelStorageAccess = this.server.storageSource;
            if (info.dimension() != 0) {
                try {
                    levelStorageAccess = LevelStorageSource.createDefault(this.server.server.getWorldContainer().toPath()).validateAndCreateAccess(info.name(), info.stemKey());
                } catch (IOException | ContentValidationException ex) {
                    throw new RuntimeException(ex);
                }
            }

            final LevelDataResult levelData = getLevelData(levelStorageAccess);
            if (levelData.fatalError) {
                return;
            }

            final PrimaryLevelData primaryLevelData;
            if (levelData.dataTag == null) {
                primaryLevelData = (PrimaryLevelData) Main.createNewWorldData(
                    ((DedicatedServer) this.server).settings,
                    this.server.worldLoaderContext,
                    this.server.worldLoaderContext.datapackDimensions().lookupOrThrow(Registries.LEVEL_STEM),
                    this.server.isDemo(),
                    this.server.options.has("bonusChest")
                ).cookie();
            } else {
                primaryLevelData = (PrimaryLevelData) LevelStorageSource.getLevelDataAndDimensions(
                    levelData.dataTag,
                    this.server.worldLoaderContext.dataConfiguration(),
                    this.server.worldLoaderContext.datapackDimensions().lookupOrThrow(Registries.LEVEL_STEM),
                    this.server.worldLoaderContext.datapackWorldgen()
                ).worldData();
            }

            primaryLevelData.checkName(info.name()); // CraftBukkit - Migration did not rewrite the level.dat; This forces 1.8 to take the last loaded world as respawn (in this case the end)
            primaryLevelData.setModdedInfo(this.server.getServerModName(), this.server.getModdedStatus().shouldReportAsModified());

            if (this.server.options.has("forceUpgrade")) {
                Main.forceUpgrade(
                    levelStorageAccess,
                    primaryLevelData,
                    DataFixers.getDataFixer(),
                    this.server.options.has("eraseCache"),
                    () -> true,
                    this.server.registryAccess(),
                    this.server.options.has("recreateRegionFiles")
                );
            }

            this.server.createLevel(stem, info, levelStorageAccess, primaryLevelData);
        }

        ((DedicatedServer) this.server).forceDifficulty();

        for (ServerLevel serverLevel : this.server.getAllLevels()) {
            this.server.prepareLevel(serverLevel);
        }
    }

    public record LevelDataResult(@Nullable Dynamic<?> dataTag, boolean fatalError) {}

    // Based on code in net.minecraft.server.Main
    public static LevelDataResult getLevelData(
        final LevelStorageSource.LevelStorageAccess levelStorageAccess
    ) {
        Dynamic<?> dataTag;
        if (levelStorageAccess.hasWorldData()) {
            LevelSummary summary;
            try {
                dataTag = levelStorageAccess.getDataTag();
                summary = levelStorageAccess.getSummary(dataTag);
            } catch (NbtException | ReportedNbtException | IOException var41) {
                LevelStorageSource.LevelDirectory levelDirectory = levelStorageAccess.getLevelDirectory();
                LOGGER.warn("Failed to load world data from {}", levelDirectory.dataFile(), var41);
                LOGGER.info("Attempting to use fallback");

                try {
                    dataTag = levelStorageAccess.getDataTagFallback();
                    summary = levelStorageAccess.getSummary(dataTag);
                } catch (NbtException | ReportedNbtException | IOException var40) {
                    LOGGER.error("Failed to load world data from {}", levelDirectory.oldDataFile(), var40);
                    LOGGER.error(
                        "Failed to load world data from {} and {}. World files may be corrupted. Shutting down.",
                        levelDirectory.dataFile(),
                        levelDirectory.oldDataFile()
                    );
                    return new LevelDataResult(null, true);
                }

                levelStorageAccess.restoreLevelDataFromOld();
            }

            if (summary.requiresManualConversion()) {
                LOGGER.info("This world must be opened in an older version (like 1.6.4) to be safely converted");
                return new LevelDataResult(null, true);
            }

            if (!summary.isCompatible()) {
                LOGGER.info("This world was created by an incompatible version.");
                return new LevelDataResult(null, true);
            }
        } else {
            return new LevelDataResult(null, false);
        }

        return new LevelDataResult(dataTag, false);
    }
}
