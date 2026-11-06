package io.papermc.paper.world.storage;

import com.github.luben.zstd.Zstd;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.Objects;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@Normal
class ZstdRegionCompressionTest {

    private static final RegionStorageInfo INFO = new RegionStorageInfo(
        "test", ResourceKey.create(Registries.DIMENSION, Identifier.withDefaultNamespace("overworld")), "chunk"
    );
    private static final int CUSTOM_COMPRESSION_ID = 127;
    private static final int EXTERNAL_FLAG = 128;
    private static final byte[] ZSTD_FRAME_MAGIC = {(byte) 0x28, (byte) 0xB5, (byte) 0x2F, (byte) 0xFD};
    private static final String NO_DICTIONARY = "paper:zstd";
    private static final String DICTIONARY_1 = "paper:zstd_dict_1";
    // Chunks written with paper:zstd_dict_1 can only ever be read with this exact dictionary:
    // never change it, add a better dictionary as a new format instead.
    private static final String DICTIONARY_1_SHA256 = "2539eb142970c9d09078e1cd6e463eabf08e14a02396e31d1c871d2d8867cbe8";

    // stored inside the region file
    private static final ChunkPos SMALL = new ChunkPos(1, 2);
    private static final CompoundTag SMALL_TAG = chunk(SMALL, 16 * 1024);
    // compresses to more than 255 sectors (the random data is incompressible), so it is stored in an external .mcc file
    private static final ChunkPos LARGE = new ChunkPos(3, 4);
    private static final CompoundTag LARGE_TAG = chunk(LARGE, 2 * 1024 * 1024);

    @TempDir
    Path dir;

    @Test
    void zstdOptionWritesWithTheNewestDictionary() {
        try {
            RegionFileVersion.configure("zstd");
            assertSame(RegionFileVersion.VERSION_PAPER_ZSTD_DICT_1, RegionFileVersion.getSelected());
        } finally {
            RegionFileVersion.configure("deflate");
        }
    }

    @Test
    void shippedDictionaryNeverChanges() throws IOException, NoSuchAlgorithmException {
        final String sha256 = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(shippedDictionary()));
        assertEquals(DICTIONARY_1_SHA256, sha256, "zstd_dict_1.bin changed: chunks written with it would become unreadable");
    }

    @ParameterizedTest
    @ValueSource(strings = {NO_DICTIONARY, DICTIONARY_1})
    void storesChunksInTheVanillaCustomCompressionLayout(final String format) throws IOException {
        final byte[] region = Files.readAllBytes(this.writeRegion(version(format)));
        final long dictionaryId = format.equals(NO_DICTIONARY) ? 0 : Zstd.getDictIdFromDict(shippedDictionary());

        // internal chunk: length, custom compression id, format identifier, zstd frame
        assertEquals(CUSTOM_COMPRESSION_ID, region[chunkStart(region, SMALL) + 4]);
        assertFormatThenZstdFrame(internalStream(region, SMALL), format, dictionaryId);

        // external chunk: the region file keeps the custom compression id with the external flag, the .mcc file has the rest
        assertEquals((byte) (CUSTOM_COMPRESSION_ID | EXTERNAL_FLAG), region[chunkStart(region, LARGE) + 4]);
        assertFormatThenZstdFrame(Files.readAllBytes(this.dir.resolve("c.3.4.mcc")), format, dictionaryId);
    }

    @ParameterizedTest
    @ValueSource(strings = {NO_DICTIONARY, DICTIONARY_1})
    void readsChunksWhateverFormatIsSelectedForWriting(final String format) throws IOException {
        final Path file = this.writeRegion(version(format));
        try (RegionFile region = new RegionFile(INFO, file, this.dir, RegionFileVersion.VERSION_DEFLATE, false)) {
            assertEquals(SMALL_TAG, read(region, SMALL));
            assertEquals(LARGE_TAG, read(region, LARGE));
        }
    }

    @Test
    void readsRegionsMixingBothZstdFormats() throws IOException {
        // e.g. a world written by a build without the dictionary, then by one with it
        final Path file = this.dir.resolve("r.0.0.mca");
        try (RegionFile region = new RegionFile(INFO, file, this.dir, RegionFileVersion.VERSION_PAPER_ZSTD, false)) {
            write(region, SMALL, SMALL_TAG);
        }
        try (RegionFile region = new RegionFile(INFO, file, this.dir, RegionFileVersion.VERSION_PAPER_ZSTD_DICT_1, false)) {
            write(region, LARGE, LARGE_TAG);
        }
        assertEquals(NO_DICTIONARY, new DataInputStream(new ByteArrayInputStream(internalStream(Files.readAllBytes(file), SMALL))).readUTF());
        assertEquals(DICTIONARY_1, new DataInputStream(new ByteArrayInputStream(Files.readAllBytes(this.dir.resolve("c.3.4.mcc")))).readUTF());

        try (RegionFile region = new RegionFile(INFO, file, this.dir, RegionFileVersion.VERSION_DEFLATE, false)) {
            assertEquals(SMALL_TAG, read(region, SMALL));
            assertEquals(LARGE_TAG, read(region, LARGE));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {NO_DICTIONARY, DICTIONARY_1})
    void unknownCustomCompressionReadsAsMissing(final String format) throws IOException {
        final Path file = this.writeRegion(version(format));
        final byte[] region = Files.readAllBytes(file);
        final byte[] stored = format.getBytes(StandardCharsets.UTF_8);
        final int idStart = chunkStart(region, SMALL) + 5 + 2;
        assertArrayEquals(stored, Arrays.copyOfRange(region, idStart, idStart + stored.length));
        region[idStart + stored.length - 1] = 'x'; // same length, unknown identifier
        Files.write(file, region);

        try (RegionFile regionFile = new RegionFile(INFO, file, this.dir, version(format), false)) {
            assertNull(read(regionFile, SMALL));
            assertEquals(LARGE_TAG, read(regionFile, LARGE));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {NO_DICTIONARY, DICTIONARY_1})
    void headerRecalculationRecoversZstdChunks(final String format) throws IOException {
        final Path file = this.writeRegion(version(format));
        final byte[] region = Files.readAllBytes(file);
        Arrays.fill(region, 0, 8192, (byte) 0); // lose the offset and timestamp tables
        Files.write(file, region);

        try (RegionFile regionFile = new RegionFile(INFO, file, this.dir, version(format), false)) {
            assertFalse(regionFile.hasChunk(SMALL));
            assertFalse(regionFile.hasChunk(LARGE));

            regionFile.moonrise$recalculateHeader();

            assertEquals(SMALL_TAG, read(regionFile, SMALL));
            assertEquals(LARGE_TAG, read(regionFile, LARGE));
        }
    }

    private Path writeRegion(final RegionFileVersion version) throws IOException {
        final Path file = this.dir.resolve("r.0.0.mca");
        try (RegionFile region = new RegionFile(INFO, file, this.dir, version, false)) {
            write(region, SMALL, SMALL_TAG);
            write(region, LARGE, LARGE_TAG);
        }
        return file;
    }

    private static RegionFileVersion version(final String format) {
        return Objects.requireNonNull(RegionFileVersion.fromCustomId(Identifier.parse(format)), format);
    }

    private static byte[] shippedDictionary() throws IOException {
        try (InputStream in = ZstdRegionCompression.class.getResourceAsStream("zstd_dict_1.bin")) {
            return Objects.requireNonNull(in, "zstd_dict_1.bin").readAllBytes();
        }
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

    private static void assertFormatThenZstdFrame(final byte[] stream, final String format, final long dictionaryId) throws IOException {
        final DataInputStream in = new DataInputStream(new ByteArrayInputStream(stream));
        assertEquals(format, in.readUTF());
        final byte[] frame = in.readAllBytes();
        assertArrayEquals(ZSTD_FRAME_MAGIC, Arrays.copyOf(frame, ZSTD_FRAME_MAGIC.length));
        assertEquals(dictionaryId, Zstd.getDictIdFromFrame(frame));
    }

    private static int chunkStart(final byte[] region, final ChunkPos pos) {
        return (ByteBuffer.wrap(region).getInt(offsetIndex(pos) * 4) >>> 8) * 4096;
    }

    // the stored chunk stream of an internal chunk: everything after its length and compression id
    private static byte[] internalStream(final byte[] region, final ChunkPos pos) {
        final int start = chunkStart(region, pos);
        final int length = ByteBuffer.wrap(region).getInt(start);
        return Arrays.copyOfRange(region, start + 5, start + 4 + length);
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
