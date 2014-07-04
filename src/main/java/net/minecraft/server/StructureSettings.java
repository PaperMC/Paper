package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;

public class StructureSettings {

    public static final Codec<StructureSettings> a = RecordCodecBuilder.create((instance) -> {
        return instance.group(StructureSettingsStronghold.a.optionalFieldOf("stronghold").forGetter((structuresettings) -> {
            return Optional.ofNullable(structuresettings.e);
        }), Codec.simpleMap(IRegistry.STRUCTURE_FEATURE, StructureSettingsFeature.a, IRegistry.STRUCTURE_FEATURE).fieldOf("structures").forGetter((structuresettings) -> {
            return structuresettings.d;
        })).apply(instance, StructureSettings::new);
    });
    public static final ImmutableMap<StructureGenerator<?>, StructureSettingsFeature> b = ImmutableMap.<StructureGenerator<?>, StructureSettingsFeature>builder().put(StructureGenerator.VILLAGE, new StructureSettingsFeature(32, 8, 10387312)).put(StructureGenerator.DESERT_PYRAMID, new StructureSettingsFeature(32, 8, 14357617)).put(StructureGenerator.IGLOO, new StructureSettingsFeature(32, 8, 14357618)).put(StructureGenerator.JUNGLE_PYRAMID, new StructureSettingsFeature(32, 8, 14357619)).put(StructureGenerator.SWAMP_HUT, new StructureSettingsFeature(32, 8, 14357620)).put(StructureGenerator.PILLAGER_OUTPOST, new StructureSettingsFeature(32, 8, 165745296)).put(StructureGenerator.STRONGHOLD, new StructureSettingsFeature(1, 0, 0)).put(StructureGenerator.MONUMENT, new StructureSettingsFeature(32, 5, 10387313)).put(StructureGenerator.ENDCITY, new StructureSettingsFeature(20, 11, 10387313)).put(StructureGenerator.MANSION, new StructureSettingsFeature(80, 20, 10387319)).put(StructureGenerator.BURIED_TREASURE, new StructureSettingsFeature(1, 0, 0)).put(StructureGenerator.MINESHAFT, new StructureSettingsFeature(1, 0, 0)).put(StructureGenerator.RUINED_PORTAL, new StructureSettingsFeature(40, 15, 34222645)).put(StructureGenerator.SHIPWRECK, new StructureSettingsFeature(24, 4, 165745295)).put(StructureGenerator.OCEAN_RUIN, new StructureSettingsFeature(20, 8, 14357621)).put(StructureGenerator.BASTION_REMNANT, new StructureSettingsFeature(27, 4, 30084232)).put(StructureGenerator.FORTRESS, new StructureSettingsFeature(27, 4, 30084232)).put(StructureGenerator.NETHER_FOSSIL, new StructureSettingsFeature(2, 1, 14357921)).build();
    public static final StructureSettingsStronghold c;
    private final Map<StructureGenerator<?>, StructureSettingsFeature> d;
    @Nullable
    private final StructureSettingsStronghold e;

    public StructureSettings(Optional<StructureSettingsStronghold> optional, Map<StructureGenerator<?>, StructureSettingsFeature> map) {
        this.e = (StructureSettingsStronghold) optional.orElse(null);
        this.d = Maps.newHashMap(map); // Spigot
    }

    public StructureSettings(boolean flag) {
        this.d = Maps.newHashMap(StructureSettings.b);
        this.e = flag ? StructureSettings.c : null;
    }

    public Map<StructureGenerator<?>, StructureSettingsFeature> a() {
        return this.d;
    }

    @Nullable
    public StructureSettingsFeature a(StructureGenerator<?> structuregenerator) {
        return (StructureSettingsFeature) this.d.get(structuregenerator);
    }

    @Nullable
    public StructureSettingsStronghold b() {
        return this.e;
    }

    static {
        c = new StructureSettingsStronghold(32, 3, 128);

        for (StructureGenerator<?> var1 : IRegistry.STRUCTURE_FEATURE) {
            if (!b.containsKey(var1)) {
                throw new IllegalStateException("Structure feature without default settings: " + IRegistry.STRUCTURE_FEATURE.getKey(var1));
            }
        }
    }
}
