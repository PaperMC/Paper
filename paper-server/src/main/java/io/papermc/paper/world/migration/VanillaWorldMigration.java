package io.papermc.paper.world.migration;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import io.papermc.paper.world.saveddata.PaperLevelOverrides;
import io.papermc.paper.world.saveddata.PaperWorldMetadata;
import io.papermc.paper.world.saveddata.PaperWorldPDC;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRuleMap;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.saveddata.WanderingTraderData;
import net.minecraft.world.level.saveddata.WeatherData;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.SavedDataStorage;
import net.minecraft.world.level.timers.TimerQueue;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@NullMarked
final class VanillaWorldMigration {
    private static final Logger LOGGER = LogUtils.getClassLogger();

    private VanillaWorldMigration() {
    }

    static void migrate(final WorldMigrationContext context) throws IOException {
        LOGGER.info("Starting Vanilla import for world '{}' ({})", context.worldName(), context.dimensionKey().identifier());
        final var levelDataResult = WorldMigrationSupport.readFixedLevelData(context.rootAccess());
        if (levelDataResult.fatalError()) {
            throw new IOException("Failed to read level data for world migration");
        }

        final boolean rootOwnsThisWorld = context.dimensionKey() == Level.OVERWORLD;

        if (rootOwnsThisWorld) {
            WorldMigrationSupport.migratePaperWorldConfig(context.baseRoot(), context.targetDimensionPath());
            migrateLegacyWorldMetadata(context);
        }

        migrateSavedData(context);
        createLevelOverrides(context, levelDataResult.dataTag());
        migrateLegacyPdc(context, levelDataResult.dataTag());

        LOGGER.info("Completed Vanilla import for world '{}' ({})", context.worldName(), context.dimensionKey().identifier());
    }

    private static void migrateSavedData(final WorldMigrationContext context) throws IOException {
        final List<Path> sourceDataRoots = List.of(
            context.rootAccess().getDimensionPath(Level.OVERWORLD).resolve(LevelResource.DATA.id()),
            context.baseRoot().resolve(LevelResource.DATA.id())
        );

        WorldMigrationSupport.copySavedDataIfPresent(sourceDataRoots, context.targetDataRoot(), WorldGenSettings.TYPE, false);
        WorldMigrationSupport.copySavedDataIfPresent(sourceDataRoots, context.targetDataRoot(), GameRuleMap.TYPE, false);
        WorldMigrationSupport.copySavedDataIfPresent(sourceDataRoots, context.targetDataRoot(), WeatherData.TYPE, false);
        WorldMigrationSupport.copySavedDataIfPresent(sourceDataRoots, context.targetDataRoot(), ServerClockManager.TYPE, false);
        if (context.dimensionKey() == Level.OVERWORLD) {
            WorldMigrationSupport.copySavedDataIfPresent(sourceDataRoots, context.targetDataRoot(), TimerQueue.TYPE, false);
            WorldMigrationSupport.copySavedDataIfPresent(sourceDataRoots, context.targetDataRoot(), WanderingTraderData.TYPE, false);

            deleteLegacyRootCopyIfMigrated(context, WorldGenSettings.TYPE);
            deleteLegacyRootCopyIfMigrated(context, GameRuleMap.TYPE);
            deleteLegacyRootCopyIfMigrated(context, WeatherData.TYPE);
            deleteLegacyRootCopyIfMigrated(context, ServerClockManager.TYPE);
            deleteLegacyRootCopyIfMigrated(context, TimerQueue.TYPE);
            deleteLegacyRootCopyIfMigrated(context, WanderingTraderData.TYPE);
        }
    }

    private static void deleteLegacyRootCopyIfMigrated(final WorldMigrationContext context, final SavedDataType<?> type) throws IOException {
        final Path target = WorldMigrationSupport.savedDataPath(context.targetDataRoot(), type);
        if (!Files.isRegularFile(target)) {
            return;
        }

        final Path rootSource = WorldMigrationSupport.savedDataPath(context.baseRoot().resolve(LevelResource.DATA.id()), type);
        Files.deleteIfExists(rootSource);
    }

    private static void createLevelOverrides(
        final WorldMigrationContext context,
        final @Nullable Dynamic<?> levelData
    ) {
        final Path levelOverridesPath = WorldMigrationSupport.savedDataPath(context.targetDataRoot(), PaperLevelOverrides.TYPE);
        if (Files.exists(levelOverridesPath)) {
            return;
        }

        final PaperLevelOverrides levelOverrides = PaperLevelOverrides.createFromRawLevelData(levelData);
        final ResourceKey<Level> explicitPaperRespawnDimension = resolveExplicitPaperRespawnDimension(levelData);
        if (explicitPaperRespawnDimension == null && context.dimensionKey() != resolveVanillaRespawnDimension(levelData)) {
            levelOverrides.setInitialized(false);
        }

        final SavedDataStorage targetStorage = new SavedDataStorage(context.targetDataRoot(), DataFixers.getDataFixer(), context.registryAccess());
        targetStorage.set(PaperLevelOverrides.TYPE, levelOverrides);
        targetStorage.saveAndJoin();
    }

    private static void migrateLegacyPdc(
        final WorldMigrationContext context,
        final @Nullable Dynamic<?> levelData
    ) {
        final Path pdcPath = WorldMigrationSupport.savedDataPath(context.targetDataRoot(), PaperWorldPDC.TYPE);
        if (Files.exists(pdcPath)) {
            return;
        }

        final PaperWorldPDC pdc = WorldMigrationSupport.readLegacyPdc(levelData, context.registryAccess());
        if (pdc != null) {
            final SavedDataStorage targetStorage = new SavedDataStorage(context.targetDataRoot(), DataFixers.getDataFixer(), context.registryAccess());
            targetStorage.set(PaperWorldPDC.TYPE, pdc);
            targetStorage.saveAndJoin();

            WorldMigrationSupport.clearLegacyPdc(levelData);
            context.rootAccess().saveLevelData(levelData);
        }
    }

    private static void migrateLegacyWorldMetadata(final WorldMigrationContext context) throws IOException {
        final Path metadataPath = WorldMigrationSupport.savedDataPath(context.targetDataRoot(), PaperWorldMetadata.TYPE);
        if (Files.exists(metadataPath)) {
            return;
        }

        final UUID legacyUuid = WorldMigrationSupport.readLegacyUuid(context.baseRoot());
        final SavedDataStorage targetStorage = new SavedDataStorage(context.targetDataRoot(), DataFixers.getDataFixer(), context.registryAccess());
        targetStorage.set(PaperWorldMetadata.TYPE, new PaperWorldMetadata(legacyUuid == null ? UUID.randomUUID() : legacyUuid));
        targetStorage.saveAndJoin();
        Files.deleteIfExists(context.baseRoot().resolve(WorldMigrationSupport.LEGACY_UID_FILE_NAME));
    }

    private static @Nullable ResourceKey<Level> resolveExplicitPaperRespawnDimension(final @Nullable Dynamic<?> levelData) {
        if (levelData == null) {
            return null;
        }

        return levelData.get(PrimaryLevelData.PAPER_RESPAWN_DIMENSION)
            .read(Level.RESOURCE_KEY_CODEC)
            .result()
            .orElse(null);
    }

    private static ResourceKey<Level> resolveVanillaRespawnDimension(final @Nullable Dynamic<?> levelData) {
        if (levelData == null) {
            return Level.OVERWORLD;
        }

        return levelData.get("spawn")
            .read(LevelData.RespawnData.CODEC)
            .result()
            .map(LevelData.RespawnData::dimension)
            .orElse(Level.OVERWORLD);
    }
}
