package io.papermc.paper.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.chunk.storage.RegionFileVersion;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("Normal")
class RegionFileTest {
    private static final int SECTOR_BYTES = 4096;
    private static final int HEADER_BYTES = 2 * SECTOR_BYTES;
    private static final RegionStorageInfo STORAGE_INFO = new RegionStorageInfo("test", Level.OVERWORLD, "chunk");
    private static final ChunkPos FIRST_CHUNK = new ChunkPos(0, 0);
    private static final ChunkPos SECOND_CHUNK = new ChunkPos(1, 0);

    @Test
    void flushTruncatesClearedTailSectors(@TempDir final Path directory) throws IOException {
        final Path regionPath = directory.resolve("r.0.0.mca");
        try (RegionFile regionFile = open(regionPath, directory)) {
            write(regionFile, FIRST_CHUNK, payload(20_000, 1));
            regionFile.clear(FIRST_CHUNK);
            regionFile.flush();

            assertEquals(HEADER_BYTES, Files.size(regionPath));
            assertNull(regionFile.getChunkDataInputStream(FIRST_CHUNK));
        }
    }

    @Test
    void flushRetainsLiveSectorsAndData(@TempDir final Path directory) throws IOException {
        final Path regionPath = directory.resolve("r.0.0.mca");
        final byte[] retained = payload(20_000, 2);
        try (RegionFile regionFile = open(regionPath, directory)) {
            write(regionFile, FIRST_CHUNK, retained);
            final long retainedLength = HEADER_BYTES + 5L * SECTOR_BYTES;
            write(regionFile, SECOND_CHUNK, payload(2_000, 3));
            regionFile.clear(SECOND_CHUNK);
            regionFile.flush();

            assertEquals(retainedLength, Files.size(regionPath));
            assertArrayEquals(retained, read(regionFile, FIRST_CHUNK));
        }

        try (RegionFile reopened = open(regionPath, directory)) {
            assertArrayEquals(retained, read(reopened, FIRST_CHUNK));
            assertNull(reopened.getChunkDataInputStream(SECOND_CHUNK));
        }
    }

    @Test
    void flushDoesNotRemoveInteriorFreeSectors(@TempDir final Path directory) throws IOException {
        final Path regionPath = directory.resolve("r.0.0.mca");
        final byte[] retained = payload(2_000, 6);
        try (RegionFile regionFile = open(regionPath, directory)) {
            write(regionFile, FIRST_CHUNK, payload(20_000, 7));
            write(regionFile, SECOND_CHUNK, retained);
            final long lengthWithBothAllocations = Files.size(regionPath);
            regionFile.clear(FIRST_CHUNK);
            regionFile.flush();

            assertEquals(lengthWithBothAllocations, Files.size(regionPath));
            assertArrayEquals(retained, read(regionFile, SECOND_CHUNK));
        }
    }

    @Test
    void flushTruncatesTailLeftByPreviousProcess(@TempDir final Path directory) throws IOException {
        final Path regionPath = directory.resolve("r.0.0.mca");
        Files.write(regionPath, new byte[HEADER_BYTES]);
        Files.write(regionPath, new byte[SECTOR_BYTES], StandardOpenOption.APPEND);

        try (RegionFile regionFile = open(regionPath, directory)) {
            regionFile.flush();
            assertEquals(HEADER_BYTES, Files.size(regionPath));
        }
    }

    @Test
    void closeTruncatesSectorsFreedByReplacement(@TempDir final Path directory) throws IOException {
        final Path regionPath = directory.resolve("r.0.0.mca");
        final byte[] replacement = payload(2_000, 4);
        try (RegionFile regionFile = open(regionPath, directory)) {
            write(regionFile, FIRST_CHUNK, payload(20_000, 5));
            write(regionFile, FIRST_CHUNK, replacement);
            write(regionFile, FIRST_CHUNK, replacement);
        }

        assertEquals(HEADER_BYTES + SECTOR_BYTES, Files.size(regionPath));
        try (RegionFile reopened = open(regionPath, directory)) {
            assertArrayEquals(replacement, read(reopened, FIRST_CHUNK));
        }
    }

    private static RegionFile open(final Path path, final Path directory) throws IOException {
        return new RegionFile(STORAGE_INFO, path, directory, RegionFileVersion.VERSION_NONE, false);
    }

    private static void write(final RegionFile regionFile, final ChunkPos pos, final byte[] payload) throws IOException {
        try (DataOutputStream output = regionFile.getChunkDataOutputStream(pos)) {
            output.write(payload);
        }
    }

    private static byte[] read(final RegionFile regionFile, final ChunkPos pos) throws IOException {
        try (DataInputStream input = regionFile.getChunkDataInputStream(pos)) {
            return input.readAllBytes();
        }
    }

    private static byte[] payload(final int size, final int value) {
        final byte[] payload = new byte[size];
        Arrays.fill(payload, (byte)value);
        return payload;
    }
}
