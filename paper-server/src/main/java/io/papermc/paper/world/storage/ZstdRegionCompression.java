package io.papermc.paper.world.storage;

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdCompressCtx;
import com.github.luben.zstd.ZstdDecompressCtx;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.chunk.storage.RegionFileVersion;

/**
 * The codec of the {@code paper:zstd} region file compression format ({@link RegionFileVersion#VERSION_PAPER_ZSTD}).
 * <p>
 * Chunks using it are stored like Vanilla expects custom compression formats: the custom compression id (127),
 * then the {@code paper:zstd} identifier, both handled by {@link RegionFileVersion} and {@link RegionFile}.
 * This class only handles what follows them: one zstd frame with the chunk data.
 */
public final class ZstdRegionCompression {

    // Level 6 is smaller than vanilla deflate while compressing ~3x and decompressing ~3x faster on real chunk data.
    public static final int LEVEL = Integer.getInteger("Paper.region-file-compression.zstd-level", 6);

    // Chunks are compressed whole (they are fully buffered in memory anyway), so reuse one native context per thread
    // instead of allocating a new streaming context for every chunk.
    private static final ThreadLocal<ZstdCompressCtx> COMPRESS = ThreadLocal.withInitial(() -> {
        final ZstdCompressCtx ctx = new ZstdCompressCtx();
        ctx.setLevel(LEVEL);
        return ctx;
    });
    private static final ThreadLocal<ZstdDecompressCtx> DECOMPRESS = ThreadLocal.withInitial(ZstdDecompressCtx::new);

    private ZstdRegionCompression() {
    }

    public static InputStream wrapInput(final InputStream in) throws IOException {
        return new ByteArrayInputStream(decompress(in.readAllBytes()));
    }

    public static OutputStream wrapOutput(final OutputStream out) {
        return new ChunkOutputStream(out);
    }

    private static byte[] decompress(final byte[] compressed) throws IOException {
        final long size = Zstd.getFrameContentSize(compressed, 0, compressed.length);
        if (size < 0 || size > RegionFile.MAX_CHUNK_SIZE) {
            throw new IOException("Invalid zstd frame content size " + size);
        }
        final byte[] result = new byte[(int) size];
        final int read = DECOMPRESS.get().decompressByteArray(result, 0, result.length, compressed, 0, compressed.length);
        if (read != result.length) {
            throw new IOException("zstd frame decompressed to " + read + " bytes, expected " + result.length);
        }
        return result;
    }

    private static final class ChunkOutputStream extends ByteArrayOutputStream {
        private final OutputStream out;
        private boolean closed;

        private ChunkOutputStream(final OutputStream out) {
            super(16 * 1024);
            this.out = out;
        }

        @Override
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            this.closed = true;
            final byte[] compressed = new byte[(int) Zstd.compressBound(this.count)];
            final int length = COMPRESS.get().compressByteArray(compressed, 0, compressed.length, this.buf, 0, this.count);
            this.out.write(compressed, 0, length);
            this.out.close();
        }
    }
}
