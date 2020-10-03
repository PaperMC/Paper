package net.minecraft.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import javax.annotation.Nullable;

public final class ChunkGeneratorAbstract extends ChunkGenerator {

    public static final Codec<ChunkGeneratorAbstract> d = RecordCodecBuilder.create((instance) -> {
        return instance.group(WorldChunkManager.a.fieldOf("biome_source").forGetter((chunkgeneratorabstract) -> {
            return chunkgeneratorabstract.b;
        }), Codec.LONG.fieldOf("seed").stable().forGetter((chunkgeneratorabstract) -> {
            return chunkgeneratorabstract.w;
        }), GeneratorSettingBase.b.fieldOf("settings").forGetter((chunkgeneratorabstract) -> {
            return chunkgeneratorabstract.h;
        })).apply(instance, instance.stable(ChunkGeneratorAbstract::new));
    });
    private static final float[] i = (float[]) SystemUtils.a((Object) (new float[13824]), (afloat) -> {
        for (int i = 0; i < 24; ++i) {
            for (int j = 0; j < 24; ++j) {
                for (int k = 0; k < 24; ++k) {
                    afloat[i * 24 * 24 + j * 24 + k] = (float) b(j - 12, k - 12, i - 12);
                }
            }
        }

    });
    private static final float[] j = (float[]) SystemUtils.a((Object) (new float[25]), (afloat) -> {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                float f = 10.0F / MathHelper.c((float) (i * i + j * j) + 0.2F);

                afloat[i + 2 + (j + 2) * 5] = f;
            }
        }

    });
    private static final IBlockData k = Blocks.AIR.getBlockData();
    private final int l;
    private final int m;
    private final int n;
    private final int o;
    private final int p;
    protected final SeededRandom e;
    private final NoiseGeneratorOctaves q;
    private final NoiseGeneratorOctaves r;
    private final NoiseGeneratorOctaves s;
    private final NoiseGenerator t;
    private final NoiseGeneratorOctaves u;
    @Nullable
    private final NoiseGenerator3Handler v;
    protected final IBlockData f;
    protected final IBlockData g;
    private final long w;
    protected final Supplier<GeneratorSettingBase> h;
    private final int x;

    public ChunkGeneratorAbstract(WorldChunkManager worldchunkmanager, long i, Supplier<GeneratorSettingBase> supplier) {
        this(worldchunkmanager, worldchunkmanager, i, supplier);
    }

    private ChunkGeneratorAbstract(WorldChunkManager worldchunkmanager, WorldChunkManager worldchunkmanager1, long i, Supplier<GeneratorSettingBase> supplier) {
        super(worldchunkmanager, worldchunkmanager1, ((GeneratorSettingBase) supplier.get()).a(), i);
        this.w = i;
        GeneratorSettingBase generatorsettingbase = (GeneratorSettingBase) supplier.get();

        this.h = supplier;
        NoiseSettings noisesettings = generatorsettingbase.b();

        this.x = noisesettings.a();
        this.l = noisesettings.f() * 4;
        this.m = noisesettings.e() * 4;
        this.f = generatorsettingbase.c();
        this.g = generatorsettingbase.d();
        this.n = 16 / this.m;
        this.o = noisesettings.a() / this.l;
        this.p = 16 / this.m;
        this.e = new SeededRandom(i);
        this.q = new NoiseGeneratorOctaves(this.e, IntStream.rangeClosed(-15, 0));
        this.r = new NoiseGeneratorOctaves(this.e, IntStream.rangeClosed(-15, 0));
        this.s = new NoiseGeneratorOctaves(this.e, IntStream.rangeClosed(-7, 0));
        this.t = (NoiseGenerator) (noisesettings.i() ? new NoiseGenerator3(this.e, IntStream.rangeClosed(-3, 0)) : new NoiseGeneratorOctaves(this.e, IntStream.rangeClosed(-3, 0)));
        this.e.a(2620);
        this.u = new NoiseGeneratorOctaves(this.e, IntStream.rangeClosed(-15, 0));
        if (noisesettings.k()) {
            SeededRandom seededrandom = new SeededRandom(i);

            seededrandom.a(17292);
            this.v = new NoiseGenerator3Handler(seededrandom);
        } else {
            this.v = null;
        }

    }

    @Override
    protected Codec<? extends ChunkGenerator> a() {
        return ChunkGeneratorAbstract.d;
    }

    public boolean a(long i, ResourceKey<GeneratorSettingBase> resourcekey) {
        return this.w == i && ((GeneratorSettingBase) this.h.get()).a(resourcekey);
    }

    private double a(int i, int j, int k, double d0, double d1, double d2, double d3) {
        double d4 = 0.0D;
        double d5 = 0.0D;
        double d6 = 0.0D;
        boolean flag = true;
        double d7 = 1.0D;

        for (int l = 0; l < 16; ++l) {
            double d8 = NoiseGeneratorOctaves.a((double) i * d0 * d7);
            double d9 = NoiseGeneratorOctaves.a((double) j * d1 * d7);
            double d10 = NoiseGeneratorOctaves.a((double) k * d0 * d7);
            double d11 = d1 * d7;
            NoiseGeneratorPerlin noisegeneratorperlin = this.q.a(l);

            if (noisegeneratorperlin != null) {
                d4 += noisegeneratorperlin.a(d8, d9, d10, d11, (double) j * d11) / d7;
            }

            NoiseGeneratorPerlin noisegeneratorperlin1 = this.r.a(l);

            if (noisegeneratorperlin1 != null) {
                d5 += noisegeneratorperlin1.a(d8, d9, d10, d11, (double) j * d11) / d7;
            }

            if (l < 8) {
                NoiseGeneratorPerlin noisegeneratorperlin2 = this.s.a(l);

                if (noisegeneratorperlin2 != null) {
                    d6 += noisegeneratorperlin2.a(NoiseGeneratorOctaves.a((double) i * d2 * d7), NoiseGeneratorOctaves.a((double) j * d3 * d7), NoiseGeneratorOctaves.a((double) k * d2 * d7), d3 * d7, (double) j * d3 * d7) / d7;
                }
            }

            d7 /= 2.0D;
        }

        return MathHelper.b(d4 / 512.0D, d5 / 512.0D, (d6 / 10.0D + 1.0D) / 2.0D);
    }

    private double[] b(int i, int j) {
        double[] adouble = new double[this.o + 1];

        this.a(adouble, i, j);
        return adouble;
    }

    private void a(double[] adouble, int i, int j) {
        NoiseSettings noisesettings = ((GeneratorSettingBase) this.h.get()).b();
        double d0;
        double d1;
        double d2;
        double d3;

        if (this.v != null) {
            d0 = (double) (WorldChunkManagerTheEnd.a(this.v, i, j) - 8.0F);
            if (d0 > 0.0D) {
                d1 = 0.25D;
            } else {
                d1 = 1.0D;
            }
        } else {
            float f = 0.0F;
            float f1 = 0.0F;
            float f2 = 0.0F;
            boolean flag = true;
            int k = this.getSeaLevel();
            float f3 = this.b.getBiome(i, k, j).h();

            for (int l = -2; l <= 2; ++l) {
                for (int i1 = -2; i1 <= 2; ++i1) {
                    BiomeBase biomebase = this.b.getBiome(i + l, k, j + i1);
                    float f4 = biomebase.h();
                    float f5 = biomebase.j();
                    float f6;
                    float f7;

                    if (noisesettings.l() && f4 > 0.0F) {
                        f6 = 1.0F + f4 * 2.0F;
                        f7 = 1.0F + f5 * 4.0F;
                    } else {
                        f6 = f4;
                        f7 = f5;
                    }

                    float f8 = f4 > f3 ? 0.5F : 1.0F;
                    float f9 = f8 * ChunkGeneratorAbstract.j[l + 2 + (i1 + 2) * 5] / (f6 + 2.0F);

                    f += f7 * f9;
                    f1 += f6 * f9;
                    f2 += f9;
                }
            }

            float f10 = f1 / f2;
            float f11 = f / f2;

            d2 = (double) (f10 * 0.5F - 0.125F);
            d3 = (double) (f11 * 0.9F + 0.1F);
            d0 = d2 * 0.265625D;
            d1 = 96.0D / d3;
        }

        double d4 = 684.412D * noisesettings.b().a();
        double d5 = 684.412D * noisesettings.b().b();
        double d6 = d4 / noisesettings.b().c();
        double d7 = d5 / noisesettings.b().d();

        d2 = (double) noisesettings.c().a();
        d3 = (double) noisesettings.c().b();
        double d8 = (double) noisesettings.c().c();
        double d9 = (double) noisesettings.d().a();
        double d10 = (double) noisesettings.d().b();
        double d11 = (double) noisesettings.d().c();
        double d12 = noisesettings.j() ? this.c(i, j) : 0.0D;
        double d13 = noisesettings.g();
        double d14 = noisesettings.h();

        for (int j1 = 0; j1 <= this.o; ++j1) {
            double d15 = this.a(i, j1, j, d4, d5, d6, d7);
            double d16 = 1.0D - (double) j1 * 2.0D / (double) this.o + d12;
            double d17 = d16 * d13 + d14;
            double d18 = (d17 + d0) * d1;

            if (d18 > 0.0D) {
                d15 += d18 * 4.0D;
            } else {
                d15 += d18;
            }

            double d19;

            if (d3 > 0.0D) {
                d19 = ((double) (this.o - j1) - d8) / d3;
                d15 = MathHelper.b(d2, d15, d19);
            }

            if (d10 > 0.0D) {
                d19 = ((double) j1 - d11) / d10;
                d15 = MathHelper.b(d9, d15, d19);
            }

            adouble[j1] = d15;
        }

    }

    private double c(int i, int j) {
        double d0 = this.u.a((double) (i * 200), 10.0D, (double) (j * 200), 1.0D, 0.0D, true);
        double d1;

        if (d0 < 0.0D) {
            d1 = -d0 * 0.3D;
        } else {
            d1 = d0;
        }

        double d2 = d1 * 24.575625D - 2.0D;

        return d2 < 0.0D ? d2 * 0.009486607142857142D : Math.min(d2, 1.0D) * 0.006640625D;
    }

    @Override
    public int getBaseHeight(int i, int j, HeightMap.Type heightmap_type) {
        return this.a(i, j, (IBlockData[]) null, heightmap_type.e());
    }

    @Override
    public IBlockAccess a(int i, int j) {
        IBlockData[] aiblockdata = new IBlockData[this.o * this.l];

        this.a(i, j, aiblockdata, (Predicate) null);
        return new BlockColumn(aiblockdata);
    }

    private int a(int i, int j, @Nullable IBlockData[] aiblockdata, @Nullable Predicate<IBlockData> predicate) {
        int k = Math.floorDiv(i, this.m);
        int l = Math.floorDiv(j, this.m);
        int i1 = Math.floorMod(i, this.m);
        int j1 = Math.floorMod(j, this.m);
        double d0 = (double) i1 / (double) this.m;
        double d1 = (double) j1 / (double) this.m;
        double[][] adouble = new double[][]{this.b(k, l), this.b(k, l + 1), this.b(k + 1, l), this.b(k + 1, l + 1)};

        for (int k1 = this.o - 1; k1 >= 0; --k1) {
            double d2 = adouble[0][k1];
            double d3 = adouble[1][k1];
            double d4 = adouble[2][k1];
            double d5 = adouble[3][k1];
            double d6 = adouble[0][k1 + 1];
            double d7 = adouble[1][k1 + 1];
            double d8 = adouble[2][k1 + 1];
            double d9 = adouble[3][k1 + 1];

            for (int l1 = this.l - 1; l1 >= 0; --l1) {
                double d10 = (double) l1 / (double) this.l;
                double d11 = MathHelper.a(d10, d0, d1, d2, d6, d4, d8, d3, d7, d5, d9);
                int i2 = k1 * this.l + l1;
                IBlockData iblockdata = this.a(d11, i2);

                if (aiblockdata != null) {
                    aiblockdata[i2] = iblockdata;
                }

                if (predicate != null && predicate.test(iblockdata)) {
                    return i2 + 1;
                }
            }
        }

        return 0;
    }

    protected IBlockData a(double d0, int i) {
        IBlockData iblockdata;

        if (d0 > 0.0D) {
            iblockdata = this.f;
        } else if (i < this.getSeaLevel()) {
            iblockdata = this.g;
        } else {
            iblockdata = ChunkGeneratorAbstract.k;
        }

        return iblockdata;
    }

    @Override
    public void buildBase(RegionLimitedWorldAccess regionlimitedworldaccess, IChunkAccess ichunkaccess) {
        ChunkCoordIntPair chunkcoordintpair = ichunkaccess.getPos();
        int i = chunkcoordintpair.x;
        int j = chunkcoordintpair.z;
        SeededRandom seededrandom = new SeededRandom();

        seededrandom.a(i, j);
        ChunkCoordIntPair chunkcoordintpair1 = ichunkaccess.getPos();
        int k = chunkcoordintpair1.d();
        int l = chunkcoordintpair1.e();
        double d0 = 0.0625D;
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

        for (int i1 = 0; i1 < 16; ++i1) {
            for (int j1 = 0; j1 < 16; ++j1) {
                int k1 = k + i1;
                int l1 = l + j1;
                int i2 = ichunkaccess.getHighestBlock(HeightMap.Type.WORLD_SURFACE_WG, i1, j1) + 1;
                double d1 = this.t.a((double) k1 * 0.0625D, (double) l1 * 0.0625D, 0.0625D, (double) i1 * 0.0625D) * 15.0D;

                regionlimitedworldaccess.getBiome(blockposition_mutableblockposition.d(k + i1, i2, l + j1)).a(seededrandom, ichunkaccess, k1, l1, i2, d1, this.f, this.g, this.getSeaLevel(), regionlimitedworldaccess.getSeed());
            }
        }

        this.a(ichunkaccess, seededrandom);
    }

    private void a(IChunkAccess ichunkaccess, Random random) {
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
        int i = ichunkaccess.getPos().d();
        int j = ichunkaccess.getPos().e();
        GeneratorSettingBase generatorsettingbase = (GeneratorSettingBase) this.h.get();
        int k = generatorsettingbase.f();
        int l = this.x - 1 - generatorsettingbase.e();
        boolean flag = true;
        boolean flag1 = l + 4 >= 0 && l < this.x;
        boolean flag2 = k + 4 >= 0 && k < this.x;

        if (flag1 || flag2) {
            Iterator iterator = BlockPosition.b(i, 0, j, i + 15, 0, j + 15).iterator();

            while (iterator.hasNext()) {
                BlockPosition blockposition = (BlockPosition) iterator.next();
                int i1;

                if (flag1) {
                    for (i1 = 0; i1 < 5; ++i1) {
                        if (i1 <= random.nextInt(5)) {
                            ichunkaccess.setType(blockposition_mutableblockposition.d(blockposition.getX(), l - i1, blockposition.getZ()), Blocks.BEDROCK.getBlockData(), false);
                        }
                    }
                }

                if (flag2) {
                    for (i1 = 4; i1 >= 0; --i1) {
                        if (i1 <= random.nextInt(5)) {
                            ichunkaccess.setType(blockposition_mutableblockposition.d(blockposition.getX(), k + i1, blockposition.getZ()), Blocks.BEDROCK.getBlockData(), false);
                        }
                    }
                }
            }

        }
    }

    @Override
    public void buildNoise(GeneratorAccess generatoraccess, StructureManager structuremanager, IChunkAccess ichunkaccess) {
        ObjectList<StructurePiece> objectlist = new ObjectArrayList(10);
        ObjectList<WorldGenFeatureDefinedStructureJigsawJunction> objectlist1 = new ObjectArrayList(32);
        ChunkCoordIntPair chunkcoordintpair = ichunkaccess.getPos();
        int i = chunkcoordintpair.x;
        int j = chunkcoordintpair.z;
        int k = i << 4;
        int l = j << 4;
        Iterator iterator = StructureGenerator.t.iterator();

        while (iterator.hasNext()) {
            StructureGenerator<?> structuregenerator = (StructureGenerator) iterator.next();

            structuremanager.a(SectionPosition.a(chunkcoordintpair, 0), structuregenerator).forEach((structurestart) -> {
                Iterator iterator1 = structurestart.d().iterator();

                while (iterator1.hasNext()) {
                    StructurePiece structurepiece = (StructurePiece) iterator1.next();

                    if (structurepiece.a(chunkcoordintpair, 12)) {
                        if (structurepiece instanceof WorldGenFeaturePillagerOutpostPoolPiece) {
                            WorldGenFeaturePillagerOutpostPoolPiece worldgenfeaturepillageroutpostpoolpiece = (WorldGenFeaturePillagerOutpostPoolPiece) structurepiece;
                            WorldGenFeatureDefinedStructurePoolTemplate.Matching worldgenfeaturedefinedstructurepooltemplate_matching = worldgenfeaturepillageroutpostpoolpiece.b().e();

                            if (worldgenfeaturedefinedstructurepooltemplate_matching == WorldGenFeatureDefinedStructurePoolTemplate.Matching.RIGID) {
                                objectlist.add(worldgenfeaturepillageroutpostpoolpiece);
                            }

                            Iterator iterator2 = worldgenfeaturepillageroutpostpoolpiece.e().iterator();

                            while (iterator2.hasNext()) {
                                WorldGenFeatureDefinedStructureJigsawJunction worldgenfeaturedefinedstructurejigsawjunction = (WorldGenFeatureDefinedStructureJigsawJunction) iterator2.next();
                                int i1 = worldgenfeaturedefinedstructurejigsawjunction.a();
                                int j1 = worldgenfeaturedefinedstructurejigsawjunction.c();

                                if (i1 > k - 12 && j1 > l - 12 && i1 < k + 15 + 12 && j1 < l + 15 + 12) {
                                    objectlist1.add(worldgenfeaturedefinedstructurejigsawjunction);
                                }
                            }
                        } else {
                            objectlist.add(structurepiece);
                        }
                    }
                }

            });
        }

        double[][][] adouble = new double[2][this.p + 1][this.o + 1];

        for (int i1 = 0; i1 < this.p + 1; ++i1) {
            adouble[0][i1] = new double[this.o + 1];
            this.a(adouble[0][i1], i * this.n, j * this.p + i1);
            adouble[1][i1] = new double[this.o + 1];
        }

        ProtoChunk protochunk = (ProtoChunk) ichunkaccess;
        HeightMap heightmap = protochunk.a(HeightMap.Type.OCEAN_FLOOR_WG);
        HeightMap heightmap1 = protochunk.a(HeightMap.Type.WORLD_SURFACE_WG);
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
        ObjectListIterator<StructurePiece> objectlistiterator = objectlist.iterator();
        ObjectListIterator<WorldGenFeatureDefinedStructureJigsawJunction> objectlistiterator1 = objectlist1.iterator();

        for (int j1 = 0; j1 < this.n; ++j1) {
            int k1;

            for (k1 = 0; k1 < this.p + 1; ++k1) {
                this.a(adouble[1][k1], i * this.n + j1 + 1, j * this.p + k1);
            }

            for (k1 = 0; k1 < this.p; ++k1) {
                ChunkSection chunksection = protochunk.a(15);

                chunksection.a();

                for (int l1 = this.o - 1; l1 >= 0; --l1) {
                    double d0 = adouble[0][k1][l1];
                    double d1 = adouble[0][k1 + 1][l1];
                    double d2 = adouble[1][k1][l1];
                    double d3 = adouble[1][k1 + 1][l1];
                    double d4 = adouble[0][k1][l1 + 1];
                    double d5 = adouble[0][k1 + 1][l1 + 1];
                    double d6 = adouble[1][k1][l1 + 1];
                    double d7 = adouble[1][k1 + 1][l1 + 1];

                    for (int i2 = this.l - 1; i2 >= 0; --i2) {
                        int j2 = l1 * this.l + i2;
                        int k2 = j2 & 15;
                        int l2 = j2 >> 4;

                        if (chunksection.getYPosition() >> 4 != l2) {
                            chunksection.b();
                            chunksection = protochunk.a(l2);
                            chunksection.a();
                        }

                        double d8 = (double) i2 / (double) this.l;
                        double d9 = MathHelper.d(d8, d0, d4);
                        double d10 = MathHelper.d(d8, d2, d6);
                        double d11 = MathHelper.d(d8, d1, d5);
                        double d12 = MathHelper.d(d8, d3, d7);

                        for (int i3 = 0; i3 < this.m; ++i3) {
                            int j3 = k + j1 * this.m + i3;
                            int k3 = j3 & 15;
                            double d13 = (double) i3 / (double) this.m;
                            double d14 = MathHelper.d(d13, d9, d10);
                            double d15 = MathHelper.d(d13, d11, d12);

                            for (int l3 = 0; l3 < this.m; ++l3) {
                                int i4 = l + k1 * this.m + l3;
                                int j4 = i4 & 15;
                                double d16 = (double) l3 / (double) this.m;
                                double d17 = MathHelper.d(d16, d14, d15);
                                double d18 = MathHelper.a(d17 / 200.0D, -1.0D, 1.0D);

                                int k4;
                                int l4;
                                int i5;

                                for (d18 = d18 / 2.0D - d18 * d18 * d18 / 24.0D; objectlistiterator.hasNext(); d18 += a(k4, l4, i5) * 0.8D) {
                                    StructurePiece structurepiece = (StructurePiece) objectlistiterator.next();
                                    StructureBoundingBox structureboundingbox = structurepiece.g();

                                    k4 = Math.max(0, Math.max(structureboundingbox.a - j3, j3 - structureboundingbox.d));
                                    l4 = j2 - (structureboundingbox.b + (structurepiece instanceof WorldGenFeaturePillagerOutpostPoolPiece ? ((WorldGenFeaturePillagerOutpostPoolPiece) structurepiece).d() : 0));
                                    i5 = Math.max(0, Math.max(structureboundingbox.c - i4, i4 - structureboundingbox.f));
                                }

                                objectlistiterator.back(objectlist.size());

                                while (objectlistiterator1.hasNext()) {
                                    WorldGenFeatureDefinedStructureJigsawJunction worldgenfeaturedefinedstructurejigsawjunction = (WorldGenFeatureDefinedStructureJigsawJunction) objectlistiterator1.next();
                                    int j5 = j3 - worldgenfeaturedefinedstructurejigsawjunction.a();

                                    k4 = j2 - worldgenfeaturedefinedstructurejigsawjunction.b();
                                    l4 = i4 - worldgenfeaturedefinedstructurejigsawjunction.c();
                                    d18 += a(j5, k4, l4) * 0.4D;
                                }

                                objectlistiterator1.back(objectlist1.size());
                                IBlockData iblockdata = this.a(d18, j2);

                                if (iblockdata != ChunkGeneratorAbstract.k) {
                                    if (iblockdata.f() != 0) {
                                        blockposition_mutableblockposition.d(j3, j2, i4);
                                        protochunk.k(blockposition_mutableblockposition);
                                    }

                                    chunksection.setType(k3, k2, j4, iblockdata, false);
                                    heightmap.a(k3, j2, j4, iblockdata);
                                    heightmap1.a(k3, j2, j4, iblockdata);
                                }
                            }
                        }
                    }
                }

                chunksection.b();
            }

            double[][] adouble1 = adouble[0];

            adouble[0] = adouble[1];
            adouble[1] = adouble1;
        }

    }

    private static double a(int i, int j, int k) {
        int l = i + 12;
        int i1 = j + 12;
        int j1 = k + 12;

        return l >= 0 && l < 24 ? (i1 >= 0 && i1 < 24 ? (j1 >= 0 && j1 < 24 ? (double) ChunkGeneratorAbstract.i[j1 * 24 * 24 + l * 24 + i1] : 0.0D) : 0.0D) : 0.0D;
    }

    private static double b(int i, int j, int k) {
        double d0 = (double) (i * i + k * k);
        double d1 = (double) j + 0.5D;
        double d2 = d1 * d1;
        double d3 = Math.pow(2.718281828459045D, -(d2 / 16.0D + d0 / 16.0D));
        double d4 = -d1 * MathHelper.i(d2 / 2.0D + d0 / 2.0D) / 2.0D;

        return d4 * d3;
    }

    @Override
    public int getGenerationDepth() {
        return this.x;
    }

    @Override
    public int getSeaLevel() {
        return ((GeneratorSettingBase) this.h.get()).g();
    }

    @Override
    public List<BiomeSettingsMobs.c> getMobsFor(BiomeBase biomebase, StructureManager structuremanager, EnumCreatureType enumcreaturetype, BlockPosition blockposition) {
        if (structuremanager.a(blockposition, true, StructureGenerator.SWAMP_HUT).e()) {
            if (enumcreaturetype == EnumCreatureType.MONSTER) {
                return StructureGenerator.SWAMP_HUT.c();
            }

            if (enumcreaturetype == EnumCreatureType.CREATURE) {
                return StructureGenerator.SWAMP_HUT.j();
            }
        }

        if (enumcreaturetype == EnumCreatureType.MONSTER) {
            if (structuremanager.a(blockposition, false, StructureGenerator.PILLAGER_OUTPOST).e()) {
                return StructureGenerator.PILLAGER_OUTPOST.c();
            }

            if (structuremanager.a(blockposition, false, StructureGenerator.MONUMENT).e()) {
                return StructureGenerator.MONUMENT.c();
            }

            if (structuremanager.a(blockposition, true, StructureGenerator.FORTRESS).e()) {
                return StructureGenerator.FORTRESS.c();
            }
        }

        return super.getMobsFor(biomebase, structuremanager, enumcreaturetype, blockposition);
    }

    @Override
    public void addMobs(RegionLimitedWorldAccess regionlimitedworldaccess) {
        if (!((GeneratorSettingBase) this.h.get()).h()) {
            int i = regionlimitedworldaccess.a();
            int j = regionlimitedworldaccess.b();
            BiomeBase biomebase = regionlimitedworldaccess.getBiome((new ChunkCoordIntPair(i, j)).l());
            SeededRandom seededrandom = new SeededRandom();

            seededrandom.a(regionlimitedworldaccess.getSeed(), i << 4, j << 4);
            SpawnerCreature.a(regionlimitedworldaccess, biomebase, i, j, seededrandom);
        }
    }
}
