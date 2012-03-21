package net.minecraft.server;

import java.util.List;
import java.util.Random;

public class ChunkProviderTheEnd implements IChunkProvider {

    private Random i;
    private NoiseGeneratorOctaves j;
    private NoiseGeneratorOctaves k;
    private NoiseGeneratorOctaves l;
    public NoiseGeneratorOctaves a;
    public NoiseGeneratorOctaves b;
    private World m;
    private double[] n;
    private BiomeBase[] o;
    double[] c;
    double[] d;
    double[] e;
    double[] f;
    double[] g;
    int[][] h = new int[32][32];

    public ChunkProviderTheEnd(World world, long i) {
        this.m = world;
        this.i = new Random(i);
        this.j = new NoiseGeneratorOctaves(this.i, 16);
        this.k = new NoiseGeneratorOctaves(this.i, 16);
        this.l = new NoiseGeneratorOctaves(this.i, 8);
        this.a = new NoiseGeneratorOctaves(this.i, 10);
        this.b = new NoiseGeneratorOctaves(this.i, 16);
    }

    public void a(int i, int j, byte[] abyte, BiomeBase[] abiomebase) {
        byte b0 = 2;
        int k = b0 + 1;
        byte b1 = 33;
        int l = b0 + 1;

        this.n = this.a(this.n, i * b0, 0, j * b0, k, b1, l);

        for (int i1 = 0; i1 < b0; ++i1) {
            for (int j1 = 0; j1 < b0; ++j1) {
                for (int k1 = 0; k1 < 32; ++k1) {
                    double d0 = 0.25D;
                    double d1 = this.n[((i1 + 0) * l + j1 + 0) * b1 + k1 + 0];
                    double d2 = this.n[((i1 + 0) * l + j1 + 1) * b1 + k1 + 0];
                    double d3 = this.n[((i1 + 1) * l + j1 + 0) * b1 + k1 + 0];
                    double d4 = this.n[((i1 + 1) * l + j1 + 1) * b1 + k1 + 0];
                    double d5 = (this.n[((i1 + 0) * l + j1 + 0) * b1 + k1 + 1] - d1) * d0;
                    double d6 = (this.n[((i1 + 0) * l + j1 + 1) * b1 + k1 + 1] - d2) * d0;
                    double d7 = (this.n[((i1 + 1) * l + j1 + 0) * b1 + k1 + 1] - d3) * d0;
                    double d8 = (this.n[((i1 + 1) * l + j1 + 1) * b1 + k1 + 1] - d4) * d0;

                    for (int l1 = 0; l1 < 4; ++l1) {
                        double d9 = 0.125D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int i2 = 0; i2 < 8; ++i2) {
                            int j2 = i2 + i1 * 8 << 11 | 0 + j1 * 8 << 7 | k1 * 4 + l1;
                            short short1 = 128;
                            double d14 = 0.125D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;

                            for (int k2 = 0; k2 < 8; ++k2) {
                                int l2 = 0;

                                if (d15 > 0.0D) {
                                    l2 = Block.WHITESTONE.id;
                                }

                                abyte[j2] = (byte) l2;
                                j2 += short1;
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    public void b(int i, int j, byte[] abyte, BiomeBase[] abiomebase) {
        for (int k = 0; k < 16; ++k) {
            for (int l = 0; l < 16; ++l) {
                byte b0 = 1;
                int i1 = -1;
                byte b1 = (byte) Block.WHITESTONE.id;
                byte b2 = (byte) Block.WHITESTONE.id;

                for (int j1 = 127; j1 >= 0; --j1) {
                    int k1 = (l * 16 + k) * 128 + j1;
                    byte b3 = abyte[k1];

                    if (b3 == 0) {
                        i1 = -1;
                    } else if (b3 == Block.STONE.id) {
                        if (i1 == -1) {
                            if (b0 <= 0) {
                                b1 = 0;
                                b2 = (byte) Block.WHITESTONE.id;
                            }

                            i1 = b0;
                            if (j1 >= 0) {
                                abyte[k1] = b1;
                            } else {
                                abyte[k1] = b2;
                            }
                        } else if (i1 > 0) {
                            --i1;
                            abyte[k1] = b2;
                        }
                    }
                }
            }
        }
    }

    public Chunk getChunkAt(int i, int j) {
        return this.getOrCreateChunk(i, j);
    }

    public Chunk getOrCreateChunk(int i, int j) {
        this.i.setSeed((long) i * 341873128712L + (long) j * 132897987541L);
        byte[] abyte = new byte['\u8000'];

        this.o = this.m.getWorldChunkManager().getBiomeBlock(this.o, i * 16, j * 16, 16, 16);
        this.a(i, j, abyte, this.o);
        this.b(i, j, abyte, this.o);
        Chunk chunk = new Chunk(this.m, abyte, i, j);

        // CraftBukkit start - prime biome data to prevent uninitialized values racing to client
        byte[] biomes = chunk.l();
        for(int idx = 0; idx < biomes.length; idx++) {
            biomes[idx] = (byte) this.o[idx].id;
        }
        // CraftBukkit end

        chunk.initLighting();
        return chunk;
    }

    private double[] a(double[] adouble, int i, int j, int k, int l, int i1, int j1) {
        if (adouble == null) {
            adouble = new double[l * i1 * j1];
        }

        double d0 = 684.412D;
        double d1 = 684.412D;

        this.f = this.a.a(this.f, i, k, l, j1, 1.121D, 1.121D, 0.5D);
        this.g = this.b.a(this.g, i, k, l, j1, 200.0D, 200.0D, 0.5D);
        d0 *= 2.0D;
        this.c = this.l.a(this.c, i, j, k, l, i1, j1, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
        this.d = this.j.a(this.d, i, j, k, l, i1, j1, d0, d1, d0);
        this.e = this.k.a(this.e, i, j, k, l, i1, j1, d0, d1, d0);
        int k1 = 0;
        int l1 = 0;

        for (int i2 = 0; i2 < l; ++i2) {
            for (int j2 = 0; j2 < j1; ++j2) {
                double d2 = (this.f[l1] + 256.0D) / 512.0D;

                if (d2 > 1.0D) {
                    d2 = 1.0D;
                }

                double d3 = this.g[l1] / 8000.0D;

                if (d3 < 0.0D) {
                    d3 = -d3 * 0.3D;
                }

                d3 = d3 * 3.0D - 2.0D;
                float f = (float) (i2 + i - 0) / 1.0F;
                float f1 = (float) (j2 + k - 0) / 1.0F;
                float f2 = 100.0F - MathHelper.c(f * f + f1 * f1) * 8.0F;

                if (f2 > 80.0F) {
                    f2 = 80.0F;
                }

                if (f2 < -100.0F) {
                    f2 = -100.0F;
                }

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                d3 /= 8.0D;
                d3 = 0.0D;
                if (d2 < 0.0D) {
                    d2 = 0.0D;
                }

                d2 += 0.5D;
                d3 = d3 * (double) i1 / 16.0D;
                ++l1;
                double d4 = (double) i1 / 2.0D;

                for (int k2 = 0; k2 < i1; ++k2) {
                    double d5 = 0.0D;
                    double d6 = ((double) k2 - d4) * 8.0D / d2;

                    if (d6 < 0.0D) {
                        d6 *= -1.0D;
                    }

                    double d7 = this.d[k1] / 512.0D;
                    double d8 = this.e[k1] / 512.0D;
                    double d9 = (this.c[k1] / 10.0D + 1.0D) / 2.0D;

                    if (d9 < 0.0D) {
                        d5 = d7;
                    } else if (d9 > 1.0D) {
                        d5 = d8;
                    } else {
                        d5 = d7 + (d8 - d7) * d9;
                    }

                    d5 -= 8.0D;
                    d5 += (double) f2;
                    byte b0 = 2;
                    double d10;

                    if (k2 > i1 / 2 - b0) {
                        d10 = (double) ((float) (k2 - (i1 / 2 - b0)) / 64.0F);
                        if (d10 < 0.0D) {
                            d10 = 0.0D;
                        }

                        if (d10 > 1.0D) {
                            d10 = 1.0D;
                        }

                        d5 = d5 * (1.0D - d10) + -3000.0D * d10;
                    }

                    b0 = 8;
                    if (k2 < b0) {
                        d10 = (double) ((float) (b0 - k2) / ((float) b0 - 1.0F));
                        d5 = d5 * (1.0D - d10) + -30.0D * d10;
                    }

                    adouble[k1] = d5;
                    ++k1;
                }
            }
        }

        return adouble;
    }

    public boolean isChunkLoaded(int i, int j) {
        return true;
    }

    public void getChunkAt(IChunkProvider ichunkprovider, int i, int j) {
        BlockSand.instaFall = true;
        int k = i * 16;
        int l = j * 16;
        BiomeBase biomebase = this.m.getBiome(k + 16, l + 16);

        biomebase.a(this.m, this.m.random, k, l);
        BlockSand.instaFall = false;
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
        return true;
    }

    public boolean unloadChunks() {
        return false;
    }

    public boolean canSave() {
        return true;
    }

    public List getMobsFor(EnumCreatureType enumcreaturetype, int i, int j, int k) {
        BiomeBase biomebase = this.m.getBiome(i, k);

        return biomebase == null ? null : biomebase.getMobs(enumcreaturetype);
    }

    public ChunkPosition findNearestMapFeature(World world, String s, int i, int j, int k) {
        return null;
    }
}
