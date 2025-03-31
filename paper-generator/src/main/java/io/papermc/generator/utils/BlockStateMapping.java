package io.papermc.generator.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Either;
import io.papermc.generator.Main;
import io.papermc.generator.resources.DataFileLoader;
import io.papermc.generator.resources.DataFiles;
import io.papermc.generator.resources.predicate.BlockPredicate;
import io.papermc.generator.types.craftblockdata.property.holder.VirtualField;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.util.ClassNamedView;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.TestBlock;
import net.minecraft.world.level.block.TestInstanceBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.Nullable;

public final class BlockStateMapping {

    public record BlockData(String implName, ClassNamed api,
                            Collection<? extends Property<?>> properties, Map<Property<?>, Field> propertyFields,
                            Multimap<Either<Field, VirtualField>, Property<?>> complexPropertyFields) {
    }

    public static final BiMap<Property<?>, Field> GENERIC_FIELDS;

    static {
        ImmutableBiMap.Builder<Property<?>, Field> genericFields = ImmutableBiMap.builder();
        fetchProperties(BlockStateProperties.class, (field, property) -> genericFields.put(property, field), null);
        GENERIC_FIELDS = genericFields.buildOrThrow();
    }

    public static final Map<Property<?>, String> GENERIC_FIELD_NAMES = BlockStateMapping.GENERIC_FIELDS.entrySet().stream().collect(Collectors.toUnmodifiableMap(
        Map.Entry::getKey, entry -> entry.getValue().getName()
    ));

    public static final Set<String> PROPERTY_NAMES = BlockStateMapping.GENERIC_FIELDS.keySet().stream()
        .map(Property::getName).collect(Collectors.toUnmodifiableSet());

    public static final Map<Class<? extends Block>, Collection<Property<?>>> STATEFUL_BLOCKS = Collections.unmodifiableMap(Util.make(new IdentityHashMap<>(), map -> {
        for (Block block : BuiltInRegistries.BLOCK) {
            if (!block.getStateDefinition().getProperties().isEmpty()) {
                map.put(block.getClass(), block.getStateDefinition().getProperties());
            }
        }
    }));

    private static Map<Class<? extends Block>, BlockData> create(Path rootDir) {
        class ExtraData {
            public static final Map<String, String> API_RENAMES = ImmutableMap.<String, String>builder()
                .put("SnowLayer", "Snow")
                .put("StainedGlassPane", "GlassPane") // weird that this one implements glass pane but not the regular glass pane
                .put("CeilingHangingSign", "HangingSign")
                .put("RedStoneWire", "RedstoneWire")
                .put("TripWire", "Tripwire")
                .put("TripWireHook", "TripwireHook")
                .put("Tnt", "TNT")
                .put("BambooStalk", "Bamboo")
                .put("Farm", "Farmland")
                .put("ChiseledBookShelf", "ChiseledBookshelf")
                .put("UntintedParticleLeaves", "Leaves")
                .put("TintedParticleLeaves", "Leaves")
                .put("StandingSign", "Sign")
                .put("FenceGate", "Gate")
                .buildOrThrow();

            public static final Set<Class<? extends Block>> BLOCK_SUFFIX_INTENDED = Set.of(
                CommandBlock.class,
                StructureBlock.class,
                NoteBlock.class,
                TestBlock.class,
                TestInstanceBlock.class
            );

            // virtual data that doesn't exist as constant in the source but still organized this way in the api
            public static final ImmutableMultimap<Class<?>, VirtualField> VIRTUAL_NODES = ImmutableMultimap.<Class<?>, VirtualField>builder()
                .build();
        }

        Map<Class<? extends Block>, BlockData> map = new IdentityHashMap<>();
        for (Map.Entry<Class<? extends Block>, Collection<Property<?>>> entry : STATEFUL_BLOCKS.entrySet()) {
            Class<? extends Block> statefulBlock = entry.getKey();

            Collection<Property<?>> properties = new ArrayList<>(entry.getValue());

            Map<Property<?>, Field> propertyFields = new HashMap<>(properties.size());
            Multimap<Either<Field, VirtualField>, Property<?>> complexPropertyFields = ArrayListMultimap.create();

            fetchProperties(statefulBlock, (field, property) -> {
                if (properties.contains(property)) {
                    propertyFields.put(property, field);
                }
            }, (field, property) -> {
                if (properties.remove(property)) { // handle those separately and only count if the property was in the state definition
                    complexPropertyFields.put(Either.left(field), property);
                }
            });

            // virtual nodes
            if (ExtraData.VIRTUAL_NODES.containsKey(statefulBlock)) {
                for (VirtualField virtualField : ExtraData.VIRTUAL_NODES.get(statefulBlock)) {
                    for (Property<?> property : virtualField.values()) {
                        if (properties.remove(property)) {
                            complexPropertyFields.put(Either.right(virtualField), property);
                        } else {
                            throw new IllegalStateException("Unhandled virtual node " + virtualField.name() + " for " + property + " in " + statefulBlock.getCanonicalName());
                        }
                    }
                }
            }

            String apiName = formatApiName(ExtraData.BLOCK_SUFFIX_INTENDED, statefulBlock);
            String implName = "Craft".concat(apiName); // before renames

            apiName = Formatting.stripInitialWord(apiName, "Base");
            apiName = ExtraData.API_RENAMES.getOrDefault(apiName, apiName);

            ClassNamedView view = new ClassNamedView(rootDir.resolve("paper-api/src/main/java"), 1, "org/bukkit/block/data/type");
            ClassNamed api = view.tryFindFirst("org/bukkit/block/data/type/" + apiName).or(() -> {
                Class<?> directParent = statefulBlock.getSuperclass();
                if (STATEFUL_BLOCKS.containsKey(directParent)) {
                    // if the properties are the same then always consider the parent
                    // check deeper in the tree?
                    if (STATEFUL_BLOCKS.get(directParent).equals(entry.getValue())) {
                        String parentApiName = formatApiName(ExtraData.BLOCK_SUFFIX_INTENDED, directParent);
                        parentApiName = Formatting.stripInitialWord(parentApiName, "Base");
                        parentApiName = ExtraData.API_RENAMES.getOrDefault(parentApiName, parentApiName);
                        return view.tryFindFirst("org/bukkit/block/data/type/" + parentApiName);
                    }
                }
                return Optional.empty();
            }).orElseGet(() -> {
                Set<Property<?>> propertySet = new HashSet<>(entry.getValue());
                for (Map.Entry<ClassNamed, List<BlockPredicate>> predicateEntry : DataFileLoader.get(DataFiles.BLOCK_STATE_PREDICATES).entrySet()) {
                    for (BlockPredicate predicate : predicateEntry.getValue()) {
                        if (predicate.matches(statefulBlock, propertySet)) {
                            return predicateEntry.getKey();
                        }
                    }
                }

                return null;
            });

            map.put(statefulBlock, new BlockData(implName, Objects.requireNonNull(api, () -> "Unknown block data for " + statefulBlock.getCanonicalName()), properties, propertyFields, complexPropertyFields));
        }
        return Collections.unmodifiableMap(map);
    }

    public static @MonotonicNonNull Map<Class<? extends Block>, BlockData> MAPPING;

    public static Map<Class<? extends Block>, BlockData> getOrCreate() {
        if (MAPPING == null) {
            MAPPING = create(Main.ROOT_DIR);
        }

        return MAPPING;
    }

    public static @Nullable ClassNamed getBestSuitedApiClass(Class<?> block) {
        if (!getOrCreate().containsKey(block)) {
            return null;
        }

        return getOrCreate().get(block).api();
    }

    public static String formatApiName(Set<Class<? extends Block>> withBlockSuffix, Class<?> specialBlock) {
        String apiName = specialBlock.getSimpleName();
        if (!withBlockSuffix.contains(specialBlock)) {
            return Formatting.stripInitialWord(apiName, "Block", true);
        }
        return apiName;
    }

    private static boolean handleComplexType(Field field, BiConsumer<Field, Property<?>> complexCallback) throws IllegalAccessException {
        if (field.getType().isArray() && Property.class.isAssignableFrom(field.getType().getComponentType())) {
            if (!field.trySetAccessible()) {
                return true;
            }

            for (Property<?> property : (Property<?>[]) field.get(null)) {
                complexCallback.accept(field, property);
            }
            return true;
        }
        if (Iterable.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType complexType) {
            Type[] args = complexType.getActualTypeArguments();
            if (args.length == 1 && Property.class.isAssignableFrom(ClassHelper.eraseType(args[0]))) {
                if (!field.trySetAccessible()) {
                    return true;
                }

                for (Property<?> property : (Iterable<Property<?>>) field.get(null)) {
                    complexCallback.accept(field, property);
                }
            }
            return true;
        }
        if (Map.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType complexType) {
            if (!field.trySetAccessible()) {
                return true;
            }

            Type[] args = complexType.getActualTypeArguments();
            if (args.length == 2 && Property.class.isAssignableFrom(ClassHelper.eraseType(args[1]))) {
                for (Property<?> property : ((Map<?, Property<?>>) field.get(null)).values()) {
                    complexCallback.accept(field, property);
                }
                return true;
            }
        }
        return false;
    }

    private static void fetchProperties(Class<?> block, BiConsumer<Field, Property<?>> simpleCallback, @Nullable BiConsumer<Field, Property<?>> complexCallback) {
        try {
            for (Field field : block.getDeclaredFields()) {
                if (ClassHelper.isStaticConstant(field, 0)) {
                    if (complexCallback != null && handleComplexType(field, complexCallback)) {
                        continue;
                    }

                    if (!Property.class.isAssignableFrom(field.getType())) {
                        continue;
                    }

                    if (field.trySetAccessible()) {
                        Property<?> property = ((Property<?>) field.get(null));
                        simpleCallback.accept(field, property);
                    }
                }
            }
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }

        if (block.isInterface()) {
            return;
        }

        // look deeper
        if (block.getSuperclass() != null && block.getSuperclass() != Block.class) {
            fetchProperties(block.getSuperclass(), simpleCallback, complexCallback);
        }
        for (Class<?> ext : block.getInterfaces()) {
            fetchProperties(ext, simpleCallback, complexCallback);
        }
    }
}
