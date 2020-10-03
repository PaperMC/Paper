package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class BiomeBase {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final Codec<BiomeBase> b = RecordCodecBuilder.create((instance) -> {
        return instance.group(BiomeBase.d.a.forGetter((biomebase) -> {
            return biomebase.j;
        }), BiomeBase.Geography.r.fieldOf("category").forGetter((biomebase) -> {
            return biomebase.o;
        }), Codec.FLOAT.fieldOf("depth").forGetter((biomebase) -> {
            return biomebase.m;
        }), Codec.FLOAT.fieldOf("scale").forGetter((biomebase) -> {
            return biomebase.n;
        }), BiomeFog.a.fieldOf("effects").forGetter((biomebase) -> {
            return biomebase.p;
        }), BiomeSettingsGeneration.c.forGetter((biomebase) -> {
            return biomebase.k;
        }), BiomeSettingsMobs.c.forGetter((biomebase) -> {
            return biomebase.l;
        })).apply(instance, BiomeBase::new);
    });
    public static final Codec<BiomeBase> c = RecordCodecBuilder.create((instance) -> {
        return instance.group(BiomeBase.d.a.forGetter((biomebase) -> {
            return biomebase.j;
        }), BiomeBase.Geography.r.fieldOf("category").forGetter((biomebase) -> {
            return biomebase.o;
        }), Codec.FLOAT.fieldOf("depth").forGetter((biomebase) -> {
            return biomebase.m;
        }), Codec.FLOAT.fieldOf("scale").forGetter((biomebase) -> {
            return biomebase.n;
        }), BiomeFog.a.fieldOf("effects").forGetter((biomebase) -> {
            return biomebase.p;
        })).apply(instance, (biomebase_d, biomebase_geography, ofloat, ofloat1, biomefog) -> {
            return new BiomeBase(biomebase_d, biomebase_geography, ofloat, ofloat1, biomefog, BiomeSettingsGeneration.b, BiomeSettingsMobs.b);
        });
    });
    public static final Codec<Supplier<BiomeBase>> d = RegistryFileCodec.a(IRegistry.ay, BiomeBase.b);
    public static final Codec<List<Supplier<BiomeBase>>> e = RegistryFileCodec.b(IRegistry.ay, BiomeBase.b);
    private final Map<Integer, List<StructureGenerator<?>>> g;
    private static final NoiseGenerator3 h = new NoiseGenerator3(new SeededRandom(1234L), ImmutableList.of(0));
    private static final NoiseGenerator3 i = new NoiseGenerator3(new SeededRandom(3456L), ImmutableList.of(-2, -1, 0));
    public static final NoiseGenerator3 f = new NoiseGenerator3(new SeededRandom(2345L), ImmutableList.of(0));
    private final BiomeBase.d j;
    private final BiomeSettingsGeneration k;
    private final BiomeSettingsMobs l;
    private final float m;
    private final float n;
    private final BiomeBase.Geography o;
    private final BiomeFog p;
    private final ThreadLocal<Long2FloatLinkedOpenHashMap> q;

    private BiomeBase(BiomeBase.d biomebase_d, BiomeBase.Geography biomebase_geography, float f, float f1, BiomeFog biomefog, BiomeSettingsGeneration biomesettingsgeneration, BiomeSettingsMobs biomesettingsmobs) {
        this.g = (Map) IRegistry.STRUCTURE_FEATURE.g().collect(Collectors.groupingBy((structuregenerator) -> {
            return structuregenerator.f().ordinal();
        }));
        this.q = ThreadLocal.withInitial(() -> {
            return (Long2FloatLinkedOpenHashMap) SystemUtils.a(() -> {
                Long2FloatLinkedOpenHashMap long2floatlinkedopenhashmap = new Long2FloatLinkedOpenHashMap(1024, 0.25F) {
                    protected void rehash(int i) {}
                };

                long2floatlinkedopenhashmap.defaultReturnValue(Float.NaN);
                return long2floatlinkedopenhashmap;
            });
        });
        this.j = biomebase_d;
        this.k = biomesettingsgeneration;
        this.l = biomesettingsmobs;
        this.o = biomebase_geography;
        this.m = f;
        this.n = f1;
        this.p = biomefog;
    }

    public BiomeSettingsMobs b() {
        return this.l;
    }

    public BiomeBase.Precipitation c() {
        return this.j.b;
    }

    public boolean d() {
        return this.getHumidity() > 0.85F;
    }

    private float b(BlockPosition blockposition) {
        float f = this.j.d.a(blockposition, this.k());

        if (blockposition.getY() > 64) {
            float f1 = (float) (BiomeBase.h.a((double) ((float) blockposition.getX() / 8.0F), (double) ((float) blockposition.getZ() / 8.0F), false) * 4.0D);

            return f - (f1 + (float) blockposition.getY() - 64.0F) * 0.05F / 30.0F;
        } else {
            return f;
        }
    }

    public final float getAdjustedTemperature(BlockPosition blockposition) {
        long i = blockposition.asLong();
        Long2FloatLinkedOpenHashMap long2floatlinkedopenhashmap = (Long2FloatLinkedOpenHashMap) this.q.get();
        float f = long2floatlinkedopenhashmap.get(i);

        if (!Float.isNaN(f)) {
            return f;
        } else {
            float f1 = this.b(blockposition);

            if (long2floatlinkedopenhashmap.size() == 1024) {
                long2floatlinkedopenhashmap.removeFirstFloat();
            }

            long2floatlinkedopenhashmap.put(i, f1);
            return f1;
        }
    }

    public boolean a(IWorldReader iworldreader, BlockPosition blockposition) {
        return this.a(iworldreader, blockposition, true);
    }

    public boolean a(IWorldReader iworldreader, BlockPosition blockposition, boolean flag) {
        if (this.getAdjustedTemperature(blockposition) >= 0.15F) {
            return false;
        } else {
            if (blockposition.getY() >= 0 && blockposition.getY() < 256 && iworldreader.getBrightness(EnumSkyBlock.BLOCK, blockposition) < 10) {
                IBlockData iblockdata = iworldreader.getType(blockposition);
                Fluid fluid = iworldreader.getFluid(blockposition);

                if (fluid.getType() == FluidTypes.WATER && iblockdata.getBlock() instanceof BlockFluids) {
                    if (!flag) {
                        return true;
                    }

                    boolean flag1 = iworldreader.A(blockposition.west()) && iworldreader.A(blockposition.east()) && iworldreader.A(blockposition.north()) && iworldreader.A(blockposition.south());

                    if (!flag1) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public boolean b(IWorldReader iworldreader, BlockPosition blockposition) {
        if (this.getAdjustedTemperature(blockposition) >= 0.15F) {
            return false;
        } else {
            if (blockposition.getY() >= 0 && blockposition.getY() < 256 && iworldreader.getBrightness(EnumSkyBlock.BLOCK, blockposition) < 10) {
                IBlockData iblockdata = iworldreader.getType(blockposition);

                if (iblockdata.isAir() && Blocks.SNOW.getBlockData().canPlace(iworldreader, blockposition)) {
                    return true;
                }
            }

            return false;
        }
    }

    public BiomeSettingsGeneration e() {
        return this.k;
    }

    public void a(StructureManager structuremanager, ChunkGenerator chunkgenerator, RegionLimitedWorldAccess regionlimitedworldaccess, long i, SeededRandom seededrandom, BlockPosition blockposition) {
        List<List<Supplier<WorldGenFeatureConfigured<?, ?>>>> list = this.k.c();
        int j = WorldGenStage.Decoration.values().length;

        for (int k = 0; k < j; ++k) {
            int l = 0;

            if (structuremanager.a()) {
                List<StructureGenerator<?>> list1 = (List) this.g.getOrDefault(k, Collections.emptyList());

                for (Iterator iterator = list1.iterator(); iterator.hasNext(); ++l) {
                    StructureGenerator<?> structuregenerator = (StructureGenerator) iterator.next();

                    seededrandom.b(i, l, k);
                    int i1 = blockposition.getX() >> 4;
                    int j1 = blockposition.getZ() >> 4;
                    int k1 = i1 << 4;
                    int l1 = j1 << 4;

                    try {
                        structuremanager.a(SectionPosition.a(blockposition), structuregenerator).forEach((structurestart) -> {
                            structurestart.a(regionlimitedworldaccess, structuremanager, chunkgenerator, seededrandom, new StructureBoundingBox(k1, l1, k1 + 15, l1 + 15), new ChunkCoordIntPair(i1, j1));
                        });
                    } catch (Exception exception) {
                        CrashReport crashreport = CrashReport.a(exception, "Feature placement");

                        crashreport.a("Feature").a("Id", (Object) IRegistry.STRUCTURE_FEATURE.getKey(structuregenerator)).a("Description", () -> {
                            return structuregenerator.toString();
                        });
                        throw new ReportedException(crashreport);
                    }
                }
            }

            if (list.size() > k) {
                for (Iterator iterator1 = ((List) list.get(k)).iterator(); iterator1.hasNext(); ++l) {
                    Supplier<WorldGenFeatureConfigured<?, ?>> supplier = (Supplier) iterator1.next();
                    WorldGenFeatureConfigured<?, ?> worldgenfeatureconfigured = (WorldGenFeatureConfigured) supplier.get();

                    seededrandom.b(i, l, k);

                    try {
                        worldgenfeatureconfigured.a(regionlimitedworldaccess, chunkgenerator, seededrandom, blockposition);
                    } catch (Exception exception1) {
                        CrashReport crashreport1 = CrashReport.a(exception1, "Feature placement");

                        crashreport1.a("Feature").a("Id", (Object) IRegistry.FEATURE.getKey(worldgenfeatureconfigured.e)).a("Config", (Object) worldgenfeatureconfigured.f).a("Description", () -> {
                            return worldgenfeatureconfigured.e.toString();
                        });
                        throw new ReportedException(crashreport1);
                    }
                }
            }
        }

    }

    public void a(Random random, IChunkAccess ichunkaccess, int i, int j, int k, double d0, IBlockData iblockdata, IBlockData iblockdata1, int l, long i1) {
        WorldGenSurfaceComposite<?> worldgensurfacecomposite = (WorldGenSurfaceComposite) this.k.d().get();

        worldgensurfacecomposite.a(i1);
        worldgensurfacecomposite.a(random, ichunkaccess, this, i, j, k, d0, iblockdata, iblockdata1, l, i1);
    }

    public final float h() {
        return this.m;
    }

    public final float getHumidity() {
        return this.j.e;
    }

    public final float j() {
        return this.n;
    }

    public final float k() {
        return this.j.c;
    }

    public BiomeFog l() {
        return this.p;
    }

    public final BiomeBase.Geography t() {
        return this.o;
    }

    public String toString() {
        MinecraftKey minecraftkey = RegistryGeneration.WORLDGEN_BIOME.getKey(this);

        return minecraftkey == null ? super.toString() : minecraftkey.toString();
    }

    static class d {

        public static final MapCodec<BiomeBase.d> a = RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(BiomeBase.Precipitation.d.fieldOf("precipitation").forGetter((biomebase_d) -> {
                return biomebase_d.b;
            }), Codec.FLOAT.fieldOf("temperature").forGetter((biomebase_d) -> {
                return biomebase_d.c;
            }), BiomeBase.TemperatureModifier.c.optionalFieldOf("temperature_modifier", BiomeBase.TemperatureModifier.NONE).forGetter((biomebase_d) -> {
                return biomebase_d.d;
            }), Codec.FLOAT.fieldOf("downfall").forGetter((biomebase_d) -> {
                return biomebase_d.e;
            })).apply(instance, BiomeBase.d::new);
        });
        private final BiomeBase.Precipitation b;
        private final float c;
        private final BiomeBase.TemperatureModifier d;
        private final float e;

        private d(BiomeBase.Precipitation biomebase_precipitation, float f, BiomeBase.TemperatureModifier biomebase_temperaturemodifier, float f1) {
            this.b = biomebase_precipitation;
            this.c = f;
            this.d = biomebase_temperaturemodifier;
            this.e = f1;
        }
    }

    public static class c {

        public static final Codec<BiomeBase.c> a = RecordCodecBuilder.create((instance) -> {
            return instance.group(Codec.floatRange(-2.0F, 2.0F).fieldOf("temperature").forGetter((biomebase_c) -> {
                return biomebase_c.b;
            }), Codec.floatRange(-2.0F, 2.0F).fieldOf("humidity").forGetter((biomebase_c) -> {
                return biomebase_c.c;
            }), Codec.floatRange(-2.0F, 2.0F).fieldOf("altitude").forGetter((biomebase_c) -> {
                return biomebase_c.d;
            }), Codec.floatRange(-2.0F, 2.0F).fieldOf("weirdness").forGetter((biomebase_c) -> {
                return biomebase_c.e;
            }), Codec.floatRange(0.0F, 1.0F).fieldOf("offset").forGetter((biomebase_c) -> {
                return biomebase_c.f;
            })).apply(instance, BiomeBase.c::new);
        });
        private final float b;
        private final float c;
        private final float d;
        private final float e;
        private final float f;

        public c(float f, float f1, float f2, float f3, float f4) {
            this.b = f;
            this.c = f1;
            this.d = f2;
            this.e = f3;
            this.f = f4;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else if (object != null && this.getClass() == object.getClass()) {
                BiomeBase.c biomebase_c = (BiomeBase.c) object;

                return Float.compare(biomebase_c.b, this.b) != 0 ? false : (Float.compare(biomebase_c.c, this.c) != 0 ? false : (Float.compare(biomebase_c.d, this.d) != 0 ? false : Float.compare(biomebase_c.e, this.e) == 0));
            } else {
                return false;
            }
        }

        public int hashCode() {
            int i = this.b != 0.0F ? Float.floatToIntBits(this.b) : 0;

            i = 31 * i + (this.c != 0.0F ? Float.floatToIntBits(this.c) : 0);
            i = 31 * i + (this.d != 0.0F ? Float.floatToIntBits(this.d) : 0);
            i = 31 * i + (this.e != 0.0F ? Float.floatToIntBits(this.e) : 0);
            return i;
        }

        public float a(BiomeBase.c biomebase_c) {
            return (this.b - biomebase_c.b) * (this.b - biomebase_c.b) + (this.c - biomebase_c.c) * (this.c - biomebase_c.c) + (this.d - biomebase_c.d) * (this.d - biomebase_c.d) + (this.e - biomebase_c.e) * (this.e - biomebase_c.e) + (this.f - biomebase_c.f) * (this.f - biomebase_c.f);
        }
    }

    public static class a {

        @Nullable
        private BiomeBase.Precipitation a;
        @Nullable
        private BiomeBase.Geography b;
        @Nullable
        private Float c;
        @Nullable
        private Float d;
        @Nullable
        private Float e;
        private BiomeBase.TemperatureModifier f;
        @Nullable
        private Float g;
        @Nullable
        private BiomeFog h;
        @Nullable
        private BiomeSettingsMobs i;
        @Nullable
        private BiomeSettingsGeneration j;

        public a() {
            this.f = BiomeBase.TemperatureModifier.NONE;
        }

        public BiomeBase.a a(BiomeBase.Precipitation biomebase_precipitation) {
            this.a = biomebase_precipitation;
            return this;
        }

        public BiomeBase.a a(BiomeBase.Geography biomebase_geography) {
            this.b = biomebase_geography;
            return this;
        }

        public BiomeBase.a a(float f) {
            this.c = f;
            return this;
        }

        public BiomeBase.a b(float f) {
            this.d = f;
            return this;
        }

        public BiomeBase.a c(float f) {
            this.e = f;
            return this;
        }

        public BiomeBase.a d(float f) {
            this.g = f;
            return this;
        }

        public BiomeBase.a a(BiomeFog biomefog) {
            this.h = biomefog;
            return this;
        }

        public BiomeBase.a a(BiomeSettingsMobs biomesettingsmobs) {
            this.i = biomesettingsmobs;
            return this;
        }

        public BiomeBase.a a(BiomeSettingsGeneration biomesettingsgeneration) {
            this.j = biomesettingsgeneration;
            return this;
        }

        public BiomeBase.a a(BiomeBase.TemperatureModifier biomebase_temperaturemodifier) {
            this.f = biomebase_temperaturemodifier;
            return this;
        }

        public BiomeBase a() {
            if (this.a != null && this.b != null && this.c != null && this.d != null && this.e != null && this.g != null && this.h != null && this.i != null && this.j != null) {
                return new BiomeBase(new BiomeBase.d(this.a, this.e, this.f, this.g), this.b, this.c, this.d, this.h, this.j, this.i);
            } else {
                throw new IllegalStateException("You are missing parameters to build a proper biome\n" + this);
            }
        }

        public String toString() {
            return "BiomeBuilder{\nprecipitation=" + this.a + ",\nbiomeCategory=" + this.b + ",\ndepth=" + this.c + ",\nscale=" + this.d + ",\ntemperature=" + this.e + ",\ntemperatureModifier=" + this.f + ",\ndownfall=" + this.g + ",\nspecialEffects=" + this.h + ",\nmobSpawnSettings=" + this.i + ",\ngenerationSettings=" + this.j + ",\n" + '}';
        }
    }

    public static enum TemperatureModifier implements INamable {

        NONE("none") {
            @Override
            public float a(BlockPosition blockposition, float f) {
                return f;
            }
        },
        FROZEN("frozen") {
            @Override
            public float a(BlockPosition blockposition, float f) {
                double d0 = BiomeBase.i.a((double) blockposition.getX() * 0.05D, (double) blockposition.getZ() * 0.05D, false) * 7.0D;
                double d1 = BiomeBase.f.a((double) blockposition.getX() * 0.2D, (double) blockposition.getZ() * 0.2D, false);
                double d2 = d0 + d1;

                if (d2 < 0.3D) {
                    double d3 = BiomeBase.f.a((double) blockposition.getX() * 0.09D, (double) blockposition.getZ() * 0.09D, false);

                    if (d3 < 0.8D) {
                        return 0.2F;
                    }
                }

                return f;
            }
        };

        private final String d;
        public static final Codec<BiomeBase.TemperatureModifier> c = INamable.a(BiomeBase.TemperatureModifier::values, BiomeBase.TemperatureModifier::a);
        private static final Map<String, BiomeBase.TemperatureModifier> e = (Map) Arrays.stream(values()).collect(Collectors.toMap(BiomeBase.TemperatureModifier::b, (biomebase_temperaturemodifier) -> {
            return biomebase_temperaturemodifier;
        }));

        public abstract float a(BlockPosition blockposition, float f);

        private TemperatureModifier(String s) {
            this.d = s;
        }

        public String b() {
            return this.d;
        }

        @Override
        public String getName() {
            return this.d;
        }

        public static BiomeBase.TemperatureModifier a(String s) {
            return (BiomeBase.TemperatureModifier) BiomeBase.TemperatureModifier.e.get(s);
        }
    }

    public static enum Precipitation implements INamable {

        NONE("none"), RAIN("rain"), SNOW("snow");

        public static final Codec<BiomeBase.Precipitation> d = INamable.a(BiomeBase.Precipitation::values, BiomeBase.Precipitation::a);
        private static final Map<String, BiomeBase.Precipitation> e = (Map) Arrays.stream(values()).collect(Collectors.toMap(BiomeBase.Precipitation::b, (biomebase_precipitation) -> {
            return biomebase_precipitation;
        }));
        private final String f;

        private Precipitation(String s) {
            this.f = s;
        }

        public String b() {
            return this.f;
        }

        public static BiomeBase.Precipitation a(String s) {
            return (BiomeBase.Precipitation) BiomeBase.Precipitation.e.get(s);
        }

        @Override
        public String getName() {
            return this.f;
        }
    }

    public static enum Geography implements INamable {

        NONE("none"), TAIGA("taiga"), EXTREME_HILLS("extreme_hills"), JUNGLE("jungle"), MESA("mesa"), PLAINS("plains"), SAVANNA("savanna"), ICY("icy"), THEEND("the_end"), BEACH("beach"), FOREST("forest"), OCEAN("ocean"), DESERT("desert"), RIVER("river"), SWAMP("swamp"), MUSHROOM("mushroom"), NETHER("nether");

        public static final Codec<BiomeBase.Geography> r = INamable.a(BiomeBase.Geography::values, BiomeBase.Geography::a);
        private static final Map<String, BiomeBase.Geography> s = (Map) Arrays.stream(values()).collect(Collectors.toMap(BiomeBase.Geography::b, (biomebase_geography) -> {
            return biomebase_geography;
        }));
        private final String t;

        private Geography(String s) {
            this.t = s;
        }

        public String b() {
            return this.t;
        }

        public static BiomeBase.Geography a(String s) {
            return (BiomeBase.Geography) BiomeBase.Geography.s.get(s);
        }

        @Override
        public String getName() {
            return this.t;
        }
    }
}
