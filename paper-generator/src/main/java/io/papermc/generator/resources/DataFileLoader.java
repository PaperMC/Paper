package io.papermc.generator.resources;

import com.google.common.base.Suppliers;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.mojang.serialization.Codec;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.registry.RegistryEntry;
import io.papermc.generator.resources.data.EntityClassData;
import io.papermc.generator.resources.data.EntityTypeData;
import io.papermc.generator.resources.data.ItemMetaData;
import io.papermc.generator.resources.data.RegistryData;
import io.papermc.generator.resources.predicate.BlockPredicate;
import io.papermc.generator.resources.predicate.ItemPredicate;
import io.papermc.generator.utils.BasePackage;
import io.papermc.generator.utils.ClassHelper;
import io.papermc.generator.utils.Formatting;
import io.papermc.generator.utils.SourceCodecs;
import io.papermc.typewriter.ClassNamed;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

public class DataFileLoader {

    private static <K, V> Transmuter<Map<K, V>, Map.Entry<K, V>, K> sortedMap(Comparator<K> comparator) {
        return transmuteMap(SliceResult::empty, comparator);
    }

    private static <K, V> Transmuter<Map<K, V>, Map.Entry<K, V>, K> transmuteMap(Supplier<Set<K>> typesProvider, Function<K, V> onMissing, Comparator<K> comparator) {
        return transmuteMap(map -> {
            Set<K> types = typesProvider.get();
            Set<K> registeredTypes = map.keySet();

            Set<Map.Entry<K, V>> added = new HashSet<>();
            Sets.difference(types, registeredTypes).forEach(missingType -> added.add(Map.entry(missingType, onMissing.apply(missingType))));
            Set<K> removed = new HashSet<>(Sets.difference(registeredTypes, types));
            return new SliceResult<>(added, removed);
        }, comparator);
    }

    private static <K, V> Transmuter<Map<K, V>, Map.Entry<K, V>, K> transmuteMap(Function<Map<K, V>, SliceResult<Map.Entry<K, V>, K>> slicer, Comparator<K> comparator) {
        return new Transmuter.Mutable<>() {

            @Override
            public SliceResult<Map.Entry<K, V>, K> examine(Map<K, V> original) {
                return slicer.apply(original);
            }

            @Override
            public Map<K, V> apply(Map<K, V> original, SliceResult<Map.Entry<K, V>, K> result) {
                Map<K, V> map = new TreeMap<>(comparator);
                map.putAll(original);
                result.added().forEach(entry -> map.put(entry.getKey(), entry.getValue()));
                result.removed().forEach(map::remove);
                return map;
            }
        };
    }

    private static final @MonotonicNonNull Map<ResourceKey<? extends DataFile<?, ?, ?>>, DataFile<?, ?, ?>> DATA_FILES = new HashMap<>();
    public static final @MonotonicNonNull Map<ResourceKey<? extends DataFile<?, ?, ?>>, DataFile<?, ?, ?>> DATA_FILES_VIEW = Collections.unmodifiableMap(DATA_FILES);

    private static final Supplier<Map<ResourceKey<EntityType<?>>, Class<?>>> ENTITY_TYPE_GENERICS = Suppliers.memoize(() -> RegistryEntries.byRegistryKey(Registries.ENTITY_TYPE).getFields(field -> {
        if (field.getGenericType() instanceof ParameterizedType complexType && complexType.getActualTypeArguments().length == 1) {
            return (Class<?>) complexType.getActualTypeArguments()[0];
        }
        return null;
    }));

    public static final Map<RegistryEntry.Type, DataFile.Map<ResourceKey<? extends Registry<?>>, RegistryData>> REGISTRIES = Collections.unmodifiableMap(Util.make(new EnumMap<>(RegistryEntry.Type.class), map -> {
        Codec<Map<ResourceKey<? extends Registry<?>>, RegistryData>> codec = Codec.lazyInitialized(() -> Codec.unboundedMap(SourceCodecs.REGISTRY_KEY, RegistryData.CODEC));
        for (RegistryEntry.Type type : RegistryEntry.Type.values()) {
            ResourceKey<DataFile.Map<ResourceKey<? extends Registry<?>>, RegistryData>> key = DataFiles.registry(type);
            map.put(type, register(key, path -> {
                return new DataFile.Map<>(path, codec, SliceResult::empty);
            }));
        }
    }));

    static {
        // todo remove Orientation once the duplicate enum is gone and then possibly check enum property types conflict instead
        register(DataFiles.BLOCK_STATE_AMBIGUOUS_NAMES, () -> Codec.unboundedMap(
                SourceCodecs.IDENTIFIER, ExtraCodecs.nonEmptyList(SourceCodecs.IDENTIFIER.listOf())
            ),
            (path, codec) -> new DataFile.Map<>(
                path, codec, SliceResult::empty
            ));

        register(DataFiles.BLOCK_STATE_ENUM_PROPERTY_TYPES, () -> Codec.unboundedMap(
                SourceCodecs.classCodec(new TypeToken<Enum<? extends StringRepresentable>>() {}), SourceCodecs.CLASS_NAME
            ),
            (path, codec) -> new DataFile.Map<>(
                path, codec,
                transmuteMap(() -> {
                        try {
                            Set<Class<? extends Enum<? extends StringRepresentable>>> enumPropertyTypes = Collections.newSetFromMap(new IdentityHashMap<>());
                            for (Field field : BlockStateProperties.class.getDeclaredFields()) {
                                if (ClassHelper.isStaticConstant(field, Modifier.PUBLIC)) {
                                    if (!EnumProperty.class.isAssignableFrom(field.getType())) {
                                        continue;
                                    }

                                    enumPropertyTypes.add(((EnumProperty<?>) field.get(null)).getValueClass());
                                }
                            }
                            return Collections.unmodifiableSet(enumPropertyTypes);
                        } catch (ReflectiveOperationException ex) {
                            throw new RuntimeException(ex);
                        }
                    },
                    missingType -> BasePackage.BUKKIT.relativeClass("block.data.type", missingType.getSimpleName()),
                    Comparator.comparing(Class::getCanonicalName))
            ));

        // order matters: instance_of / is_class -> has_property -> contains_property
        register(DataFiles.BLOCK_STATE_PREDICATES, () -> Codec.unboundedMap(
                SourceCodecs.CLASS_NAMED, ExtraCodecs.compactListCodec(BlockPredicate.CODEC, ExtraCodecs.nonEmptyList(BlockPredicate.CODEC.listOf()))
            ),
            (path, codec) -> new DataFile.Map<>(
                path, codec, SliceResult::empty
            ));

        register(DataFiles.ITEM_META_BRIDGE, () -> Codec.unboundedMap(SourceCodecs.CLASS_NAMED, ItemMetaData.CODEC),
            (path, codec) -> new DataFile.Map<>(
                path, codec, sortedMap(Comparator.comparing(ClassNamed::canonicalName))
            ));

        // order matters
        register(DataFiles.ITEM_META_PREDICATES, () -> Codec.unboundedMap(
                SourceCodecs.CLASS_NAMED, ExtraCodecs.nonEmptyList(ItemPredicate.CODEC.listOf())
            ),
            (path, codec) -> new DataFile.Map<>(
                path, codec, SliceResult::empty, true
            ));

        register(DataFiles.ENTITY_TYPES, () -> Codec.unboundedMap(ResourceKey.codec(Registries.ENTITY_TYPE), EntityTypeData.CODEC),
            (path, codec) -> new DataFile.Map<>(
                path, codec,
                transmuteMap(() -> BuiltInRegistries.ENTITY_TYPE.listElementIds().collect(Collectors.toSet()),
                    missingType -> {
                        Class<?> genericType = ENTITY_TYPE_GENERICS.get().get(missingType);

                        String packageName = BasePackage.BUKKIT.name().concat(".entity");
                        if (AbstractBoat.class.isAssignableFrom(genericType)) {
                            packageName += ".boat";
                        } else if (AbstractMinecart.class.isAssignableFrom(genericType)) {
                            packageName += ".minecart";
                        }

                        return new EntityTypeData(ClassNamed.of(packageName, genericType.getSimpleName()));
                    },
                    Formatting.alphabeticKeyOrder(key -> key.location().getPath()))
            ));

        register(DataFiles.ENTITY_CLASS_NAMES, () -> Codec.unboundedMap(SourceCodecs.classCodec(Mob.class), EntityClassData.CODEC),
            (path, codec) -> new DataFile.Map<>(
                path, codec,
                transmuteMap(() -> {
                        try (ScanResult scanResult = new ClassGraph().enableClassInfo().acceptPackages(Entity.class.getPackageName()).scan()) {
                            Set<Class<? extends Mob>> classes = new HashSet<>(scanResult.getSubclasses(Mob.class.getName()).loadClasses(Mob.class));
                            if (classes.isEmpty()) {
                                throw new IllegalStateException("There are supposed to be more than 0 mob classes!");
                            }

                            classes.add(Mob.class);
                            return Collections.unmodifiableSet(classes);
                        }
                    },
                    missingType -> new EntityClassData(BasePackage.BUKKIT.relativeClass("entity", missingType.getSimpleName())),
                    Comparator.comparing(Class::getCanonicalName))
            ));
    }

    public static <V, A, R> V get(ResourceKey<? extends DataFile<V, A, R>> key) {
        return ((DataFile<V, A, R>) DATA_FILES.get(key)).get();
    }

    private static <V, A, R> void register(ResourceKey<? extends DataFile<V, A, R>> key, Supplier<Codec<V>> codec, BiFunction<String, Codec<V>, DataFile<V, A, R>> maker) {
        register(key, path -> maker.apply(path, Codec.lazyInitialized(codec)));
    }

    private static <F extends DataFile<?, ?, ?>> F register(ResourceKey<? extends F> key, Function<String, F> maker) {
        F file = maker.apply("data/%s.json".formatted(key.location().getPath()));
        DATA_FILES.put(key, file);
        return file;
    }
}
