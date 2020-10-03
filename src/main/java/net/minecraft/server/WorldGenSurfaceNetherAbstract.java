package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.serialization.Codec;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Random;

public abstract class WorldGenSurfaceNetherAbstract extends WorldGenSurface<WorldGenSurfaceConfigurationBase> {

    private long a;
    private ImmutableMap<IBlockData, NoiseGeneratorOctaves> b = ImmutableMap.of();
    private ImmutableMap<IBlockData, NoiseGeneratorOctaves> c = ImmutableMap.of();
    private NoiseGeneratorOctaves d;

    public WorldGenSurfaceNetherAbstract(Codec<WorldGenSurfaceConfigurationBase> codec) {
        super(codec);
    }

    public void a(Random random, IChunkAccess ichunkaccess, BiomeBase biomebase, int i, int j, int k, double d0, IBlockData iblockdata, IBlockData iblockdata1, int l, long i1, WorldGenSurfaceConfigurationBase worldgensurfaceconfigurationbase) {
        int j1 = l + 1;
        int k1 = i & 15;
        int l1 = j & 15;
        int i2 = (int) (d0 / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        int j2 = (int) (d0 / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        double d1 = 0.03125D;
        boolean flag = this.d.a((double) i * 0.03125D, 109.0D, (double) j * 0.03125D) * 75.0D + random.nextDouble() > 0.0D;
        IBlockData iblockdata2 = (IBlockData) ((Entry) this.c.entrySet().stream().max(Comparator.comparing((entry) -> {
            return ((NoiseGeneratorOctaves) entry.getValue()).a((double) i, (double) l, (double) j);
        })).get()).getKey();
        IBlockData iblockdata3 = (IBlockData) ((Entry) this.b.entrySet().stream().max(Comparator.comparing((entry) -> {
            return ((NoiseGeneratorOctaves) entry.getValue()).a((double) i, (double) l, (double) j);
        })).get()).getKey();
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
        IBlockData iblockdata4 = ichunkaccess.getType(blockposition_mutableblockposition.d(k1, 128, l1));

        for (int k2 = 127; k2 >= 0; --k2) {
            blockposition_mutableblockposition.d(k1, k2, l1);
            IBlockData iblockdata5 = ichunkaccess.getType(blockposition_mutableblockposition);
            int l2;

            if (iblockdata4.a(iblockdata.getBlock()) && (iblockdata5.isAir() || iblockdata5 == iblockdata1)) {
                for (l2 = 0; l2 < i2; ++l2) {
                    blockposition_mutableblockposition.c(EnumDirection.UP);
                    if (!ichunkaccess.getType(blockposition_mutableblockposition).a(iblockdata.getBlock())) {
                        break;
                    }

                    ichunkaccess.setType(blockposition_mutableblockposition, iblockdata2, false);
                }

                blockposition_mutableblockposition.d(k1, k2, l1);
            }

            if ((iblockdata4.isAir() || iblockdata4 == iblockdata1) && iblockdata5.a(iblockdata.getBlock())) {
                for (l2 = 0; l2 < j2 && ichunkaccess.getType(blockposition_mutableblockposition).a(iblockdata.getBlock()); ++l2) {
                    if (flag && k2 >= j1 - 4 && k2 <= j1 + 1) {
                        ichunkaccess.setType(blockposition_mutableblockposition, this.c(), false);
                    } else {
                        ichunkaccess.setType(blockposition_mutableblockposition, iblockdata3, false);
                    }

                    blockposition_mutableblockposition.c(EnumDirection.DOWN);
                }
            }

            iblockdata4 = iblockdata5;
        }

    }

    @Override
    public void a(long i) {
        if (this.a != i || this.d == null || this.b.isEmpty() || this.c.isEmpty()) {
            this.b = a(this.a(), i);
            this.c = a(this.b(), i + (long) this.b.size());
            this.d = new NoiseGeneratorOctaves(new SeededRandom(i + (long) this.b.size() + (long) this.c.size()), ImmutableList.of(0));
        }

        this.a = i;
    }

    private static ImmutableMap<IBlockData, NoiseGeneratorOctaves> a(ImmutableList<IBlockData> immutablelist, long i) {
        Builder<IBlockData, NoiseGeneratorOctaves> builder = new Builder();

        for (UnmodifiableIterator unmodifiableiterator = immutablelist.iterator(); unmodifiableiterator.hasNext(); ++i) {
            IBlockData iblockdata = (IBlockData) unmodifiableiterator.next();

            builder.put(iblockdata, new NoiseGeneratorOctaves(new SeededRandom(i), ImmutableList.of(-4)));
        }

        return builder.build();
    }

    protected abstract ImmutableList<IBlockData> a();

    protected abstract ImmutableList<IBlockData> b();

    protected abstract IBlockData c();
}
