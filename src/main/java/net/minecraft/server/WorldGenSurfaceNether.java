package net.minecraft.server;

import com.mojang.serialization.Codec;
import java.util.Random;
import java.util.stream.IntStream;

public class WorldGenSurfaceNether extends WorldGenSurface<WorldGenSurfaceConfigurationBase> {

    private static final IBlockData c = Blocks.CAVE_AIR.getBlockData();
    private static final IBlockData d = Blocks.GRAVEL.getBlockData();
    private static final IBlockData e = Blocks.SOUL_SAND.getBlockData();
    protected long a;
    protected NoiseGeneratorOctaves b;

    public WorldGenSurfaceNether(Codec<WorldGenSurfaceConfigurationBase> codec) {
        super(codec);
    }

    public void a(Random random, IChunkAccess ichunkaccess, BiomeBase biomebase, int i, int j, int k, double d0, IBlockData iblockdata, IBlockData iblockdata1, int l, long i1, WorldGenSurfaceConfigurationBase worldgensurfaceconfigurationbase) {
        int j1 = l;
        int k1 = i & 15;
        int l1 = j & 15;
        double d1 = 0.03125D;
        boolean flag = this.b.a((double) i * 0.03125D, (double) j * 0.03125D, 0.0D) * 75.0D + random.nextDouble() > 0.0D;
        boolean flag1 = this.b.a((double) i * 0.03125D, 109.0D, (double) j * 0.03125D) * 75.0D + random.nextDouble() > 0.0D;
        int i2 = (int) (d0 / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
        int j2 = -1;
        IBlockData iblockdata2 = worldgensurfaceconfigurationbase.a();
        IBlockData iblockdata3 = worldgensurfaceconfigurationbase.b();

        for (int k2 = 127; k2 >= 0; --k2) {
            blockposition_mutableblockposition.d(k1, k2, l1);
            IBlockData iblockdata4 = ichunkaccess.getType(blockposition_mutableblockposition);

            if (iblockdata4.isAir()) {
                j2 = -1;
            } else if (iblockdata4.a(iblockdata.getBlock())) {
                if (j2 == -1) {
                    boolean flag2 = false;

                    if (i2 <= 0) {
                        flag2 = true;
                        iblockdata3 = worldgensurfaceconfigurationbase.b();
                    } else if (k2 >= j1 - 4 && k2 <= j1 + 1) {
                        iblockdata2 = worldgensurfaceconfigurationbase.a();
                        iblockdata3 = worldgensurfaceconfigurationbase.b();
                        if (flag1) {
                            iblockdata2 = WorldGenSurfaceNether.d;
                            iblockdata3 = worldgensurfaceconfigurationbase.b();
                        }

                        if (flag) {
                            iblockdata2 = WorldGenSurfaceNether.e;
                            iblockdata3 = WorldGenSurfaceNether.e;
                        }
                    }

                    if (k2 < j1 && flag2) {
                        iblockdata2 = iblockdata1;
                    }

                    j2 = i2;
                    if (k2 >= j1 - 1) {
                        ichunkaccess.setType(blockposition_mutableblockposition, iblockdata2, false);
                    } else {
                        ichunkaccess.setType(blockposition_mutableblockposition, iblockdata3, false);
                    }
                } else if (j2 > 0) {
                    --j2;
                    ichunkaccess.setType(blockposition_mutableblockposition, iblockdata3, false);
                }
            }
        }

    }

    @Override
    public void a(long i) {
        if (this.a != i || this.b == null) {
            this.b = new NoiseGeneratorOctaves(new SeededRandom(i), IntStream.rangeClosed(-3, 0));
        }

        this.a = i;
    }
}
