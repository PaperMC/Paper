package net.minecraft.server;

import java.util.List;
import java.util.Random;

public class ChunkProviderGenerate implements IChunkProvider {

    private Random k;
    private NoiseGeneratorOctaves l;
    private NoiseGeneratorOctaves m;
    private NoiseGeneratorOctaves n;
    private NoiseGeneratorOctaves o;
    public NoiseGeneratorOctaves a;
    public NoiseGeneratorOctaves b;
    public NoiseGeneratorOctaves c;
    private World p;
    private final boolean q;
    private double[] r;
    private double[] s = new double[256];
    private WorldGenBase t = new WorldGenCaves();
    private WorldGenStronghold u = new WorldGenStronghold();
    private WorldGenVillage v = new WorldGenVillage(0);
    private WorldGenMineshaft w = new WorldGenMineshaft();
    private WorldGenBase x = new WorldGenCanyon();
    private BiomeBase[] y;
    double[] d;
    double[] e;
    double[] f;
    double[] g;
    double[] h;
    float[] i;
    int[][] j = new int[32][32];

    public ChunkProviderGenerate(World world, long i, boolean flag) {
        this.p = world;
        this.q = flag;
        this.k = new Random(i);
        this.l = new NoiseGeneratorOctaves(this.k, 16);
        this.m = new NoiseGeneratorOctaves(this.k, 16);
        this.n = new NoiseGeneratorOctaves(this.k, 8);
        this.o = new NoiseGeneratorOctaves(this.k, 4);
        this.a = new NoiseGeneratorOctaves(this.k, 10);
        this.b = new NoiseGeneratorOctaves(this.k, 16);
        this.c = new NoiseGeneratorOctaves(this.k, 8);
    }

    public void a(int i, int j, byte[] abyte) {
        byte b0 = 4;
        byte b1 = 16;
        byte b2 = 63;
        int k = b0 + 1;
        byte b3 = 17;
        int l = b0 + 1;

        this.y = this.p.getWorldChunkManager().getBiomes(this.y, i * 4 - 2, j * 4 - 2, k + 5, l + 5);
        this.r = this.a(this.r, i * b0, 0, j * b0, k, b3, l);

        for (int i1 = 0; i1 < b0; ++i1) {
            for (int j1 = 0; j1 < b0; ++j1) {
                for (int k1 = 0; k1 < b1; ++k1) {
                    double d0 = 0.125D;
                    double d1 = this.r[((i1 + 0) * l + j1 + 0) * b3 + k1 + 0];
                    double d2 = this.r[((i1 + 0) * l + j1 + 1) * b3 + k1 + 0];
                    double d3 = this.r[((i1 + 1) * l + j1 + 0) * b3 + k1 + 0];
                    double d4 = this.r[((i1 + 1) * l + j1 + 1) * b3 + k1 + 0];
                    double d5 = (this.r[((i1 + 0) * l + j1 + 0) * b3 + k1 + 1] - d1) * d0;
                    double d6 = (this.r[((i1 + 0) * l + j1 + 1) * b3 + k1 + 1] - d2) * d0;
                    double d7 = (this.r[((i1 + 1) * l + j1 + 0) * b3 + k1 + 1] - d3) * d0;
                    double d8 = (this.r[((i1 + 1) * l + j1 + 1) * b3 + k1 + 1] - d4) * d0;

                    for (int l1 = 0; l1 < 8; ++l1) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int i2 = 0; i2 < 4; ++i2) {
                            int j2 = i2 + i1 * 4 << 11 | 0 + j1 * 4 << 7 | k1 * 8 + l1;
                            short short1 = 128;

                            j2 -= short1;
                            double d14 = 0.25D;
                            double d15 = (d11 - d10) * d14;
                            double d16 = d10 - d15;

                            for (int k2 = 0; k2 < 4; ++k2) {
                                if ((d16 += d15) > 0.0D) {
                                    abyte[j2 += short1] = (byte) Block.STONE.id;
                                } else if (k1 * 8 + l1 < b2) {
                                    abyte[j2 += short1] = (byte) Block.STATIONARY_WATER.id;
                                } else {
                                    abyte[j2 += short1] = 0;
                                }
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

    public void a(int i, int j, byte[] abyte, BiomeBase[] abiomebase) {
        byte b0 = 63;
        double d0 = 0.03125D;

        this.s = this.o.a(this.s, i * 16, j * 16, 0, 16, 16, 1, d0 * 2.0D, d0 * 2.0D, d0 * 2.0D);

        for (int k = 0; k < 16; ++k) {
            for (int l = 0; l < 16; ++l) {
                BiomeBase biomebase = abiomebase[l + k * 16];
                float f = biomebase.h();
                int i1 = (int) (this.s[k + l * 16] / 3.0D + 3.0D + this.k.nextDouble() * 0.25D);
                int j1 = -1;
                byte b1 = biomebase.A;
                byte b2 = biomebase.B;

                for (int k1 = 127; k1 >= 0; --k1) {
                    int l1 = (l * 16 + k) * 128 + k1;

                    if (k1 <= 0 + this.k.nextInt(5)) {
                        abyte[l1] = (byte) Block.BEDROCK.id;
                    } else {
                        byte b3 = abyte[l1];

                        if (b3 == 0) {
                            j1 = -1;
                        } else if (b3 == Block.STONE.id) {
                            if (j1 == -1) {
                                if (i1 <= 0) {
                                    b1 = 0;
                                    b2 = (byte) Block.STONE.id;
                                } else if (k1 >= b0 - 4 && k1 <= b0 + 1) {
                                    b1 = biomebase.A;
                                    b2 = biomebase.B;
                                }

                                if (k1 < b0 && b1 == 0) {
                                    if (f < 0.15F) {
                                        b1 = (byte) Block.ICE.id;
                                    } else {
                                        b1 = (byte) Block.STATIONARY_WATER.id;
                                    }
                                }

                                j1 = i1;
                                if (k1 >= b0 - 1) {
                                    abyte[l1] = b1;
                                } else {
                                    abyte[l1] = b2;
                                }
                            } else if (j1 > 0) {
                                --j1;
                                abyte[l1] = b2;
                                if (j1 == 0 && b2 == Block.SAND.id) {
                                    j1 = this.k.nextInt(4);
                                    b2 = (byte) Block.SANDSTONE.id;
                                }
                            }
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
        this.k.setSeed((long) i * 341873128712L + (long) j * 132897987541L);
        byte[] abyte = new byte['\u8000'];

        this.a(i, j, abyte);
        this.y = this.p.getWorldChunkManager().getBiomeBlock(this.y, i * 16, j * 16, 16, 16);
        this.a(i, j, abyte, this.y);
        this.t.a(this, this.p, i, j, abyte);
        this.x.a(this, this.p, i, j, abyte);
        if (this.q) {
            this.w.a(this, this.p, i, j, abyte);
            this.v.a(this, this.p, i, j, abyte);
            this.u.a(this, this.p, i, j, abyte);
        }

        Chunk chunk = new Chunk(this.p, abyte, i, j);
        // CraftBukkit start - prime biome data to prevent uninitialized values racing to client
        byte[] biomes = chunk.l();
        for(int idx = 0; idx < biomes.length; idx++) {
            biomes[idx] = (byte) this.y[idx].id;
        }
        // CraftBukkit end

        chunk.initLighting();
        return chunk;
    }

    private double[] a(double[] adouble, int i, int j, int k, int l, int i1, int j1) {
        if (adouble == null) {
            adouble = new double[l * i1 * j1];
        }

        if (this.i == null) {
            this.i = new float[25];

            for (int k1 = -2; k1 <= 2; ++k1) {
                for (int l1 = -2; l1 <= 2; ++l1) {
                    float f = 10.0F / MathHelper.c((float) (k1 * k1 + l1 * l1) + 0.2F);

                    this.i[k1 + 2 + (l1 + 2) * 5] = f;
                }
            }
        }

        double d0 = 684.412D;
        double d1 = 684.412D;

        this.g = this.a.a(this.g, i, k, l, j1, 1.121D, 1.121D, 0.5D);
        this.h = this.b.a(this.h, i, k, l, j1, 200.0D, 200.0D, 0.5D);
        this.d = this.n.a(this.d, i, j, k, l, i1, j1, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
        this.e = this.l.a(this.e, i, j, k, l, i1, j1, d0, d1, d0);
        this.f = this.m.a(this.f, i, j, k, l, i1, j1, d0, d1, d0);
        boolean flag = false;
        boolean flag1 = false;
        int i2 = 0;
        int j2 = 0;

        for (int k2 = 0; k2 < l; ++k2) {
            for (int l2 = 0; l2 < j1; ++l2) {
                float f1 = 0.0F;
                float f2 = 0.0F;
                float f3 = 0.0F;
                byte b0 = 2;
                BiomeBase biomebase = this.y[k2 + 2 + (l2 + 2) * (l + 5)];

                for (int i3 = -b0; i3 <= b0; ++i3) {
                    for (int j3 = -b0; j3 <= b0; ++j3) {
                        BiomeBase biomebase1 = this.y[k2 + i3 + 2 + (l2 + j3 + 2) * (l + 5)];
                        float f4 = this.i[i3 + 2 + (j3 + 2) * 5] / (biomebase1.D + 2.0F);

                        if (biomebase1.D > biomebase.D) {
                            f4 /= 2.0F;
                        }

                        f1 += biomebase1.E * f4;
                        f2 += biomebase1.D * f4;
                        f3 += f4;
                    }
                }

                f1 /= f3;
                f2 /= f3;
                f1 = f1 * 0.9F + 0.1F;
                f2 = (f2 * 4.0F - 1.0F) / 8.0F;
                double d2 = this.h[j2] / 8000.0D;

                if (d2 < 0.0D) {
                    d2 = -d2 * 0.3D;
                }

                d2 = d2 * 3.0D - 2.0D;
                if (d2 < 0.0D) {
                    d2 /= 2.0D;
                    if (d2 < -1.0D) {
                        d2 = -1.0D;
                    }

                    d2 /= 1.4D;
                    d2 /= 2.0D;
                } else {
                    if (d2 > 1.0D) {
                        d2 = 1.0D;
                    }

                    d2 /= 8.0D;
                }

                ++j2;

                for (int k3 = 0; k3 < i1; ++k3) {
                    double d3 = (double) f2;
                    double d4 = (double) f1;

                    d3 += d2 * 0.2D;
                    d3 = d3 * (double) i1 / 16.0D;
                    double d5 = (double) i1 / 2.0D + d3 * 4.0D;
                    double d6 = 0.0D;
                    double d7 = ((double) k3 - d5) * 12.0D * 128.0D / 128.0D / d4;

                    if (d7 < 0.0D) {
                        d7 *= 4.0D;
                    }

                    double d8 = this.e[i2] / 512.0D;
                    double d9 = this.f[i2] / 512.0D;
                    double d10 = (this.d[i2] / 10.0D + 1.0D) / 2.0D;

                    if (d10 < 0.0D) {
                        d6 = d8;
                    } else if (d10 > 1.0D) {
                        d6 = d9;
                    } else {
                        d6 = d8 + (d9 - d8) * d10;
                    }

                    d6 -= d7;
                    if (k3 > i1 - 4) {
                        double d11 = (double) ((float) (k3 - (i1 - 4)) / 3.0F);

                        d6 = d6 * (1.0D - d11) + -10.0D * d11;
                    }

                    adouble[i2] = d6;
                    ++i2;
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
        BiomeBase biomebase = this.p.getBiome(k + 16, l + 16);

        this.k.setSeed(this.p.getSeed());
        long i1 = this.k.nextLong() / 2L * 2L + 1L;
        long j1 = this.k.nextLong() / 2L * 2L + 1L;

        this.k.setSeed((long) i * i1 + (long) j * j1 ^ this.p.getSeed());
        boolean flag = false;

        if (this.q) {
            this.w.a(this.p, this.k, i, j);
            flag = this.v.a(this.p, this.k, i, j);
            this.u.a(this.p, this.k, i, j);
        }

        int k1;
        int l1;
        int i2;

        if (!flag && this.k.nextInt(4) == 0) {
            k1 = k + this.k.nextInt(16) + 8;
            l1 = this.k.nextInt(128);
            i2 = l + this.k.nextInt(16) + 8;
            (new WorldGenLakes(Block.STATIONARY_WATER.id)).a(this.p, this.k, k1, l1, i2);
        }

        if (!flag && this.k.nextInt(8) == 0) {
            k1 = k + this.k.nextInt(16) + 8;
            l1 = this.k.nextInt(this.k.nextInt(120) + 8);
            i2 = l + this.k.nextInt(16) + 8;
            if (l1 < 63 || this.k.nextInt(10) == 0) {
                (new WorldGenLakes(Block.STATIONARY_LAVA.id)).a(this.p, this.k, k1, l1, i2);
            }
        }

        for (k1 = 0; k1 < 8; ++k1) {
            l1 = k + this.k.nextInt(16) + 8;
            i2 = this.k.nextInt(128);
            int j2 = l + this.k.nextInt(16) + 8;

            if ((new WorldGenDungeons()).a(this.p, this.k, l1, i2, j2)) {
                ;
            }
        }

        biomebase.a(this.p, this.k, k, l);
        SpawnerCreature.a(this.p, biomebase, k + 8, l + 8, 16, 16, this.k);
        k += 8;
        l += 8;

        for (k1 = 0; k1 < 16; ++k1) {
            for (l1 = 0; l1 < 16; ++l1) {
                i2 = this.p.f(k + k1, l + l1);
                if (this.p.s(k1 + k, i2 - 1, l1 + l)) {
                    this.p.setTypeId(k1 + k, i2 - 1, l1 + l, Block.ICE.id);
                }

                if (this.p.u(k1 + k, i2, l1 + l)) {
                    this.p.setTypeId(k1 + k, i2, l1 + l, Block.SNOW.id);
                }
            }
        }

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
        BiomeBase biomebase = this.p.getBiome(i, k);

        return biomebase == null ? null : biomebase.getMobs(enumcreaturetype);
    }

    public ChunkPosition findNearestMapFeature(World world, String s, int i, int j, int k) {
        return "Stronghold".equals(s) && this.u != null ? this.u.getNearestGeneratedFeature(world, i, j, k) : null;
    }
}
