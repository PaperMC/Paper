package io.papermc.paper.world.storage;

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdCompressCtx;
import com.github.luben.zstd.ZstdDecompressCtx;
import com.github.luben.zstd.ZstdDictCompress;
import com.github.luben.zstd.ZstdDictDecompress;
import com.google.common.base.Suppliers;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.function.Supplier;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.chunk.storage.RegionFileVersion;
import org.jspecify.annotations.Nullable;

/**
 * The codecs of the zstd region file compression formats: {@code paper:zstd} without a dictionary
 * ({@link RegionFileVersion#VERSION_PAPER_ZSTD}) and {@code paper:zstd_dict_1} with the first shipped dictionary
 * ({@link RegionFileVersion#VERSION_PAPER_ZSTD_DICT_1}).
 * <p>
 * Chunks using them are stored like Vanilla expects custom compression formats: the custom compression id (127),
 * then the identifier of the format, both handled by {@link RegionFileVersion} and {@link RegionFile}.
 * This class only handles what follows them: one zstd frame with the chunk data.
 * <p>
 * A chunk compressed with a dictionary can only ever be read with that exact dictionary, so shipped dictionaries are
 * never changed or removed. A better dictionary is added as a new format with a new identifier.
 */
public final class ZstdRegionCompression {

    // Level 6 is smaller than vanilla deflate while compressing ~3x and decompressing ~3x faster on real chunk data.
    // With the dictionary, it is ~8% smaller than deflate on disk while still compressing ~2x and decompressing ~3x faster.
    public static final int LEVEL = Integer.getInteger("Paper.region-file-compression.zstd-level", 6);

    public static final ZstdRegionCompression NO_DICTIONARY = new ZstdRegionCompression(null);
    // 112 KiB, trained with zstd's default trainer on 2,000 chunks from two freshly generated 26.3 worlds with different seeds.
    public static final ZstdRegionCompression DICTIONARY_1 = new ZstdRegionCompression("zstd_dict_1.bin");

    // Created on first use, so the native library and the dictionary are only loaded by servers that use the format.
    private final Supplier<Contexts> contexts;

    private ZstdRegionCompression(final @Nullable String dictionaryResource) {
        this.contexts = Suppliers.memoize(() -> new Contexts(dictionaryResource == null ? null : readDictionary(dictionaryResource)));
    }

    public InputStream wrapInput(final InputStream in) throws IOException {
        return new ByteArrayInputStream(this.decompress(in.readAllBytes()));
    }

    public OutputStream wrapOutput(final OutputStream out) {
        return new ChunkOutputStream(out, this.contexts.get());
    }

    private byte[] decompress(final byte[] compressed) throws IOException {
        final long size = Zstd.getFrameContentSize(compressed, 0, compressed.length);
        if (size < 0 || size > RegionFile.MAX_CHUNK_SIZE) {
            throw new IOException("Invalid zstd frame content size " + size);
        }
        final byte[] result = new byte[(int) size];
        final int read = this.contexts.get().decompress.get().decompressByteArray(result, 0, result.length, compressed, 0, compressed.length);
        if (read != result.length) {
            throw new IOException("zstd frame decompressed to " + read + " bytes, expected " + result.length);
        }
        return result;
    }

    private static byte[] readDictionary(final String resource) {
        try (InputStream in = ZstdRegionCompression.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Missing zstd dictionary " + resource);
            }
            return in.readAllBytes();
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to read zstd dictionary " + resource, e);
        }
    }

    private static final class Contexts {
        // Digested once and shared by the contexts of every thread
        private final @Nullable ZstdDictCompress compressDictionary;
        private final @Nullable ZstdDictDecompress decompressDictionary;
        // Chunks are compressed whole (they are fully buffered in memory anyway), so reuse one native context per thread
        // instead of allocating a new streaming context for every chunk.
        private final ThreadLocal<ZstdCompressCtx> compress;
        private final ThreadLocal<ZstdDecompressCtx> decompress;

        private Contexts(final byte @Nullable [] dictionary) {
            final ZstdDictCompress compressDictionary = dictionary == null ? null : new ZstdDictCompress(dictionary, LEVEL);
            final ZstdDictDecompress decompressDictionary = dictionary == null ? null : new ZstdDictDecompress(dictionary);
            this.compressDictionary = compressDictionary;
            this.decompressDictionary = decompressDictionary;
            this.compress = ThreadLocal.withInitial(() -> {
                final ZstdCompressCtx ctx = new ZstdCompressCtx();
                ctx.setLevel(LEVEL);
                if (compressDictionary != null) {
                    ctx.loadDict(compressDictionary);
                }
                return ctx;
            });
            this.decompress = ThreadLocal.withInitial(() -> {
                final ZstdDecompressCtx ctx = new ZstdDecompressCtx();
                if (decompressDictionary != null) {
                    ctx.loadDict(decompressDictionary);
                }
                return ctx;
            });
        }
    }

    private static final class ChunkOutputStream extends ByteArrayOutputStream {
        private final OutputStream out;
        private final Contexts contexts;
        private boolean closed;

        private ChunkOutputStream(final OutputStream out, final Contexts contexts) {
            super(16 * 1024);
            this.out = out;
            this.contexts = contexts;
        }

        @Override
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            this.closed = true;
            final byte[] compressed = new byte[(int) Zstd.compressBound(this.count)];
            final int length = this.contexts.compress.get().compressByteArray(compressed, 0, compressed.length, this.buf, 0, this.count);
            this.out.write(compressed, 0, length);
            this.out.close();
        }
    }
}
