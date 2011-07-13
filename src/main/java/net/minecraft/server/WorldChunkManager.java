package net.minecraft.server;

import java.util.Random;

public class WorldChunkManager {

    private NoiseGeneratorOctaves2 e;
    private NoiseGeneratorOctaves2 f;
    private NoiseGeneratorOctaves2 g;
    public double[] temperature;
    public double[] rain;
    public double[] c;
    public BiomeBase[] d;

    protected WorldChunkManager() {}

    public WorldChunkManager(World world) {
        this.e = new NoiseGeneratorOctaves2(new Random(world.getSeed() * 9871L), 4);
        this.f = new NoiseGeneratorOctaves2(new Random(world.getSeed() * 39811L), 4);
        this.g = new NoiseGeneratorOctaves2(new Random(world.getSeed() * 543321L), 2);
    }

    public BiomeBase a(ChunkCoordIntPair chunkcoordintpair) {
        return this.getBiome(chunkcoordintpair.x << 4, chunkcoordintpair.z << 4);
    }

    public BiomeBase getBiome(int i, int j) {
        return this.getBiomeData(i, j, 1, 1)[0];
    }

    public BiomeBase[] getBiomeData(int i, int j, int k, int l) {
        this.d = this.a(this.d, i, j, k, l);
        return this.d;
    }

    public double[] a(double[] adouble, int i, int j, int k, int l) {
        if (adouble == null || adouble.length < k * l) {
            adouble = new double[k * l];
        }

        adouble = this.e.a(adouble, (double) i, (double) j, k, l, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        this.c = this.g.a(this.c, (double) i, (double) j, k, l, 0.25D, 0.25D, 0.5882352941176471D);
        int i1 = 0;

        for (int j1 = 0; j1 < k; ++j1) {
            for (int k1 = 0; k1 < l; ++k1) {
                double d0 = this.c[i1] * 1.1D + 0.5D;
                double d1 = 0.01D;
                double d2 = 1.0D - d1;
                double d3 = (adouble[i1] * 0.15D + 0.7D) * d2 + d0 * d1;

                d3 = 1.0D - (1.0D - d3) * (1.0D - d3);
                if (d3 < 0.0D) {
                    d3 = 0.0D;
                }

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                adouble[i1] = d3;
                ++i1;
            }
        }

        return adouble;
    }

    public BiomeBase[] a(BiomeBase[] abiomebase, int i, int j, int k, int l) {
        if (abiomebase == null || abiomebase.length < k * l) {
            abiomebase = new BiomeBase[k * l];
        }

        this.temperature = this.e.a(this.temperature, (double) i, (double) j, k, k, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        this.rain = this.f.a(this.rain, (double) i, (double) j, k, k, 0.05000000074505806D, 0.05000000074505806D, 0.3333333333333333D);
        this.c = this.g.a(this.c, (double) i, (double) j, k, k, 0.25D, 0.25D, 0.5882352941176471D);
        int i1 = 0;

        for (int j1 = 0; j1 < k; ++j1) {
            for (int k1 = 0; k1 < l; ++k1) {
                double d0 = this.c[i1] * 1.1D + 0.5D;
                double d1 = 0.01D;
                double d2 = 1.0D - d1;
                double d3 = (this.temperature[i1] * 0.15D + 0.7D) * d2 + d0 * d1;

                d1 = 0.0020D;
                d2 = 1.0D - d1;
                double d4 = (this.rain[i1] * 0.15D + 0.5D) * d2 + d0 * d1;

                d3 = 1.0D - (1.0D - d3) * (1.0D - d3);
                if (d3 < 0.0D) {
                    d3 = 0.0D;
                }

                if (d4 < 0.0D) {
                    d4 = 0.0D;
                }

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                if (d4 > 1.0D) {
                    d4 = 1.0D;
                }

                this.temperature[i1] = d3;
                this.rain[i1] = d4;
                abiomebase[i1++] = BiomeBase.a(d3, d4);
            }
        }

        return abiomebase;
    }

    // CraftBukkit start
    public double getHumidity(int x, int z) {
        return this.f.a(this.rain, (double)x, (double)z, 1, 1, 0.05000000074505806D, 0.05000000074505806D, 0.3333333333333333D)[0];
    }
    // CraftBukkit end
}
