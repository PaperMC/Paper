package io.papermc.paper.world.storage;

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdCompressCtx;
import com.github.luben.zstd.ZstdDecompressCtx;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.minecraft.world.level.chunk.storage.RegionFile;

/**
 * Region file compression formats that vanilla does not know about.
 * <p>
 * They are stored with the vanilla "custom" compression id (127): the chunk stream starts with a
 * {@link DataOutputStream#writeUTF(String) UTF} identifier naming the codec, followed by the codec's data.
 * Vanilla reports such chunks as "Unrecognized custom compression" instead of treating them as corrupt,
 * and the identifier can never collide with compression ids Mojang adds later.
 */
public final class CustomRegionCompression {

    public static final String ZSTD_ID = "paper:zstd";
    // Level 6 is smaller than vanilla deflate while compressing ~3x and decompressing ~3x faster on real chunk data.
    public static final int ZSTD_LEVEL = Integer.getInteger("Paper.region-file-compression.zstd-level", 6);

    // Chunks are compressed whole (they are fully buffered in memory anyway), so reuse one native context per thread
    // instead of allocating a new streaming context for every chunk.
    private static final ThreadLocal<ZstdCompressCtx> ZSTD_COMPRESS = ThreadLocal.withInitial(() -> {
        final ZstdCompressCtx ctx = new ZstdCompressCtx();
        ctx.setLevel(ZSTD_LEVEL);
        return ctx;
    });
    private static final ThreadLocal<ZstdDecompressCtx> ZSTD_DECOMPRESS = ThreadLocal.withInitial(ZstdDecompressCtx::new);

    private CustomRegionCompression() {
    }

    public static InputStream wrapInput(final InputStream in) throws IOException {
        final String id = new DataInputStream(in).readUTF();
        if (ZSTD_ID.equals(id)) {
            return new ByteArrayInputStream(decompressZstd(in.readAllBytes()));
        }
        throw new UnknownCompressionException(id);
    }

    public static OutputStream wrapZstdOutput(final OutputStream out) throws IOException {
        new DataOutputStream(out).writeUTF(ZSTD_ID); // DataOutputStream is unbuffered, this goes straight to out
        return new ZstdChunkOutputStream(out);
    }

    private static byte[] decompressZstd(final byte[] compressed) throws IOException {
        final long size = Zstd.getFrameContentSize(compressed, 0, compressed.length);
        if (size < 0 || size > RegionFile.MAX_CHUNK_SIZE) {
            throw new IOException("Invalid zstd frame content size " + size);
        }
        final byte[] result = new byte[(int) size];
        final int read = ZSTD_DECOMPRESS.get().decompressByteArray(result, 0, result.length, compressed, 0, compressed.length);
        if (read != result.length) {
            throw new IOException("zstd frame decompressed to " + read + " bytes, expected " + result.length);
        }
        return result;
    }

    private static final class ZstdChunkOutputStream extends ByteArrayOutputStream {
        private final OutputStream out;
        private boolean closed;

        private ZstdChunkOutputStream(final OutputStream out) {
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
            final int length = ZSTD_COMPRESS.get().compressByteArray(compressed, 0, compressed.length, this.buf, 0, this.count);
            this.out.write(compressed, 0, length);
            this.out.close();
        }
    }

    public static final class UnknownCompressionException extends IOException {
        private final String compressionId;

        public UnknownCompressionException(final String compressionId) {
            super("Unknown custom region compression " + compressionId);
            this.compressionId = compressionId;
        }

        public String compressionId() {
            return this.compressionId;
        }
    }
}
