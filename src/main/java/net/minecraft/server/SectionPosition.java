package net.minecraft.server;

import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SectionPosition extends BaseBlockPosition {

    private SectionPosition(int i, int j, int k) {
        super(i, j, k);
    }

    public static SectionPosition a(int i, int j, int k) {
        return new SectionPosition(i, j, k);
    }

    public static SectionPosition a(BlockPosition blockposition) {
        return new SectionPosition(blockposition.getX() >> 4, blockposition.getY() >> 4, blockposition.getZ() >> 4); // Paper
    }

    public static SectionPosition a(ChunkCoordIntPair chunkcoordintpair, int i) {
        return new SectionPosition(chunkcoordintpair.x, i, chunkcoordintpair.z);
    }

    public static SectionPosition a(Entity entity) {
        return new SectionPosition(a(MathHelper.floor(entity.locX())), a(MathHelper.floor(entity.locY())), a(MathHelper.floor(entity.locZ())));
    }

    public static SectionPosition a(long i) {
        return new SectionPosition((int) (i >> 42), (int) (i << 44 >> 44), (int) (i << 22 >> 42)); // Paper
    }

    public static long a(long i, EnumDirection enumdirection) {
        return a(i, enumdirection.getAdjacentX(), enumdirection.getAdjacentY(), enumdirection.getAdjacentZ());
    }

    // Paper start
    public static long getAdjacentFromBlockPos(int x, int y, int z, EnumDirection enumdirection) {
        return (((long) ((x >> 4) + enumdirection.getAdjacentX()) & 4194303L) << 42) | (((long) ((y >> 4) + enumdirection.getAdjacentY()) & 1048575L)) | (((long) ((z >> 4) + enumdirection.getAdjacentZ()) & 4194303L) << 20);
    }
    public static long getAdjacentFromSectionPos(int x, int y, int z, EnumDirection enumdirection) {
        return (((long) (x + enumdirection.getAdjacentX()) & 4194303L) << 42) | (((long) ((y) + enumdirection.getAdjacentY()) & 1048575L)) | (((long) (z + enumdirection.getAdjacentZ()) & 4194303L) << 20);
    }
    // Paper end
    public static long a(long i, int j, int k, int l) {
        return (((long) ((int) (i >> 42) + j) & 4194303L) << 42) | (((long) ((int) (i << 44 >> 44) + k) & 1048575L)) | (((long) ((int) (i << 22 >> 42) + l) & 4194303L) << 20); // Simplify to reduce instruction count
    }

    public static int a(int i) {
        return i >> 4;
    }

    public static int b(int i) {
        return i & 15;
    }

    public static short b(BlockPosition blockposition) {
        return (short) ((blockposition.getX() & 15) << 8 | (blockposition.getZ() & 15) << 4 | blockposition.getY() & 15); // Paper - simplify/inline
    }

    public static int a(short short0) {
        return short0 >>> 8 & 15;
    }

    public static int b(short short0) {
        return short0 >>> 0 & 15;
    }

    public static int c(short short0) {
        return short0 >>> 4 & 15;
    }

    public int d(short short0) {
        return this.d() + a(short0);
    }

    public int e(short short0) {
        return this.e() + b(short0);
    }

    public int f(short short0) {
        return this.f() + c(short0);
    }

    public BlockPosition g(short short0) {
        return new BlockPosition(this.d(short0), this.e(short0), this.f(short0));
    }

    public static int c(int i) {
        return i << 4;
    }

    public static int b(long i) {
        return (int) (i << 0 >> 42);
    }

    public static int c(long i) {
        return (int) (i << 44 >> 44);
    }

    public static int d(long i) {
        return (int) (i << 22 >> 42);
    }

    public int a() {
        return this.getX();
    }

    public int b() {
        return this.getY();
    }

    public int c() {
        return this.getZ();
    }

    public final int d() { // Paper
        return this.getX() << 4; // Paper
    }

    public final int e() { // Paper
        return this.getY() << 4; // Paper
    }

    public final int f() { // Paper
        return this.getZ() << 4; // Paper
    }

    public int g() {
        return (this.a() << 4) + 15;
    }

    public int h() {
        return (this.b() << 4) + 15;
    }

    public int i() {
        return (this.c() << 4) + 15;
    }

    public static long blockToSection(long i) { return e(i); } // Paper - OBFHELPER
    public static long e(long i) {
        // b(a(BlockPosition.b(i)), a(BlockPosition.c(i)), a(BlockPosition.d(i)));
        return (((long) (int) (i >> 42) & 4194303L) << 42) | (((long) (int) ((i << 52) >> 56) & 1048575L)) | (((long) (int) ((i << 26) >> 42) & 4194303L) << 20); // Simplify to reduce instruction count
    }

    public static long f(long i) {
        return i & -1048576L;
    }

    public BlockPosition p() {
        return new BlockPosition(c(this.a()), c(this.b()), c(this.c()));
    }

    public BlockPosition q() {
        boolean flag = true;

        return this.p().b(8, 8, 8);
    }

    public ChunkCoordIntPair r() {
        return new ChunkCoordIntPair(this.a(), this.c());
    }

    // Paper start
    public static long blockPosAsSectionLong(int i, int j, int k) {
        return (((long) (i >> 4) & 4194303L) << 42) | (((long) (j >> 4) & 1048575L)) | (((long) (k >> 4) & 4194303L) << 20);
    }
    // Paper end
    public static long asLong(int i, int j, int k) { return b(i, j, k); } // Paper - OBFHELPER
    public static long b(int i, int j, int k) {
        return (((long) i & 4194303L) << 42) | (((long) j & 1048575L)) | (((long) k & 4194303L) << 20); // Paper - Simplify to reduce instruction count
    }

    public long s() {
        return (((long) getX() & 4194303L) << 42) | (((long) getY() & 1048575L)) | (((long) getZ() & 4194303L) << 20); // Paper - Simplify to reduce instruction count
    }

    public Stream<BlockPosition> t() {
        return BlockPosition.a(this.d(), this.e(), this.f(), this.g(), this.h(), this.i());
    }

    public static Stream<SectionPosition> a(SectionPosition sectionposition, int i) {
        return a(sectionposition.getX() - i, sectionposition.getY() - i, sectionposition.getZ() - i, sectionposition.getX() + i, sectionposition.getY() + i, sectionposition.getZ() + i); // Paper - simplify/inline
    }

    public static Stream<SectionPosition> b(ChunkCoordIntPair chunkcoordintpair, int i) {
        return a(chunkcoordintpair.x - i, 0, chunkcoordintpair.z - i, chunkcoordintpair.x + i, 15, chunkcoordintpair.z + i); // Paper - simplify/inline
    }

    public static Stream<SectionPosition> a(final int i, final int j, final int k, final int l, final int i1, final int j1) {
        return StreamSupport.stream(new AbstractSpliterator<SectionPosition>((long) ((l - i + 1) * (i1 - j + 1) * (j1 - k + 1)), 64) {
            final CursorPosition a = new CursorPosition(i, j, k, l, i1, j1);

            public boolean tryAdvance(Consumer<? super SectionPosition> consumer) {
                if (this.a.a()) {
                    consumer.accept(new SectionPosition(this.a.b(), this.a.c(), this.a.d()));
                    return true;
                } else {
                    return false;
                }
            }
        }, false);
    }
}
