package net.minecraft.server;

import com.google.common.collect.AbstractIterator;
import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Immutable
public class BlockPosition extends BaseBlockPosition {

    public static final Codec<BlockPosition> a = Codec.INT_STREAM.comapFlatMap((intstream) -> {
        return SystemUtils.a(intstream, 3).map((aint) -> {
            return new BlockPosition(aint[0], aint[1], aint[2]);
        });
    }, (blockposition) -> {
        return IntStream.of(new int[]{blockposition.getX(), blockposition.getY(), blockposition.getZ()});
    }).stable();
    private static final Logger LOGGER = LogManager.getLogger();
    public static final BlockPosition ZERO = new BlockPosition(0, 0, 0);
    // Paper start - static constants
    private static final int f = 26;
    private static final int g = 26;
    private static final int h = 12;
    private static final long i = 67108863;
    private static final long j = 4095;
    private static final long k = 67108863;
    private static final int l = 12;
    private static final int m = 38;
    // Paper end

    public BlockPosition(int i, int j, int k) {
        super(i, j, k);
    }

    public BlockPosition(double d0, double d1, double d2) {
        super(d0, d1, d2);
    }

    public BlockPosition(Vec3D vec3d) {
        this(vec3d.x, vec3d.y, vec3d.z);
    }

    public BlockPosition(IPosition iposition) {
        this(iposition.getX(), iposition.getY(), iposition.getZ());
    }

    public BlockPosition(BaseBlockPosition baseblockposition) {
        this(baseblockposition.getX(), baseblockposition.getY(), baseblockposition.getZ());
    }

    public static long getAdjacent(int baseX, int baseY, int baseZ, EnumDirection enumdirection) { return asLong(baseX + enumdirection.getAdjacentX(), baseY + enumdirection.getAdjacentY(), baseZ + enumdirection.getAdjacentZ()); } // Paper
    public static long a(long i, EnumDirection enumdirection) {
        return a(i, enumdirection.getAdjacentX(), enumdirection.getAdjacentY(), enumdirection.getAdjacentZ());
    }

    public static long a(long i, int j, int k, int l) {
        return a((int) (i >> 38) + j, (int) ((i << 52) >> 52) + k, (int) ((i << 26) >> 38) + l); // Paper - simplify/inline
    }

    public static int b(long i) {
        return (int) (i >> 38); // Paper - simplify/inline
    }

    public static int c(long i) {
        return (int) ((i << 52) >> 52); // Paper - simplify/inline
    }

    public static int d(long i) {
        return (int) ((i << 26) >> 38);  // Paper - simplify/inline
    }

    public static BlockPosition fromLong(long i) {
        return new BlockPosition((int) (i >> 38), (int) ((i << 52) >> 52), (int) ((i << 26) >> 38)); // Paper - simplify/inline
    }

    public long asLong() {
        return a(this.getX(), this.getY(), this.getZ());
    }

    public static long asLong(int x, int y, int z) { return a(x, y, z); } // Paper - OBFHELPER
    public static long a(int i, int j, int k) {
        return (((long) i & (long) 67108863) << 38) | (((long) j & (long) 4095)) | (((long) k & (long) 67108863) << 12); // Paper - inline constants and simplify
    }

    public static long f(long i) {
        return i & -16L;
    }

    public BlockPosition a(double d0, double d1, double d2) {
        return d0 == 0.0D && d1 == 0.0D && d2 == 0.0D ? this : new BlockPosition((double) this.getX() + d0, (double) this.getY() + d1, (double) this.getZ() + d2);
    }

    public final BlockPosition add(int i, int j, int k) {return b(i, j, k);} // Paper - OBFHELPER
    public BlockPosition b(int i, int j, int k) {
        return i == 0 && j == 0 && k == 0 ? this : new BlockPosition(this.getX() + i, this.getY() + j, this.getZ() + k);
    }

    public final BlockPosition add(BaseBlockPosition baseblockposition) { return this.a(baseblockposition); } // Paper - OBFHELPER
    public BlockPosition a(BaseBlockPosition baseblockposition) {
        return this.b(baseblockposition.getX(), baseblockposition.getY(), baseblockposition.getZ());
    }

    public BlockPosition b(BaseBlockPosition baseblockposition) {
        return this.b(-baseblockposition.getX(), -baseblockposition.getY(), -baseblockposition.getZ());
    }

    @Override
    public BlockPosition up() {
        return new BlockPosition(this.getX(), this.getY() + 1, this.getZ()); // Paper - Optimize BlockPosition
    }

    @Override
    public BlockPosition up(int i) {
        return i == 0 ? this : new BlockPosition(this.getX(), this.getY() + i, this.getZ()); // Paper - Optimize BlockPosition
    }

    @Override
    public BlockPosition down() {
        return new BlockPosition(this.getX(), this.getY() - 1, this.getZ()); // Paper - Optimize BlockPosition
    }

    @Override
    public BlockPosition down(int i) {
        return i == 0 ? this : new BlockPosition(this.getX(), this.getY() - i, this.getZ()); // Paper - Optimize BlockPosition
    }

    public BlockPosition north() {
        return new BlockPosition(this.getX(), this.getY(), this.getZ() - 1); // Paper - Optimize BlockPosition
    }

    public BlockPosition north(int i) {
        return i == 0 ? this : new BlockPosition(this.getX(), this.getY(), this.getZ() - i); // Paper - Optimize BlockPosition
    }

    public BlockPosition south() {
        return new BlockPosition(this.getX(), this.getY(), this.getZ() + 1); // Paper - Optimize BlockPosition
    }

    public BlockPosition south(int i) {
        return i == 0 ? this : new BlockPosition(this.getX(), this.getY(), this.getZ() + i); // Paper - Optimize BlockPosition
    }

    public BlockPosition west() {
        return new BlockPosition(this.getX() - 1, this.getY(), this.getZ()); // Paper - Optimize BlockPosition
    }

    public BlockPosition west(int i) {
        return i == 0 ? this : new BlockPosition(this.getX() - i, this.getY(), this.getZ()); // Paper - Optimize BlockPosition
    }

    public BlockPosition east() {
        return new BlockPosition(this.getX() + 1, this.getY(), this.getZ()); // Paper - Optimize BlockPosition
    }

    public BlockPosition east(int i) {
        return i == 0 ? this : new BlockPosition(this.getX() + i, this.getY(), this.getZ()); // Paper - Optimize BlockPosition
    }

    public BlockPosition shift(EnumDirection enumdirection) {
        // Paper Start - Optimize BlockPosition
        switch(enumdirection) {
            case UP:
                return new BlockPosition(this.getX(), this.getY() + 1, this.getZ());
            case DOWN:
                return new BlockPosition(this.getX(), this.getY() - 1, this.getZ());
            case NORTH:
                return new BlockPosition(this.getX(), this.getY(), this.getZ() - 1);
            case SOUTH:
                return new BlockPosition(this.getX(), this.getY(), this.getZ() + 1);
            case WEST:
                return new BlockPosition(this.getX() - 1, this.getY(), this.getZ());
            case EAST:
                return new BlockPosition(this.getX() + 1, this.getY(), this.getZ());
            default:
                return new BlockPosition(this.getX() + enumdirection.getAdjacentX(), this.getY() + enumdirection.getAdjacentY(), this.getZ() + enumdirection.getAdjacentZ());
        }
        // Paper End
    }

    @Override
    public BlockPosition shift(EnumDirection enumdirection, int i) {
        return i == 0 ? this : new BlockPosition(this.getX() + enumdirection.getAdjacentX() * i, this.getY() + enumdirection.getAdjacentY() * i, this.getZ() + enumdirection.getAdjacentZ() * i);
    }

    public BlockPosition a(EnumDirection.EnumAxis enumdirection_enumaxis, int i) {
        if (i == 0) {
            return this;
        } else {
            int j = enumdirection_enumaxis == EnumDirection.EnumAxis.X ? i : 0;
            int k = enumdirection_enumaxis == EnumDirection.EnumAxis.Y ? i : 0;
            int l = enumdirection_enumaxis == EnumDirection.EnumAxis.Z ? i : 0;

            return new BlockPosition(this.getX() + j, this.getY() + k, this.getZ() + l);
        }
    }

    public BlockPosition a(EnumBlockRotation enumblockrotation) {
        switch (enumblockrotation) {
            case NONE:
            default:
                return this;
            case CLOCKWISE_90:
                return new BlockPosition(-this.getZ(), this.getY(), this.getX());
            case CLOCKWISE_180:
                return new BlockPosition(-this.getX(), this.getY(), -this.getZ());
            case COUNTERCLOCKWISE_90:
                return new BlockPosition(this.getZ(), this.getY(), -this.getX());
        }
    }

    @Override
    public BlockPosition d(BaseBlockPosition baseblockposition) {
        return new BlockPosition(this.getY() * baseblockposition.getZ() - this.getZ() * baseblockposition.getY(), this.getZ() * baseblockposition.getX() - this.getX() * baseblockposition.getZ(), this.getX() * baseblockposition.getY() - this.getY() * baseblockposition.getX());
    }

    public BlockPosition immutableCopy() {
        return this;
    }

    public BlockPosition.MutableBlockPosition i() {
        return new BlockPosition.MutableBlockPosition(this.getX(), this.getY(), this.getZ());
    }

    public static Iterable<BlockPosition> a(Random random, int i, int j, int k, int l, int i1, int j1, int k1) {
        int l1 = i1 - j + 1;
        int i2 = j1 - k + 1;
        int j2 = k1 - l + 1;

        return () -> {
            return new AbstractIterator<BlockPosition>() {
                final BlockPosition.MutableBlockPosition a = new BlockPosition.MutableBlockPosition();
                int b = i;

                protected BlockPosition computeNext() {
                    if (this.b <= 0) {
                        return (BlockPosition) this.endOfData();
                    } else {
                        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = this.a.d(j + random.nextInt(l1), k + random.nextInt(i2), l + random.nextInt(j2));

                        --this.b;
                        return blockposition_mutableblockposition;
                    }
                }
            };
        };
    }

    public static Iterable<BlockPosition> a(BlockPosition blockposition, int p_i, int p_j, int p_k) { // Paper - decompile issues - variable name conflicts to inner class field refs
        int l_decompiled = p_i + p_j + p_k; // Paper - decompile issues
        int i1 = blockposition.getX();
        int j1 = blockposition.getY();
        int k1 = blockposition.getZ();

        return () -> {
            return new AbstractIterator<BlockPosition>() {
                private final BlockPosition.MutableBlockPosition h = new BlockPosition.MutableBlockPosition();
                private int i;
                private int j;
                private int k;
                private int l;
                private int m;
                private boolean n;

                protected BlockPosition computeNext() {
                    if (this.n) {
                        this.n = false;
                        this.h.q(k1 - (this.h.getZ() - k1));
                        return this.h;
                    } else {
                        BlockPosition.MutableBlockPosition blockposition_mutableblockposition;

                        for (blockposition_mutableblockposition = null; blockposition_mutableblockposition == null; ++this.m) {
                            if (this.m > this.k) {
                                ++this.l;
                                if (this.l > this.j) {
                                    ++this.i;
                                    if (this.i > l_decompiled) { // Paper - use proper l above (first line of this method)
                                        return (BlockPosition) this.endOfData();
                                    }

                                    this.j = Math.min(p_i, this.i); // Paper - decompile issues
                                    this.l = -this.j;
                                }

                                this.k = Math.min(p_j, this.i - Math.abs(this.l)); // Paper - decompile issues
                                this.m = -this.k;
                            }

                            int l1 = this.l;
                            int i2 = this.m;
                            int j2 = this.i - Math.abs(l1) - Math.abs(i2);

                            if (j2 <= p_k) { // Paper - decompile issues
                                this.n = j2 != 0;
                                blockposition_mutableblockposition = this.h.d(i1 + l1, j1 + i2, k1 + j2);
                            }
                        }

                        return blockposition_mutableblockposition;
                    }
                }
            };
        };
    }

    public static Optional<BlockPosition> a(BlockPosition blockposition, int i, int j, Predicate<BlockPosition> predicate) {
        return b(blockposition, i, j, i).filter(predicate).findFirst();
    }

    public static Stream<BlockPosition> b(BlockPosition blockposition, int i, int j, int k) {
        return StreamSupport.stream(a(blockposition, i, j, k).spliterator(), false);
    }

    public static Iterable<BlockPosition> a(BlockPosition blockposition, BlockPosition blockposition1) {
        return b(Math.min(blockposition.getX(), blockposition1.getX()), Math.min(blockposition.getY(), blockposition1.getY()), Math.min(blockposition.getZ(), blockposition1.getZ()), Math.max(blockposition.getX(), blockposition1.getX()), Math.max(blockposition.getY(), blockposition1.getY()), Math.max(blockposition.getZ(), blockposition1.getZ()));
    }

    public static Stream<BlockPosition> b(BlockPosition blockposition, BlockPosition blockposition1) {
        return StreamSupport.stream(a(blockposition, blockposition1).spliterator(), false);
    }

    public static Stream<BlockPosition> a(StructureBoundingBox structureboundingbox) {
        return a(Math.min(structureboundingbox.a, structureboundingbox.d), Math.min(structureboundingbox.b, structureboundingbox.e), Math.min(structureboundingbox.c, structureboundingbox.f), Math.max(structureboundingbox.a, structureboundingbox.d), Math.max(structureboundingbox.b, structureboundingbox.e), Math.max(structureboundingbox.c, structureboundingbox.f));
    }

    public static Stream<BlockPosition> a(AxisAlignedBB axisalignedbb) {
        return a(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY), MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY), MathHelper.floor(axisalignedbb.maxZ));
    }

    public static Stream<BlockPosition> a(int i, int j, int k, int l, int i1, int j1) {
        return StreamSupport.stream(b(i, j, k, l, i1, j1).spliterator(), false);
    }

    public static Iterable<BlockPosition> b(int i, int j, int k, int l, int i1, int j1) {
        int k1 = l - i + 1;
        int l1 = i1 - j + 1;
        int i2 = j1 - k + 1;
        int j2 = k1 * l1 * i2;

        return () -> {
            return new AbstractIterator<BlockPosition>() {
                private final BlockPosition.MutableBlockPosition g = new BlockPosition.MutableBlockPosition();
                private int h;

                protected BlockPosition computeNext() {
                    if (this.h == j2) {
                        return (BlockPosition) this.endOfData();
                    } else {
                        int k2 = this.h % k1;
                        int l2 = this.h / k1;
                        int i3 = l2 % l1;
                        int j3 = l2 / l1;

                        ++this.h;
                        return this.g.d(i + k2, j + i3, k + j3);
                    }
                }
            };
        };
    }

    public static Iterable<BlockPosition.MutableBlockPosition> a(BlockPosition blockposition, int I, EnumDirection enumdirection, EnumDirection enumdirection1) { // Paper - decompile fix
        Validate.validState(enumdirection.n() != enumdirection1.n(), "The two directions cannot be on the same axis", new Object[0]);
        return () -> {
            return new AbstractIterator<BlockPosition.MutableBlockPosition>() {
                private final EnumDirection[] e = new EnumDirection[]{enumdirection, enumdirection1, enumdirection.opposite(), enumdirection1.opposite()};
                private final BlockPosition.MutableBlockPosition f = blockposition.i().c(enumdirection1);
                private final int g = 4 * I;
                private int h = -1;
                private int i;
                private int j;
                private int k;
                private int l;
                private int m;

                {
                    this.k = this.f.getX();
                    this.l = this.f.getY();
                    this.m = this.f.getZ();
                }

                protected BlockPosition.MutableBlockPosition computeNext() {
                    this.f.d(this.k, this.l, this.m).c(this.e[(this.h + 4) % 4]);
                    this.k = this.f.getX();
                    this.l = this.f.getY();
                    this.m = this.f.getZ();
                    if (this.j >= this.i) {
                        if (this.h >= this.g) {
                            return (BlockPosition.MutableBlockPosition) this.endOfData();
                        }

                        ++this.h;
                        this.j = 0;
                        this.i = this.h / 2 + 1;
                    }

                    ++this.j;
                    return this.f;
                }
            };
        };
    }

    public static class MutableBlockPosition extends BlockPosition {

        public MutableBlockPosition() {
            this(0, 0, 0);
        }

        public MutableBlockPosition(int i, int j, int k) {
            super(i, j, k);
        }

        public MutableBlockPosition(double d0, double d1, double d2) {
            this(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2));
        }

        @Override
        public BlockPosition a(double d0, double d1, double d2) {
            return super.a(d0, d1, d2).immutableCopy();
        }

        @Override
        public BlockPosition b(int i, int j, int k) {
            return super.b(i, j, k).immutableCopy();
        }

        @Override
        public BlockPosition shift(EnumDirection enumdirection, int i) {
            return super.shift(enumdirection, i).immutableCopy();
        }

        @Override
        public BlockPosition a(EnumDirection.EnumAxis enumdirection_enumaxis, int i) {
            return super.a(enumdirection_enumaxis, i).immutableCopy();
        }

        @Override
        public BlockPosition a(EnumBlockRotation enumblockrotation) {
            return super.a(enumblockrotation).immutableCopy();
        }

        public final BlockPosition.MutableBlockPosition setValues(int i, int j, int k) { return d(i, j, k);} // Paper - OBFHELPER
        public BlockPosition.MutableBlockPosition d(int i, int j, int k) {
            this.o(i);
            this.p(j);
            this.q(k);
            return this;
        }

        public final BlockPosition.MutableBlockPosition setValues(double d0, double d1, double d2) { return c(d0, d1, d2);} // Paper - OBFHELPER
        public BlockPosition.MutableBlockPosition c(double d0, double d1, double d2) {
            return this.d(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2));
        }

        public final BlockPosition.MutableBlockPosition setValues(final BaseBlockPosition baseblockposition) { return this.g(baseblockposition); } // Paper - OBFHELPER
        public BlockPosition.MutableBlockPosition g(BaseBlockPosition baseblockposition) {
            return this.d(baseblockposition.getX(), baseblockposition.getY(), baseblockposition.getZ());
        }

        public BlockPosition.MutableBlockPosition g(long i) {
            return this.d(b(i), c(i), d(i));
        }

        public BlockPosition.MutableBlockPosition a(EnumAxisCycle enumaxiscycle, int i, int j, int k) {
            return this.d(enumaxiscycle.a(i, j, k, EnumDirection.EnumAxis.X), enumaxiscycle.a(i, j, k, EnumDirection.EnumAxis.Y), enumaxiscycle.a(i, j, k, EnumDirection.EnumAxis.Z));
        }

        public BlockPosition.MutableBlockPosition a(BaseBlockPosition baseblockposition, EnumDirection enumdirection) {
            return this.d(baseblockposition.getX() + enumdirection.getAdjacentX(), baseblockposition.getY() + enumdirection.getAdjacentY(), baseblockposition.getZ() + enumdirection.getAdjacentZ());
        }

        public BlockPosition.MutableBlockPosition a(BaseBlockPosition baseblockposition, int i, int j, int k) {
            return this.d(baseblockposition.getX() + i, baseblockposition.getY() + j, baseblockposition.getZ() + k);
        }

        public BlockPosition.MutableBlockPosition c(EnumDirection enumdirection) {
            return this.c(enumdirection, 1);
        }

        public BlockPosition.MutableBlockPosition c(EnumDirection enumdirection, int i) {
            return this.d(this.getX() + enumdirection.getAdjacentX() * i, this.getY() + enumdirection.getAdjacentY() * i, this.getZ() + enumdirection.getAdjacentZ() * i);
        }

        public BlockPosition.MutableBlockPosition e(int i, int j, int k) {
            return this.d(this.getX() + i, this.getY() + j, this.getZ() + k);
        }

        public BlockPosition.MutableBlockPosition h(BaseBlockPosition baseblockposition) {
            return this.d(this.getX() + baseblockposition.getX(), this.getY() + baseblockposition.getY(), this.getZ() + baseblockposition.getZ());
        }

        public BlockPosition.MutableBlockPosition a(EnumDirection.EnumAxis enumdirection_enumaxis, int i, int j) {
            switch (enumdirection_enumaxis) {
                case X:
                    return this.d(MathHelper.clamp(this.getX(), i, j), this.getY(), this.getZ());
                case Y:
                    return this.d(this.getX(), MathHelper.clamp(this.getY(), i, j), this.getZ());
                case Z:
                    return this.d(this.getX(), this.getY(), MathHelper.clamp(this.getZ(), i, j));
                default:
                    throw new IllegalStateException("Unable to clamp axis " + enumdirection_enumaxis);
            }
        }

        /* // Paper start - comment out useless overrides @Override
        @Override
        public void o(int i) {
            super.o(i);
        }

        @Override
        public void p(int i) {
            super.p(i);
        }

        public void q(int i) {
            super.q(i);
        }
        */ // Paper end

        @Override
        public BlockPosition immutableCopy() {
            return new BlockPosition(this);
        }
    }
}
