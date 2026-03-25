package io.papermc.paper.world;

import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Normal
class PaperWorldLoaderPathTest {

    @Test
    void targetWorldFolderUsesDimensionLayout() {
        assertEquals(
            Path.of("world_nether", "dimensions", "minecraft", "the_nether"),
            PaperWorldLoader.getTargetWorldFolder(Path.of("world_nether"), LevelStem.NETHER)
        );
        assertEquals(
            Path.of("world_the_end", "dimensions", "minecraft", "the_end"),
            PaperWorldLoader.getTargetWorldFolder(Path.of("world_the_end"), LevelStem.END)
        );
    }

    @Test
    void sourceWorldFolderFallsBackToLegacyBuiltinLayout(@TempDir final Path tempDir) throws Exception {
        final Path root = tempDir.resolve("world");
        Files.createDirectories(root.resolve("DIM-1"));

        assertEquals(root.resolve("DIM-1"), PaperWorldLoader.findSourceWorldFolder(root, LevelStem.NETHER));
    }

    @Test
    void sourceWorldFolderPrefersDimensionLayoutWhenPresent(@TempDir final Path tempDir) throws Exception {
        final Path root = tempDir.resolve("world");
        final Path netherModern = root.resolve("dimensions").resolve("minecraft").resolve("the_nether");
        Files.createDirectories(netherModern);

        assertEquals(netherModern, PaperWorldLoader.findSourceWorldFolder(root, LevelStem.NETHER));
    }

    @Test
    void dataOnlyDimensionsArtifactDoesNotCountAsExistingSplitWorldPayload(@TempDir final Path tempDir) throws Exception {
        // Datafixer migration of world files can migrate some of the end files preemptively, which was causing migration to halt.
        // This change verifies that we have also migrated the region/other dat files instead of just verifying folder existence.
        final Path root = tempDir.resolve("world_the_end");
        Files.createDirectories(root.resolve("dimensions").resolve("minecraft").resolve("the_end").resolve("data").resolve("minecraft"));

        assertFalse(PaperWorldLoader.hasExistingWorldPayload(root, LevelStem.END));
    }

    @Test
    void migratedSplitWorldCountsAsExistingPayload(@TempDir final Path tempDir) throws Exception {
        final Path root = tempDir.resolve("world_the_end");
        Files.createDirectories(root.resolve("dimensions").resolve("minecraft").resolve("the_end").resolve("region"));

        assertTrue(PaperWorldLoader.hasExistingWorldPayload(root, LevelStem.END));
    }

    @Test
    void legacySplitWorldPayloadCountsAsExistingPayload(@TempDir final Path tempDir) throws Exception {
        final Path targetRoot = tempDir.resolve("world_the_end");
        Files.createDirectories(targetRoot.resolve("DIM1").resolve("region"));

        assertTrue(PaperWorldLoader.hasExistingWorldPayload(targetRoot, LevelStem.END));
    }

    @Test
    void customDimensionFallbackMatchesCurrentDimensionsLayout(@TempDir final Path tempDir) throws Exception {
        final ResourceKey<LevelStem> customDimension = ResourceKey.create(
            Registries.LEVEL_STEM,
            Identifier.fromNamespaceAndPath("paper", "custom")
        );
        final Path root = tempDir.resolve("world");
        final Path customPath = root.resolve("dimensions").resolve("paper").resolve("custom");
        Files.createDirectories(customPath);

        assertEquals(customPath, PaperWorldLoader.getTargetWorldFolder(root, customDimension));
        assertEquals(customPath, PaperWorldLoader.findSourceWorldFolder(root, customDimension));
    }
}
