package net.minecraft.server;

import java.util.function.BiPredicate;
import java.util.function.Function;

public class DoubleBlockFinder {

    public static <S extends TileEntity> DoubleBlockFinder.Result<S> a(TileEntityTypes<S> tileentitytypes, Function<IBlockData, DoubleBlockFinder.BlockType> function, Function<IBlockData, EnumDirection> function1, BlockStateDirection blockstatedirection, IBlockData iblockdata, GeneratorAccess generatoraccess, BlockPosition blockposition, BiPredicate<GeneratorAccess, BlockPosition> bipredicate) {
        S s0 = tileentitytypes.a((IBlockAccess) generatoraccess, blockposition);

        if (s0 == null) {
            return DoubleBlockFinder.Combiner::b;
        } else if (bipredicate.test(generatoraccess, blockposition)) {
            return DoubleBlockFinder.Combiner::b;
        } else {
            DoubleBlockFinder.BlockType doubleblockfinder_blocktype = (DoubleBlockFinder.BlockType) function.apply(iblockdata);
            boolean flag = doubleblockfinder_blocktype == DoubleBlockFinder.BlockType.SINGLE;
            boolean flag1 = doubleblockfinder_blocktype == DoubleBlockFinder.BlockType.FIRST;

            if (flag) {
                return new DoubleBlockFinder.Result.Single<>(s0);
            } else {
                BlockPosition blockposition1 = blockposition.shift((EnumDirection) function1.apply(iblockdata));
                IBlockData iblockdata1 = generatoraccess.getType(blockposition1);

                if (iblockdata1.a(iblockdata.getBlock())) {
                    DoubleBlockFinder.BlockType doubleblockfinder_blocktype1 = (DoubleBlockFinder.BlockType) function.apply(iblockdata1);

                    if (doubleblockfinder_blocktype1 != DoubleBlockFinder.BlockType.SINGLE && doubleblockfinder_blocktype != doubleblockfinder_blocktype1 && iblockdata1.get(blockstatedirection) == iblockdata.get(blockstatedirection)) {
                        if (bipredicate.test(generatoraccess, blockposition1)) {
                            return DoubleBlockFinder.Combiner::b;
                        }

                        S s1 = tileentitytypes.a((IBlockAccess) generatoraccess, blockposition1);

                        if (s1 != null) {
                            S s2 = flag1 ? s0 : s1;
                            S s3 = flag1 ? s1 : s0;

                            return new DoubleBlockFinder.Result.Double<>(s2, s3);
                        }
                    }
                }

                return new DoubleBlockFinder.Result.Single<>(s0);
            }
        }
    }

    public interface Result<S> {

        <T> T apply(DoubleBlockFinder.Combiner<? super S, T> doubleblockfinder_combiner);

        public static final class Single<S> implements DoubleBlockFinder.Result<S> {

            private final S a;

            public Single(S s0) {
                this.a = s0;
            }

            @Override
            public <T> T apply(DoubleBlockFinder.Combiner<? super S, T> doubleblockfinder_combiner) {
                return doubleblockfinder_combiner.a(this.a);
            }
        }

        public static final class Double<S> implements DoubleBlockFinder.Result<S> {

            private final S a;
            private final S b;

            public Double(S s0, S s1) {
                this.a = s0;
                this.b = s1;
            }

            @Override
            public <T> T apply(DoubleBlockFinder.Combiner<? super S, T> doubleblockfinder_combiner) {
                return doubleblockfinder_combiner.a(this.a, this.b);
            }
        }
    }

    public interface Combiner<S, T> {

        T a(S s0, S s1);

        T a(S s0);

        T b();
    }

    public static enum BlockType {

        SINGLE, FIRST, SECOND;

        private BlockType() {}
    }
}
