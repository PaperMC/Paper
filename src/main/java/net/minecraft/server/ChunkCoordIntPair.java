package net.minecraft.server;

import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;

public class ChunkCoordIntPair {

    public static final long a = pair(1875016, 1875016);
    public final int x;
    public final int z;

    public ChunkCoordIntPair(int i, int j) {
        this.x = i;
        this.z = j;
    }

    public ChunkCoordIntPair(BlockPosition blockposition) {
        this.x = blockposition.getX() >> 4;
        this.z = blockposition.getZ() >> 4;
    }

    public ChunkCoordIntPair(long i) {
        this.x = (int) i;
        this.z = (int) (i >> 32);
    }

    public long pair() {
        return pair(this.x, this.z);
    }

    public static long pair(int i, int j) {
        return (long) i & 4294967295L | ((long) j & 4294967295L) << 32;
    }

    public static int getX(long i) {
        return (int) (i & 4294967295L);
    }

    public static int getZ(long i) {
        return (int) (i >>> 32 & 4294967295L);
    }

    public int hashCode() {
        int i = 1664525 * this.x + 1013904223;
        int j = 1664525 * (this.z ^ -559038737) + 1013904223;

        return i ^ j;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ChunkCoordIntPair)) {
            return false;
        } else {
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) object;

            return this.x == chunkcoordintpair.x && this.z == chunkcoordintpair.z;
        }
    }

    public int d() {
        return this.x << 4;
    }

    public int e() {
        return this.z << 4;
    }

    public int f() {
        return (this.x << 4) + 15;
    }

    public int g() {
        return (this.z << 4) + 15;
    }

    public int getRegionX() {
        return this.x >> 5;
    }

    public int getRegionZ() {
        return this.z >> 5;
    }

    public int j() {
        return this.x & 31;
    }

    public int k() {
        return this.z & 31;
    }

    public String toString() {
        return "[" + this.x + ", " + this.z + "]";
    }

    public BlockPosition l() {
        return new BlockPosition(this.d(), 0, this.e());
    }

    public int a(ChunkCoordIntPair chunkcoordintpair) {
        return Math.max(Math.abs(this.x - chunkcoordintpair.x), Math.abs(this.z - chunkcoordintpair.z));
    }

    public static Stream<ChunkCoordIntPair> a(ChunkCoordIntPair chunkcoordintpair, int i) {
        return a(new ChunkCoordIntPair(chunkcoordintpair.x - i, chunkcoordintpair.z - i), new ChunkCoordIntPair(chunkcoordintpair.x + i, chunkcoordintpair.z + i));
    }

    public static Stream<ChunkCoordIntPair> a(final ChunkCoordIntPair chunkcoordintpair, final ChunkCoordIntPair chunkcoordintpair1) {
        int i = Math.abs(chunkcoordintpair.x - chunkcoordintpair1.x) + 1;
        int j = Math.abs(chunkcoordintpair.z - chunkcoordintpair1.z) + 1;
        final int k = chunkcoordintpair.x < chunkcoordintpair1.x ? 1 : -1;
        final int l = chunkcoordintpair.z < chunkcoordintpair1.z ? 1 : -1;

        return StreamSupport.stream(new AbstractSpliterator<ChunkCoordIntPair>((long) (i * j), 64) {
            @Nullable
            private ChunkCoordIntPair e;

            public boolean tryAdvance(Consumer<? super ChunkCoordIntPair> consumer) {
                if (this.e == null) {
                    this.e = chunkcoordintpair;
                } else {
                    int i1 = this.e.x;
                    int j1 = this.e.z;

                    if (i1 == chunkcoordintpair1.x) {
                        if (j1 == chunkcoordintpair1.z) {
                            return false;
                        }

                        this.e = new ChunkCoordIntPair(chunkcoordintpair.x, j1 + l);
                    } else {
                        this.e = new ChunkCoordIntPair(i1 + k, j1);
                    }
                }

                consumer.accept(this.e);
                return true;
            }
        }, false);
    }
}
