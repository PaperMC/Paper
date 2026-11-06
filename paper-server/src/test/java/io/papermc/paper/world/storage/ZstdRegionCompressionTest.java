package io.papermc.paper.world.storage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.chunk.storage.RegionFileVersion;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import org.bukkit.support.environment.Normal;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@Normal
class ZstdRegionCompressionTest {

    private static final RegionStorageInfo INFO = new RegionStorageInfo(
        "test", ResourceKey.create(Registries.DIMENSION, Identifier.withDefaultNamespace("overworld")), "chunk"
    );
    private static final int CUSTOM_COMPRESSION_ID = 127;
    private static final int EXTERNAL_FLAG = 128;
    private static final byte[] ZSTD_FRAME_MAGIC = {(byte) 0x28, (byte) 0xB5, (byte) 0x2F, (byte) 0xFD};

    // stored inside the region file
    private static final ChunkPos SMALL = new ChunkPos(1, 2);
    private static final CompoundTag SMALL_TAG = chunk(SMALL, 16 * 1024);
    // compresses to more than 255 sectors (the random data is incompressible), so it is stored in an external .mcc file
    private static final ChunkPos LARGE = new ChunkPos(3, 4);
    private static final CompoundTag LARGE_TAG = chunk(LARGE, 2 * 1024 * 1024);

    @TempDir
    Path dir;

    @Test
    void storesChunksInTheVanillaCustomCompressionLayout() throws IOException {
        final byte[] region = Files.readAllBytes(this.writeRegion());

        // internal chunk: length, custom compression id, "paper:zstd", zstd frame
        final int sector = ByteBuffer.wrap(region).getInt(offsetIndex(SMALL) * 4) >>> 8;
        final int start = sector * 4096;
        assertEquals(CUSTOM_COMPRESSION_ID, region[start + 4]);
        assertIdentifierThenZstdFrame(region, start + 5);

        // external chunk: the region file keeps the custom compression id with the external flag, the .mcc file has the rest
        final int externalSector = ByteBuffer.wrap(region).getInt(offsetIndex(LARGE) * 4) >>> 8;
        assertEquals((byte) (CUSTOM_COMPRESSION_ID | EXTERNAL_FLAG), region[externalSector * 4096 + 4]);
        assertIdentifierThenZstdFrame(Files.readAllBytes(this.dir.resolve("c.3.4.mcc")), 0);
    }

    @Test
    void readsChunksWhateverFormatIsSelectedForWriting() throws IOException {
        final Path file = this.writeRegion();
        try (RegionFile region = new RegionFile(INFO, file, this.dir, RegionFileVersion.VERSION_DEFLATE, false)) {
            assertEquals(SMALL_TAG, read(region, SMALL));
            assertEquals(LARGE_TAG, read(region, LARGE));
        }
    }

    @Test
    void unknownCustomCompressionReadsAsMissing() throws IOException {
        final Path file = this.writeRegion();
        final byte[] region = Files.readAllBytes(file);
        final byte[] stored = "paper:zstd".getBytes(StandardCharsets.UTF_8);
        final int sector = ByteBuffer.wrap(region).getInt(offsetIndex(SMALL) * 4) >>> 8;
        final int idStart = sector * 4096 + 5 + 2;
        assertArrayEquals(stored, Arrays.copyOfRange(region, idStart, idStart + stored.length));
        region[idStart + stored.length - 1] = 'x'; // paper:zstx, same length
        Files.write(file, region);

        try (RegionFile regionFile = new RegionFile(INFO, file, this.dir, RegionFileVersion.VERSION_PAPER_ZSTD, false)) {
            assertNull(read(regionFile, SMALL));
            assertEquals(LARGE_TAG, read(regionFile, LARGE));
        }
    }

    @Test
    void headerRecalculationRecoversZstdChunks() throws IOException {
        final Path file = this.writeRegion();
        final byte[] region = Files.readAllBytes(file);
        Arrays.fill(region, 0, 8192, (byte) 0); // lose the offset and timestamp tables
        Files.write(file, region);

        try (RegionFile regionFile = new RegionFile(INFO, file, this.dir, RegionFileVersion.VERSION_PAPER_ZSTD, false)) {
            assertFalse(regionFile.hasChunk(SMALL));
            assertFalse(regionFile.hasChunk(LARGE));

            regionFile.moonrise$recalculateHeader();

            assertEquals(SMALL_TAG, read(regionFile, SMALL));
            assertEquals(LARGE_TAG, read(regionFile, LARGE));
        }
    }

    private Path writeRegion() throws IOException {
        final Path file = this.dir.resolve("r.0.0.mca");
        try (RegionFile region = new RegionFile(INFO, file, this.dir, RegionFileVersion.VERSION_PAPER_ZSTD, false)) {
            write(region, SMALL, SMALL_TAG);
            write(region, LARGE, LARGE_TAG);
        }
        return file;
    }

    private static void write(final RegionFile region, final ChunkPos pos, final CompoundTag tag) throws IOException {
        try (DataOutputStream out = region.getChunkDataOutputStream(pos)) {
            NbtIo.write(tag, out);
        }
    }

    private static @Nullable CompoundTag read(final RegionFile region, final ChunkPos pos) throws IOException {
        try (DataInputStream in = region.getChunkDataInputStream(pos)) {
            return in == null ? null : NbtIo.read(in);
        }
    }

    private static void assertIdentifierThenZstdFrame(final byte[] data, final int start) throws IOException {
        final DataInputStream in = new DataInputStream(new ByteArrayInputStream(data, start, data.length - start));
        assertEquals("paper:zstd", in.readUTF());
        assertArrayEquals(ZSTD_FRAME_MAGIC, in.readNBytes(ZSTD_FRAME_MAGIC.length));
    }

    private static int offsetIndex(final ChunkPos pos) {
        return (pos.x() & 31) + (pos.z() & 31) * 32;
    }

    private static CompoundTag chunk(final ChunkPos pos, final int dataBytes) {
        final CompoundTag tag = new CompoundTag();
        tag.putInt("DataVersion", 4000); // any data version after the Level tag was removed, which header recalculation relies on
        tag.putInt("xPos", pos.x());
        tag.putInt("zPos", pos.z());
        tag.putLong("LastUpdate", 100L);
        final byte[] data = new byte[dataBytes];
        new Random(pos.x() * 31L + pos.z()).nextBytes(data);
        tag.putByteArray("data", data);
        return tag;
    }
}
