package io.papermc.paper.world.migration;

import com.mojang.logging.LogUtils;
import io.papermc.paper.world.saveddata.PaperLevelOverrides;
import io.papermc.paper.world.saveddata.PaperWorldMetadata;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;

@NullMarked
public final class WorldFolderMigration {
    private static final Logger LOGGER = LogUtils.getClassLogger();
    private static final boolean DISABLE_MIGRATION_DELAY = Boolean.getBoolean("Paper.DisableMigrationDelay");
    private static boolean startupMigrationWarningShown;

    private WorldFolderMigration() {
    }

    private enum MigrationMode {
        LEGACY_CRAFTBUKKIT_MIGRATION,
        VANILLA_MIGRATION,
        NO_OP
    }

    public static void migrateStartupWorld(
        final LevelStorageSource.LevelStorageAccess rootAccess,
        final HolderLookup.Provider registryAccess,
        final String worldName,
        final ResourceKey<LevelStem> stemKey,
        final ResourceKey<Level> dimensionKey
    ) throws IOException {
        final WorldMigrationContext context = new WorldMigrationContext(rootAccess, registryAccess, worldName, stemKey, dimensionKey);
        final MigrationMode mode = classifyStartupMigration(context);
        if (mode != MigrationMode.NO_OP) {
            warnAndDelayStartupMigration();
        }
        switch (mode) {
            case LEGACY_CRAFTBUKKIT_MIGRATION -> LegacyCraftBukkitWorldMigration.migrate(context);
            case VANILLA_MIGRATION -> VanillaWorldMigration.migrate(context);
            case NO_OP -> {}
        }
    }

    public static void migrateApiWorld(
        final LevelStorageSource.LevelStorageAccess rootAccess,
        final HolderLookup.Provider registryAccess,
        final String worldName,
        final ResourceKey<LevelStem> stemKey,
        final ResourceKey<Level> dimensionKey
    ) throws IOException {
        LegacyCraftBukkitWorldMigration.migrateApiWorld(new WorldMigrationContext(rootAccess, registryAccess, worldName, stemKey, dimensionKey));
    }

    private static synchronized void warnAndDelayStartupMigration() throws IOException {
        if (startupMigrationWarningShown) {
            return;
        }
        startupMigrationWarningShown = true;
        LOGGER.warn("===================== ! ALERT ! =====================");
        LOGGER.warn("World storage migration is required during startup.");
        LOGGER.warn("If you do not have a backup: interrupt the server now. Use Ctrl+C, your panel kill function, etc.");
        LOGGER.warn("=====================================================");
        LOGGER.warn("Continuing in 30 seconds...");
        if (DISABLE_MIGRATION_DELAY) {
            LOGGER.warn("Migration delay disabled by system property.");
        } else {
            try {
                Thread.sleep(30_000L);
            } catch (final InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new IOException("Interrupted while waiting before startup world migration", ex);
            }
        }
        LOGGER.info("Continuing with startup world migration.");
    }

    private static MigrationMode classifyStartupMigration(final WorldMigrationContext context) {
        if (!context.rootAccess().hasWorldData()) {
            return MigrationMode.NO_OP;
        }
        if (!context.rootAccess().getLevelId().equals(context.worldName()) && Files.isDirectory(context.rootAccess().parent().getLevelPath(context.worldName()))) {
            return MigrationMode.LEGACY_CRAFTBUKKIT_MIGRATION;
        }
        return hasCurrentPaperData(context.rootAccess(), context.dimensionKey()) ? MigrationMode.NO_OP : MigrationMode.VANILLA_MIGRATION;
    }

    static boolean hasCurrentPaperData(final LevelStorageSource.LevelStorageAccess rootAccess, final ResourceKey<Level> dimensionKey) {
        final Path targetDataRoot = rootAccess.getDimensionPath(dimensionKey).resolve(LevelResource.DATA.id());
        return Files.isRegularFile(WorldMigrationSupport.savedDataPath(targetDataRoot, PaperWorldMetadata.TYPE))
            && Files.isRegularFile(WorldMigrationSupport.savedDataPath(targetDataRoot, PaperLevelOverrides.TYPE))
            && Files.isRegularFile(WorldMigrationSupport.savedDataPath(targetDataRoot, WorldGenSettings.TYPE));
    }
}
