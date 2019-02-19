package net.minecraft.server;

import com.google.common.annotations.VisibleForTesting;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegionFile implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ByteBuffer c = ByteBuffer.allocateDirect(1);
    private final FileChannel dataFile;
    private final java.nio.file.Path e;
    private final RegionFileCompression f;
    private final ByteBuffer g;
    private final IntBuffer h;
    private final IntBuffer i;
    @VisibleForTesting
    protected final RegionFileBitSet freeSectors;

    public RegionFile(File file, File file1, boolean flag) throws IOException {
        this(file.toPath(), file1.toPath(), RegionFileCompression.b, flag);
    }

    public RegionFile(java.nio.file.Path java_nio_file_path, java.nio.file.Path java_nio_file_path1, RegionFileCompression regionfilecompression, boolean flag) throws IOException {
        this.g = ByteBuffer.allocateDirect(8192);
        this.freeSectors = new RegionFileBitSet();
        this.f = regionfilecompression;
        if (!Files.isDirectory(java_nio_file_path1, new LinkOption[0])) {
            throw new IllegalArgumentException("Expected directory, got " + java_nio_file_path1.toAbsolutePath());
        } else {
            this.e = java_nio_file_path1;
            this.h = this.g.asIntBuffer();
            ((java.nio.Buffer) this.h).limit(1024);
            ((java.nio.Buffer) this.g).position(4096);
            this.i = this.g.asIntBuffer();
            if (flag) {
                this.dataFile = FileChannel.open(java_nio_file_path, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.DSYNC);
            } else {
                this.dataFile = FileChannel.open(java_nio_file_path, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
            }

            this.freeSectors.a(0, 2);
            ((java.nio.Buffer) this.g).position(0);
            int i = this.dataFile.read(this.g, 0L);

            if (i != -1) {
                if (i != 8192) {
                    RegionFile.LOGGER.warn("Region file {} has truncated header: {}", java_nio_file_path, i);
                }

                long j = Files.size(java_nio_file_path);

                for (int k = 0; k < 1024; ++k) {
                    int l = this.h.get(k);

                    if (l != 0) {
                        int i1 = b(l);
                        int j1 = a(l);
                        // Spigot start
                        if (j1 == 255) {
                            // We're maxed out, so we need to read the proper length from the section
                            ByteBuffer realLen = ByteBuffer.allocate(4);
                            this.dataFile.read(realLen, i1 * 4096);
                            j1 = (realLen.getInt(0) + 4) / 4096 + 1;
                        }
                        // Spigot end

                        if (i1 < 2) {
                            RegionFile.LOGGER.warn("Region file {} has invalid sector at index: {}; sector {} overlaps with header", java_nio_file_path, k, i1);
                            this.h.put(k, 0);
                        } else if (j1 == 0) {
                            RegionFile.LOGGER.warn("Region file {} has an invalid sector at index: {}; size has to be > 0", java_nio_file_path, k);
                            this.h.put(k, 0);
                        } else if ((long) i1 * 4096L > j) {
                            RegionFile.LOGGER.warn("Region file {} has an invalid sector at index: {}; sector {} is out of bounds", java_nio_file_path, k, i1);
                            this.h.put(k, 0);
                        } else {
                            this.freeSectors.a(i1, j1);
                        }
                    }
                }
            }

        }
    }

    private java.nio.file.Path e(ChunkCoordIntPair chunkcoordintpair) {
        String s = "c." + chunkcoordintpair.x + "." + chunkcoordintpair.z + ".mcc";

        return this.e.resolve(s);
    }

    @Nullable
    public synchronized DataInputStream a(ChunkCoordIntPair chunkcoordintpair) throws IOException {
        int i = this.getOffset(chunkcoordintpair);

        if (i == 0) {
            return null;
        } else {
            int j = b(i);
            int k = a(i);
            // Spigot start
            if (k == 255) {
                ByteBuffer realLen = ByteBuffer.allocate(4);
                this.dataFile.read(realLen, j * 4096);
                k = (realLen.getInt(0) + 4) / 4096 + 1;
            }
            // Spigot end
            int l = k * 4096;
            ByteBuffer bytebuffer = ByteBuffer.allocate(l);

            this.dataFile.read(bytebuffer, (long) (j * 4096));
            ((java.nio.Buffer) bytebuffer).flip();
            if (bytebuffer.remaining() < 5) {
                RegionFile.LOGGER.error("Chunk {} header is truncated: expected {} but read {}", chunkcoordintpair, l, bytebuffer.remaining());
                return null;
            } else {
                int i1 = bytebuffer.getInt();
                byte b0 = bytebuffer.get();

                if (i1 == 0) {
                    RegionFile.LOGGER.warn("Chunk {} is allocated, but stream is missing", chunkcoordintpair);
                    return null;
                } else {
                    int j1 = i1 - 1;

                    if (a(b0)) {
                        if (j1 != 0) {
                            RegionFile.LOGGER.warn("Chunk has both internal and external streams");
                        }

                        return this.a(chunkcoordintpair, b(b0));
                    } else if (j1 > bytebuffer.remaining()) {
                        RegionFile.LOGGER.error("Chunk {} stream is truncated: expected {} but read {}", chunkcoordintpair, j1, bytebuffer.remaining());
                        return null;
                    } else if (j1 < 0) {
                        RegionFile.LOGGER.error("Declared size {} of chunk {} is negative", i1, chunkcoordintpair);
                        return null;
                    } else {
                        return this.a(chunkcoordintpair, b0, a(bytebuffer, j1));
                    }
                }
            }
        }
    }

    private static boolean a(byte b0) {
        return (b0 & 128) != 0;
    }

    private static byte b(byte b0) {
        return (byte) (b0 & -129);
    }

    @Nullable
    private DataInputStream a(ChunkCoordIntPair chunkcoordintpair, byte b0, InputStream inputstream) throws IOException {
        RegionFileCompression regionfilecompression = RegionFileCompression.a(b0);

        if (regionfilecompression == null) {
            RegionFile.LOGGER.error("Chunk {} has invalid chunk stream version {}", chunkcoordintpair, b0);
            return null;
        } else {
            return new DataInputStream(new BufferedInputStream(regionfilecompression.a(inputstream)));
        }
    }

    @Nullable
    private DataInputStream a(ChunkCoordIntPair chunkcoordintpair, byte b0) throws IOException {
        java.nio.file.Path java_nio_file_path = this.e(chunkcoordintpair);

        if (!Files.isRegularFile(java_nio_file_path, new LinkOption[0])) {
            RegionFile.LOGGER.error("External chunk path {} is not file", java_nio_file_path);
            return null;
        } else {
            return this.a(chunkcoordintpair, b0, Files.newInputStream(java_nio_file_path));
        }
    }

    private static ByteArrayInputStream a(ByteBuffer bytebuffer, int i) {
        return new ByteArrayInputStream(bytebuffer.array(), bytebuffer.position(), i);
    }

    private int a(int i, int j) {
        return i << 8 | j;
    }

    private static int a(int i) {
        return i & 255;
    }

    private static int b(int i) {
        return i >> 8 & 16777215;
    }

    private static int c(int i) {
        return (i + 4096 - 1) / 4096;
    }

    public boolean b(ChunkCoordIntPair chunkcoordintpair) {
        int i = this.getOffset(chunkcoordintpair);

        if (i == 0) {
            return false;
        } else {
            int j = b(i);
            int k = a(i);
            ByteBuffer bytebuffer = ByteBuffer.allocate(5);

            try {
                this.dataFile.read(bytebuffer, (long) (j * 4096));
                ((java.nio.Buffer) bytebuffer).flip();
                if (bytebuffer.remaining() != 5) {
                    return false;
                } else {
                    int l = bytebuffer.getInt();
                    byte b0 = bytebuffer.get();

                    if (a(b0)) {
                        if (!RegionFileCompression.b(b(b0))) {
                            return false;
                        }

                        if (!Files.isRegularFile(this.e(chunkcoordintpair), new LinkOption[0])) {
                            return false;
                        }
                    } else {
                        if (!RegionFileCompression.b(b0)) {
                            return false;
                        }

                        if (l == 0) {
                            return false;
                        }

                        int i1 = l - 1;

                        if (i1 < 0 || i1 > 4096 * k) {
                            return false;
                        }
                    }

                    return true;
                }
            } catch (IOException ioexception) {
                return false;
            }
        }
    }

    public DataOutputStream c(ChunkCoordIntPair chunkcoordintpair) throws IOException {
        return new DataOutputStream(new BufferedOutputStream(this.f.a((OutputStream) (new RegionFile.ChunkBuffer(chunkcoordintpair)))));
    }

    public void a() throws IOException {
        this.dataFile.force(true);
    }

    protected synchronized void a(ChunkCoordIntPair chunkcoordintpair, ByteBuffer bytebuffer) throws IOException {
        int i = g(chunkcoordintpair);
        int j = this.h.get(i);
        int k = b(j);
        int l = a(j);
        int i1 = bytebuffer.remaining();
        int j1 = c(i1);
        int k1;
        RegionFile.b regionfile_b;

        if (j1 >= 256) {
            java.nio.file.Path java_nio_file_path = this.e(chunkcoordintpair);

            RegionFile.LOGGER.warn("Saving oversized chunk {} ({} bytes} to external file {}", chunkcoordintpair, i1, java_nio_file_path);
            j1 = 1;
            k1 = this.freeSectors.a(j1);
            regionfile_b = this.a(java_nio_file_path, bytebuffer);
            ByteBuffer bytebuffer1 = this.b();

            this.dataFile.write(bytebuffer1, (long) (k1 * 4096));
        } else {
            k1 = this.freeSectors.a(j1);
            regionfile_b = () -> {
                Files.deleteIfExists(this.e(chunkcoordintpair));
            };
            this.dataFile.write(bytebuffer, (long) (k1 * 4096));
        }

        int l1 = (int) (SystemUtils.getTimeMillis() / 1000L);

        this.h.put(i, this.a(k1, j1));
        this.i.put(i, l1);
        this.c();
        regionfile_b.run();
        if (k != 0) {
            this.freeSectors.b(k, l);
        }

    }

    private ByteBuffer b() {
        ByteBuffer bytebuffer = ByteBuffer.allocate(5);

        bytebuffer.putInt(1);
        bytebuffer.put((byte) (this.f.a() | 128));
        ((java.nio.Buffer) bytebuffer).flip();
        return bytebuffer;
    }

    private RegionFile.b a(java.nio.file.Path java_nio_file_path, ByteBuffer bytebuffer) throws IOException {
        java.nio.file.Path java_nio_file_path1 = Files.createTempFile(this.e, "tmp", (String) null);
        FileChannel filechannel = FileChannel.open(java_nio_file_path1, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        Throwable throwable = null;

        try {
            ((java.nio.Buffer) bytebuffer).position(5);
            filechannel.write(bytebuffer);
        } catch (Throwable throwable1) {
            throwable = throwable1;
            throw throwable1;
        } finally {
            if (filechannel != null) {
                if (throwable != null) {
                    try {
                        filechannel.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                } else {
                    filechannel.close();
                }
            }

        }

        return () -> {
            Files.move(java_nio_file_path1, java_nio_file_path, StandardCopyOption.REPLACE_EXISTING);
        };
    }

    private void c() throws IOException {
        ((java.nio.Buffer) this.g).position(0);
        this.dataFile.write(this.g, 0L);
    }

    private int getOffset(ChunkCoordIntPair chunkcoordintpair) {
        return this.h.get(g(chunkcoordintpair));
    }

    public boolean chunkExists(ChunkCoordIntPair chunkcoordintpair) {
        return this.getOffset(chunkcoordintpair) != 0;
    }

    private static int g(ChunkCoordIntPair chunkcoordintpair) {
        return chunkcoordintpair.j() + chunkcoordintpair.k() * 32;
    }

    public void close() throws IOException {
        try {
            this.d();
        } finally {
            try {
                this.dataFile.force(true);
            } finally {
                this.dataFile.close();
            }
        }

    }

    private void d() throws IOException {
        int i = (int) this.dataFile.size();
        int j = c(i) * 4096;

        if (i != j) {
            ByteBuffer bytebuffer = RegionFile.c.duplicate();

            ((java.nio.Buffer) bytebuffer).position(0);
            this.dataFile.write(bytebuffer, (long) (j - 1));
        }

    }

    interface b {

        void run() throws IOException;
    }

    class ChunkBuffer extends ByteArrayOutputStream {

        private final ChunkCoordIntPair b;

        public ChunkBuffer(ChunkCoordIntPair chunkcoordintpair) {
            super(8096);
            super.write(0);
            super.write(0);
            super.write(0);
            super.write(0);
            super.write(RegionFile.this.f.a());
            this.b = chunkcoordintpair;
        }

        public void close() throws IOException {
            ByteBuffer bytebuffer = ByteBuffer.wrap(this.buf, 0, this.count);

            bytebuffer.putInt(0, this.count - 5 + 1);
            RegionFile.this.a(this.b, bytebuffer);
        }
    }
}
