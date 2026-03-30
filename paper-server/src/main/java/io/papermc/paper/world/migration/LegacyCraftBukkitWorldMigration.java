package io.papermc.paper.world.migration;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import io.papermc.paper.world.saveddata.PaperLevelOverrides;
import io.papermc.paper.world.saveddata.PaperWorldMetadata;
import io.papermc.paper.world.saveddata.PaperWorldPDC;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.entity.raid.Raids;
import net.minecraft.world.level.TicketStorage;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import net.minecraft.world.level.gamerules.GameRuleMap;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.saveddata.WanderingTraderData;
import net.minecraft.world.level.saveddata.WeatherData;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.SavedDataStorage;
import net.minecraft.world.level.timers.TimerQueue;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import static java.util.Objects.requireNonNullElseGet;

@NullMarked
final class LegacyCraftBukkitWorldMigration {
    private static final Logger LOGGER = LogUtils.getClassLogger();
    private static final String[] WORLD_BORDER_RELATIVE_CANDIDATES = {"minecraft/world_border.dat", "world_border.dat"};

    private final WorldMigrationContext context;
    private final Path sourceRoot;
    private final Path targetDataRoot;
    private final Path targetDimensionPath;
    private final HolderLookup.Provider registryAccess;
    private @Nullable Path initialExplicitWorldBorder;
    private List<Path> sourceDataRoots = List.of();

    static void migrate(final WorldMigrationContext context) throws IOException {
        new LegacyCraftBukkitWorldMigration(context).run();
    }

    static void migrateApiWorld(final WorldMigrationContext context) throws IOException {
        final Path sourceRoot = context.rootAccess().parent().getLevelPath(context.worldName());
        if (!Files.isDirectory(sourceRoot)) {
            return;
        }
        migrate(context);
    }

    private LegacyCraftBukkitWorldMigration(final WorldMigrationContext context) {
        this.context = context;
        this.sourceRoot = context.rootAccess().parent().getLevelPath(context.worldName());
        this.targetDataRoot = context.targetDataRoot();
        this.targetDimensionPath = context.targetDimensionPath();
        this.registryAccess = context.registryAccess();
    }

    private void run() throws IOException {
        LOGGER.info("Starting legacy CraftBukkit import for world '{}' ({})", this.context.worldName(), this.context.dimensionKey().identifier());
        try (final var sourceAccess = this.context.rootAccess().parent().createAccess(this.context.worldName())) {
            Files.createDirectories(this.targetDataRoot);

            final List<Path> initialDimensionRoots = this.locateDimensionRoots();
            this.initialExplicitWorldBorder = this.findExplicitFile(this.explicitDataRoots(initialDimensionRoots), WORLD_BORDER_RELATIVE_CANDIDATES);
            for (final Path sourceDimensionRoot : initialDimensionRoots) {
                WorldMigrationSupport.migrateDimensionDirectories(sourceDimensionRoot, this.targetDimensionPath);
            }

            WorldMigrationSupport.migratePaperWorldConfig(this.sourceRoot, this.targetDimensionPath);

            final var levelDataResult = WorldMigrationSupport.readFixedLevelData(sourceAccess);
            if (levelDataResult.fatalError()) {
                throw new IOException("Failed to read level data for world migration");
            }

            this.sourceDataRoots = this.locateSavedDataRoots();

            final SavedDataStorage tempStorage = new SavedDataStorage(this.targetDataRoot, DataFixers.getDataFixer(), this.registryAccess);
            this.migrateSharedSavedData();
            this.migrateLegacyCraftBukkitPaperData(tempStorage, levelDataResult.dataTag());
            tempStorage.saveAndJoin();
        }
        deleteMigratedSeparateRoot(this.sourceRoot);
        LOGGER.info("Completed legacy CraftBukkit import for world '{}' ({})", this.context.worldName(), this.context.dimensionKey().identifier());
    }

    private void migrateSharedSavedData() throws IOException {
        this.copySavedDataIfPresent(GameRuleMap.TYPE);
        this.copySavedDataIfPresent(WeatherData.TYPE);
        this.copySavedDataIfPresent(TimerQueue.TYPE);
        this.copySavedDataIfPresent(WanderingTraderData.TYPE);
        this.copySavedDataIfPresent(TicketStorage.TYPE);
        this.copySavedDataIfPresent(Raids.TYPE);
        this.copySavedDataIfPresent(WorldGenSettings.TYPE);
        this.migrateLegacyWorldBorder();

        if (this.context.stemKey() == LevelStem.END) {
            this.copySavedDataIfPresent(EnderDragonFight.TYPE);
        } else {
            Files.deleteIfExists(WorldMigrationSupport.savedDataPath(this.targetDataRoot, EnderDragonFight.TYPE));
        }
    }

    static List<Path> rootOwnedDataRoots(final Path sourceRoot) {
        return List.of(
            WorldMigrationSupport.getStorageFolder(LevelStem.OVERWORLD.identifier(), sourceRoot).resolve(LevelResource.DATA.id()),
            sourceRoot.resolve(LevelResource.DATA.id())
        );
    }

    private void migrateLegacyWorldBorder() throws IOException {
        if (this.initialExplicitWorldBorder != null && Files.isRegularFile(this.initialExplicitWorldBorder)) {
            WorldMigrationSupport.copySavedDataFileIfPresent(this.targetDataRoot, this.initialExplicitWorldBorder, WorldBorder.TYPE, true);
            return;
        }

        final Path rootOwnedBorder = this.findExplicitFile(rootOwnedDataRoots(this.sourceRoot), WORLD_BORDER_RELATIVE_CANDIDATES);
        if (rootOwnedBorder != null) {
            WorldMigrationSupport.copySavedDataFileIfPresent(this.targetDataRoot, rootOwnedBorder, WorldBorder.TYPE, true);
            return;
        }

        this.copySavedDataIfPresent(WorldBorder.TYPE);
    }

    private List<Path> locateDimensionRoots() {
        final ResourceKey<LevelStem> stemKey = this.context.stemKey();
        final LinkedHashSet<Path> roots = new LinkedHashSet<>();
        roots.add(WorldMigrationSupport.getStorageFolder(stemKey.identifier(), this.sourceRoot));
        roots.add(DimensionType.getStorageFolder(this.context.dimensionKey(), this.sourceRoot));
        if (hasSourceContent(this.sourceRoot)) {
            roots.add(this.sourceRoot);
        }

        if (stemKey == LevelStem.NETHER) {
            roots.add(this.sourceRoot.resolve("DIM-1"));
        } else if (stemKey == LevelStem.END) {
            roots.add(this.sourceRoot.resolve("DIM1"));
        }

        roots.removeIf(path -> !Files.isDirectory(path));
        return List.copyOf(roots);
    }

    private static boolean hasSourceContent(final Path root) {
        if (Files.isDirectory(root.resolve(LevelResource.DATA.id()))) {
            return true;
        }
        if (Files.isRegularFile(root.resolve(WorldMigrationSupport.PAPER_WORLD_CONFIG)) || Files.isRegularFile(root.resolve("uid.dat"))) {
            return true;
        }
        for (final String directory : WorldMigrationSupport.DIMENSION_DIRECTORIES) {
            if (Files.isDirectory(root.resolve(directory))) {
                return true;
            }
        }
        return false;
    }

    private List<Path> locateSavedDataRoots() {
        final LinkedHashSet<Path> dataRoots = new LinkedHashSet<>();
        dataRoots.addAll(this.explicitDataRoots(this.locateDimensionRoots()));
        dataRoots.addAll(rootOwnedDataRoots(this.sourceRoot));
        return List.copyOf(dataRoots);
    }

    private void copySavedDataIfPresent(
        final SavedDataType<?> type
    ) throws IOException {
        WorldMigrationSupport.copySavedDataIfPresent(this.sourceDataRoots, this.targetDataRoot, type, true);
    }

    private static @Nullable UUID readLegacyUuid(final Path sourceRoot) {
        final Path fileId = sourceRoot.resolve("uid.dat");
        if (!Files.isRegularFile(fileId)) {
            return null;
        }

        try (DataInputStream inputStream = new DataInputStream(Files.newInputStream(fileId))) {
            return new UUID(inputStream.readLong(), inputStream.readLong());
        } catch (final IOException ex) {
            LOGGER.warn("Failed to read {}", fileId, ex);
            return null;
        }
    }

    private static void deleteMigratedSeparateRoot(final Path sourceRoot) throws IOException {
        if (!Files.exists(sourceRoot)) {
            return;
        }

        try (final var paths = Files.walk(sourceRoot)) {
            for (final Path path : paths.sorted(java.util.Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        }
    }

    private @Nullable Path findExplicitFile(
        final List<Path> dataRoots,
        final String... relativeCandidates
    ) {
        for (final Path dataRoot : dataRoots) {
            for (final String relativeCandidate : relativeCandidates) {
                final Path source = dataRoot.resolve(relativeCandidate);
                if (Files.isRegularFile(source)) {
                    return source;
                }
            }
        }
        return null;
    }

    private void migrateLegacyCraftBukkitPaperData(
        final SavedDataStorage targetStorage,
        final @Nullable Dynamic<?> levelData
    ) {
        targetStorage.set(PaperWorldMetadata.TYPE, new PaperWorldMetadata(requireNonNullElseGet(readLegacyUuid(this.sourceRoot), UUID::randomUUID)));
        final PaperWorldPDC preservedPdc = WorldMigrationSupport.readLegacyPdc(levelData, this.registryAccess);
        if (preservedPdc != null) {
            targetStorage.set(PaperWorldPDC.TYPE, preservedPdc);
        }
        targetStorage.set(PaperLevelOverrides.TYPE, PaperLevelOverrides.createFromRawLevelData(levelData));
    }

    private List<Path> explicitDataRoots(final List<Path> dimensionRoots) {
        final LinkedHashSet<Path> dataRoots = new LinkedHashSet<>();
        if (dimensionRoots.contains(this.sourceRoot)) {
            dataRoots.add(this.sourceRoot.resolve(net.minecraft.world.level.storage.LevelResource.DATA.id()));
        }
        for (final Path dimensionRoot : dimensionRoots) {
            if (dimensionRoot.equals(this.sourceRoot)) {
                continue;
            }
            dataRoots.add(dimensionRoot.resolve(net.minecraft.world.level.storage.LevelResource.DATA.id()));
        }
        return List.copyOf(dataRoots);
    }
}
