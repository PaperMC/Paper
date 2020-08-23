package net.minecraft.server;

import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;
import java.util.Map;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistryGeneration {

    protected static final Logger LOGGER = LogManager.getLogger();
    private static final Map<MinecraftKey, Supplier<?>> k = Maps.newLinkedHashMap();
    private static final IRegistryWritable<IRegistryWritable<?>> l = new RegistryMaterials<>(ResourceKey.a(new MinecraftKey("root")), Lifecycle.experimental());
    public static final IRegistry<? extends IRegistry<?>> b = RegistryGeneration.l;
    public static final IRegistry<WorldGenSurfaceComposite<?>> c = a(IRegistry.as, () -> {
        return WorldGenSurfaceComposites.p;
    });
    public static final IRegistry<WorldGenCarverWrapper<?>> d = a(IRegistry.at, () -> {
        return WorldGenCarvers.a;
    });
    public static final IRegistry<WorldGenFeatureConfigured<?, ?>> e = a(IRegistry.au, () -> {
        return BiomeDecoratorGroups.OAK;
    });
    public static final IRegistry<StructureFeature<?, ?>> f = a(IRegistry.av, () -> {
        return StructureFeatures.b;
    });
    public static final IRegistry<ProcessorList> g = a(IRegistry.aw, () -> {
        return ProcessorLists.b;
    });
    public static final IRegistry<WorldGenFeatureDefinedStructurePoolTemplate> h = a(IRegistry.ax, () -> WorldGenFeaturePieces.a()); // Paper - MC-197271
    public static final IRegistry<BiomeBase> WORLDGEN_BIOME = a(IRegistry.ay, () -> {
        return BiomeRegistry.a;
    });
    public static final IRegistry<GeneratorSettingBase> j = a(IRegistry.ar, () -> GeneratorSettingBase.i()); // Paper - MC-197271

    private static <T> IRegistry<T> a(ResourceKey<? extends IRegistry<T>> resourcekey, Supplier<T> supplier) {
        return a(resourcekey, Lifecycle.stable(), supplier);
    }

    private static <T> IRegistry<T> a(ResourceKey<? extends IRegistry<T>> resourcekey, Lifecycle lifecycle, Supplier<T> supplier) {
        return a(resourcekey, new RegistryMaterials<>(resourcekey, lifecycle), supplier, lifecycle);
    }

    private static <T, R extends IRegistryWritable<T>> R a(ResourceKey<? extends IRegistry<T>> resourcekey, R r0, Supplier<T> supplier, Lifecycle lifecycle) {
        MinecraftKey minecraftkey = resourcekey.a();

        RegistryGeneration.k.put(minecraftkey, supplier);
        IRegistryWritable<R> iregistrywritable = (IRegistryWritable<R>) RegistryGeneration.l; // Paper - decompile fix

        return (R) iregistrywritable.a((ResourceKey<R>) resourcekey, r0, lifecycle); // Paper - decompile fix
    }

    public static <T> T a(IRegistry<? super T> iregistry, String s, T t0) {
        return a(iregistry, new MinecraftKey(s), t0);
    }

    public static <V, T extends V> T a(IRegistry<V> iregistry, MinecraftKey minecraftkey, T t0) {
        return (T) ((IRegistryWritable) iregistry).a(ResourceKey.a(iregistry.f(), minecraftkey), t0, Lifecycle.stable()); // Paper - decompile fix
    }

    public static <V, T extends V> T a(IRegistry<V> iregistry, int i, ResourceKey<V> resourcekey, T t0) {
        return (T) ((IRegistryWritable) iregistry).a(i, resourcekey, t0, Lifecycle.stable()); // Paper - decompile fix
    }

    public static void a() {}

    static {
        RegistryGeneration.k.forEach((minecraftkey, supplier) -> {
            if (supplier.get() == null) {
                RegistryGeneration.LOGGER.error("Unable to bootstrap registry '{}'", minecraftkey);
            }

        });
        IRegistry.a(RegistryGeneration.l);
    }
}
