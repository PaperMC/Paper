package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BiomeSettingsMobs {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final BiomeSettingsMobs b = new BiomeSettingsMobs(0.1F, (Map) Stream.of(EnumCreatureType.values()).collect(ImmutableMap.toImmutableMap((enumcreaturetype) -> {
        return enumcreaturetype;
    }, (enumcreaturetype) -> {
        return ImmutableList.of();
    })), ImmutableMap.of(), false);
    // Paper start- decompile error workaround
    private static class bProxy extends BiomeSettingsMobs.b {
        private bProxy(double d0, double d1) {
            super(d0, d1);
        }
    }
    private static class cProxy extends BiomeSettingsMobs.c {
        public cProxy(EntityTypes<?> entitytypes, int i, int j, int k) {
            super(entitytypes, i, j, k);
        }
    };
    // Paper end
    public static final MapCodec<BiomeSettingsMobs> c = RecordCodecBuilder.mapCodec((instance) -> {
        RecordCodecBuilder<BiomeSettingsMobs, Float> recordcodecbuilder = Codec.FLOAT.optionalFieldOf("creature_spawn_probability", 0.1F).forGetter((biomesettingsmobs) -> { // Paper - add type to builder
            return biomesettingsmobs.d;
        });
        // Paper - remove unused vars

        return instance.group(recordcodecbuilder, Codec.simpleMap(EnumCreatureType.g, cProxy.b.listOf().promotePartial(SystemUtils.a("Spawn data: ", BiomeSettingsMobs.LOGGER::error)), INamable.a(EnumCreatureType.values())).fieldOf("spawners").forGetter((biomesettingsmobs) -> { // Paper - inline codec, cProxy, LOGGER
            return biomesettingsmobs.e;
        }), Codec.simpleMap(IRegistry.ENTITY_TYPE, bProxy.a, IRegistry.ENTITY_TYPE).fieldOf("spawn_costs").forGetter((biomesettingsmobs) -> { // Paper - decompile error - bProxy
            return biomesettingsmobs.f;
        }), Codec.BOOL.fieldOf("player_spawn_friendly").orElse(false).forGetter(BiomeSettingsMobs::b)).apply(instance, BiomeSettingsMobs::new);
    });
    private final float d;
    private final Map<EnumCreatureType, List<BiomeSettingsMobs.c>> e;
    private final Map<EntityTypes<?>, BiomeSettingsMobs.b> f;
    private final boolean g;

    private BiomeSettingsMobs(float f, Map<EnumCreatureType, List<BiomeSettingsMobs.c>> map, Map<EntityTypes<?>, BiomeSettingsMobs.b> map1, boolean flag) {
        this.d = f;
        this.e = map;
        this.f = map1;
        this.g = flag;
    }

    public List<BiomeSettingsMobs.c> a(EnumCreatureType enumcreaturetype) {
        return (List) this.e.getOrDefault(enumcreaturetype, ImmutableList.of());
    }

    @Nullable
    public BiomeSettingsMobs.b a(EntityTypes<?> entitytypes) {
        return (BiomeSettingsMobs.b) this.f.get(entitytypes);
    }

    public float a() {
        return this.d;
    }

    public boolean b() {
        return this.g;
    }

    public static class a {

        // Paper start - keep track of data in a pair set to give O(1) contains calls - we have to hook removals incase plugins mess with it
        public static class MobList extends java.util.ArrayList<BiomeSettingsMobs.c> {
            java.util.Set<BiomeSettingsMobs.c> biomes = new java.util.HashSet<>();

            @Override
            public boolean contains(Object o) {
                return biomes.contains(o);
            }

            @Override
            public boolean add(BiomeSettingsMobs.c BiomeSettingsMobs) {
                biomes.add(BiomeSettingsMobs);
                return super.add(BiomeSettingsMobs);
            }

            @Override
            public BiomeSettingsMobs.c remove(int index) {
                BiomeSettingsMobs.c removed = super.remove(index);
                if (removed != null) {
                    biomes.remove(removed);
                }
                return removed;
            }

            @Override
            public void clear() {
                biomes.clear();
                super.clear();
            }
        }
        // use toImmutableEnumMap collector
        private final Map<EnumCreatureType, List<BiomeSettingsMobs.c>> a = (Map) Stream.of(EnumCreatureType.values()).collect(Maps.toImmutableEnumMap((enumcreaturetype) -> {
            return enumcreaturetype;
        }, (enumcreaturetype) -> {
            return new MobList(); // Use MobList instead of ArrayList
        }));
        // Paper end
        private final Map<EntityTypes<?>, BiomeSettingsMobs.b> b = Maps.newLinkedHashMap();
        private float c = 0.1F;
        private boolean d;

        public a() {}

        public BiomeSettingsMobs.a a(EnumCreatureType enumcreaturetype, BiomeSettingsMobs.c biomesettingsmobs_c) {
            ((List) this.a.get(enumcreaturetype)).add(biomesettingsmobs_c);
            return this;
        }

        public BiomeSettingsMobs.a a(EntityTypes<?> entitytypes, double d0, double d1) {
            this.b.put(entitytypes, new BiomeSettingsMobs.b(d1, d0));
            return this;
        }

        public BiomeSettingsMobs.a a(float f) {
            this.c = f;
            return this;
        }

        public BiomeSettingsMobs.a a() {
            this.d = true;
            return this;
        }

        public BiomeSettingsMobs b() {
            return new BiomeSettingsMobs(this.c, (Map) this.a.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, (entry) -> {
                return ImmutableList.copyOf((Collection) entry.getValue());
            })), ImmutableMap.copyOf(this.b), this.d);
        }
    }

    public static class b {

        public static final Codec<BiomeSettingsMobs.b> a = RecordCodecBuilder.create((instance) -> {
            return instance.group(Codec.DOUBLE.fieldOf("energy_budget").forGetter((biomesettingsmobs_b) -> {
                return biomesettingsmobs_b.b;
            }), Codec.DOUBLE.fieldOf("charge").forGetter((biomesettingsmobs_b) -> {
                return biomesettingsmobs_b.c;
            })).apply(instance, BiomeSettingsMobs.b::new);
        });
        private final double b;
        private final double c;

        private b(double d0, double d1) {
            this.b = d0;
            this.c = d1;
        }

        public double a() {
            return this.b;
        }

        public double b() {
            return this.c;
        }
    }

    public static class c extends WeightedRandom.WeightedRandomChoice {

        public static final Codec<BiomeSettingsMobs.c> b = RecordCodecBuilder.create((instance) -> {
            return instance.group(IRegistry.ENTITY_TYPE.fieldOf("type").forGetter((biomesettingsmobs_c) -> {
                return biomesettingsmobs_c.c;
            }), Codec.INT.fieldOf("weight").forGetter((biomesettingsmobs_c) -> {
                return biomesettingsmobs_c.a;
            }), Codec.INT.fieldOf("minCount").forGetter((biomesettingsmobs_c) -> {
                return biomesettingsmobs_c.d;
            }), Codec.INT.fieldOf("maxCount").forGetter((biomesettingsmobs_c) -> {
                return biomesettingsmobs_c.e;
            })).apply(instance, BiomeSettingsMobs.c::new);
        });
        public final EntityTypes<?> c;
        public final int d;
        public final int e;

        public c(EntityTypes<?> entitytypes, int i, int j, int k) {
            super(i);
            this.c = entitytypes.e() == EnumCreatureType.MISC ? EntityTypes.PIG : entitytypes;
            this.d = j;
            this.e = k;
        }

        public String toString() {
            return EntityTypes.getName(this.c) + "*(" + this.d + "-" + this.e + "):" + this.a;
        }
    }
}
