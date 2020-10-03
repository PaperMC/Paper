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
        return new SectionPosition(a(blockposition.getX()), a(blockposition.getY()), a(blockposition.getZ()));
    }

    public static SectionPosition a(ChunkCoordIntPair chunkcoordintpair, int i) {
        return new SectionPosition(chunkcoordintpair.x, i, chunkcoordintpair.z);
    }

    public static SectionPosition a(Entity entity) {
        return new SectionPosition(a(MathHelper.floor(entity.locX())), a(MathHelper.floor(entity.locY())), a(MathHelper.floor(entity.locZ())));
    }

    public static SectionPosition a(long i) {
        return new SectionPosition(b(i), c(i), d(i));
    }

    public static long a(long i, EnumDirection enumdirection) {
        return a(i, enumdirection.getAdjacentX(), enumdirection.getAdjacentY(), enumdirection.getAdjacentZ());
    }

    public static long a(long i, int j, int k, int l) {
        return b(b(i) + j, c(i) + k, d(i) + l);
    }

    public static int a(int i) {
        return i >> 4;
    }

    public static int b(int i) {
        return i & 15;
    }

    public static short b(BlockPosition blockposition) {
        int i = b(blockposition.getX());
        int j = b(blockposition.getY());
        int k = b(blockposition.getZ());

        return (short) (i << 8 | k << 4 | j << 0);
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

    public int d() {
        return this.a() << 4;
    }

    public int e() {
        return this.b() << 4;
    }

    public int f() {
        return this.c() << 4;
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

    public static long e(long i) {
        return b(a(BlockPosition.b(i)), a(BlockPosition.c(i)), a(BlockPosition.d(i)));
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

    public static long b(int i, int j, int k) {
        long l = 0L;

        l |= ((long) i & 4194303L) << 42;
        l |= ((long) j & 1048575L) << 0;
        l |= ((long) k & 4194303L) << 20;
        return l;
    }

    public long s() {
        return b(this.a(), this.b(), this.c());
    }

    public Stream<BlockPosition> t() {
        return BlockPosition.a(this.d(), this.e(), this.f(), this.g(), this.h(), this.i());
    }

    public static Stream<SectionPosition> a(SectionPosition sectionposition, int i) {
        int j = sectionposition.a();
        int k = sectionposition.b();
        int l = sectionposition.c();

        return a(j - i, k - i, l - i, j + i, k + i, l + i);
    }

    public static Stream<SectionPosition> b(ChunkCoordIntPair chunkcoordintpair, int i) {
        int j = chunkcoordintpair.x;
        int k = chunkcoordintpair.z;

        return a(j - i, 0, k - i, j + i, 15, k + i);
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
