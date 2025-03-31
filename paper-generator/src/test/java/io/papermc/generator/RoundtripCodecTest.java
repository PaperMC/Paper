package io.papermc.generator;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JavaOps;
import com.mojang.serialization.JsonOps;
import io.papermc.generator.registry.RegistryEntry;
import io.papermc.generator.resources.DataFile;
import io.papermc.generator.resources.DataFileLoader;
import io.papermc.generator.resources.DataFiles;
import io.papermc.generator.resources.ParameterizedClass;
import io.papermc.generator.resources.data.EntityClassData;
import io.papermc.generator.resources.data.EntityTypeData;
import io.papermc.generator.resources.data.ItemMetaData;
import io.papermc.generator.resources.data.RegistryData;
import io.papermc.generator.resources.predicate.BlockPredicate;
import io.papermc.generator.resources.predicate.BlockPropertyPredicate;
import io.papermc.generator.resources.predicate.ItemPredicate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.vault.VaultState;
import net.minecraft.world.level.block.state.properties.CreakingHeartState;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static io.papermc.generator.utils.BasePackage.BUKKIT;
import static io.papermc.generator.utils.BasePackage.CRAFT_BUKKIT;
import static io.papermc.generator.utils.BasePackage.PAPER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoundtripCodecTest extends BootstrapTest {

    private static Multimap<ResourceKey<? extends DataFile<?, ?, ?>>, ?> values() {
        Random random = new Random();
        RandomSource randomSource = RandomSource.create();
        RandomStringUtils randomStr = RandomStringUtils.insecure();
        return Util.make(MultimapBuilder.hashKeys().arrayListValues().build(), map -> {
            put(map, DataFiles.BLOCK_STATE_AMBIGUOUS_NAMES, Map.of("Test", List.of("CraftA")));
            put(map, DataFiles.BLOCK_STATE_AMBIGUOUS_NAMES, Map.of("Test", List.of("CraftA", "CraftA")));
            put(map, DataFiles.BLOCK_STATE_AMBIGUOUS_NAMES, Map.of("Test", List.of("CraftA", "CraftB")));
            put(map, DataFiles.BLOCK_STATE_AMBIGUOUS_NAMES, Map.of(randomStr.nextAlphabetic(5), List.of(randomStr.nextAlphabetic(10))));

            put(map, DataFiles.BLOCK_STATE_ENUM_PROPERTY_TYPES, Map.of(VaultState.class, BUKKIT.rootClass("Vault", "State")));
            put(map, DataFiles.BLOCK_STATE_ENUM_PROPERTY_TYPES, Map.of(CreakingHeartState.class, BUKKIT.rootClass("CreakingHeartState")));
            put(map, DataFiles.BLOCK_STATE_ENUM_PROPERTY_TYPES, Map.of(CreakingHeartState.class, BUKKIT.rootClass(randomStr.nextAlphabetic(5))));

            put(map, DataFiles.BLOCK_STATE_PREDICATES, Map.of(BUKKIT.rootClassNamed("BlockData"), List.of(
                new BlockPredicate.IsClassPredicate(VaultBlock.class),
                new BlockPredicate.InstanceOfPredicate(FlowerPotBlock.class, List.of()),
                new BlockPredicate.InstanceOfPredicate(BaseRailBlock.class, List.of(
                    new BlockPropertyPredicate.IsFieldPredicate("POWERED"),
                    new BlockPropertyPredicate.IsNamePredicate("hatch")
                )),
                new BlockPredicate.ContainsPropertyPredicate(List.of(
                    new BlockPropertyPredicate.IsFieldPredicate("POWERED"),
                    new BlockPropertyPredicate.IsNamePredicate("hatch")
                ), 1, BlockPredicate.ContainsPropertyPredicate.Strategy.AT_LEAST),
                new BlockPredicate.ContainsPropertyPredicate(List.of(
                    new BlockPropertyPredicate.IsFieldPredicate("DUSTED"),
                    new BlockPropertyPredicate.IsNamePredicate("age")
                ), 2, BlockPredicate.ContainsPropertyPredicate.Strategy.AT_LEAST),
                new BlockPredicate.ContainsPropertyPredicate(List.of(
                    new BlockPropertyPredicate.IsFieldPredicate("POWER"),
                    new BlockPropertyPredicate.IsNamePredicate("level")
                ), 2, BlockPredicate.ContainsPropertyPredicate.Strategy.EXACT),
                new BlockPredicate.ContainsPropertyPredicate(List.of(
                    new BlockPropertyPredicate.IsFieldPredicate("FACING"),
                    new BlockPropertyPredicate.IsNamePredicate("hatch"),
                    new BlockPropertyPredicate.IsNamePredicate("age")
                ), random.nextInt(3) + 1, Util.getRandom(BlockPredicate.ContainsPropertyPredicate.Strategy.values(), randomSource))
            )));

            put(map, DataFiles.ITEM_META_BRIDGE, Map.of(
                CRAFT_BUKKIT.rootClassNamed("CraftSomethingMeta"), new ItemMetaData(
                    BUKKIT.rootClassNamed("SomethingMeta"), "SOMETHING_DATA"
                ),
                CRAFT_BUKKIT.rootClassNamed(randomStr.nextAlphabetic(10)), new ItemMetaData(
                    BUKKIT.rootClassNamed(randomStr.nextAlphabetic(5)), randomStr.nextAlphabetic(10).toUpperCase(Locale.ROOT)
                )
            ));

            put(map, DataFiles.ITEM_META_PREDICATES, Map.of(
                CRAFT_BUKKIT.rootClassNamed("CraftSomethingMeta"), List.of(
                    new ItemPredicate.InstanceOfPredicate(StandingAndWallBlockItem.class, false),
                    new ItemPredicate.InstanceOfPredicate(EntityBlock.class, true),
                    new ItemPredicate.IsClassPredicate(BedItem.class, false),
                    new ItemPredicate.IsClassPredicate(BellBlock.class, true),
                    new ItemPredicate.IsElementPredicate(Either.left(ItemTags.ANVIL)),
                    new ItemPredicate.IsElementPredicate(Either.right(BuiltInRegistries.ITEM.wrapAsHolder(Items.APPLE))),
                    new ItemPredicate.IsElementPredicate(Either.right(BuiltInRegistries.ITEM.getRandom(randomSource).orElseThrow()))
                )
            ));

            put(map, DataFiles.registry(RegistryEntry.Type.BUILT_IN), Map.of(
                Registries.GAME_EVENT,
                new RegistryData(
                    new RegistryData.Api(BUKKIT.rootClassNamed("GameEvent")),
                    new RegistryData.Impl(CRAFT_BUKKIT.rootClassNamed("CraftGameEvent")),
                    Optional.empty(),
                    Optional.empty(),
                    false
                ),
                Registries.BLOCK_ENTITY_TYPE,
                new RegistryData(
                    new RegistryData.Api(
                        new RegistryData.Api.Class(
                            BUKKIT.rootClassNamed("BlockEntityType"),
                            List.of(new ParameterizedClass(BUKKIT.rootClassNamed("SuperClass"))),
                            false
                        ),
                        Optional.of(new RegistryData.Api.HolderClass(BUKKIT.rootClassNamed("BlockEntityTypes"))),
                        true,
                        Optional.of("BLOCK_ENTITY_TYPE")
                    ),
                    new RegistryData.Impl(CRAFT_BUKKIT.rootClassNamed("CraftBlockEntityType"), "of", true),
                    Optional.of(
                        new RegistryData.Builder(
                            PAPER.rootClassNamed("BlockEntityTypeRegistryEntry", "Builder"),
                            PAPER.rootClassNamed("PaperBlockEntityTypeRegistryEntry", "PaperBuilder"),
                            List.of(),
                            RegistryData.Builder.RegisterCapability.WRITABLE
                        )
                    ),
                    Optional.of("BLOCK_ENTITY_TYPE_RENAME"),
                    true
                ),
                Registries.ATTRIBUTE,
                new RegistryData(
                    new RegistryData.Api(
                        new RegistryData.Api.Class(
                            BUKKIT.rootClassNamed("Attribute"),
                            List.of(new ParameterizedClass(BUKKIT.rootClassNamed("SuperClass"),
                                List.of(
                                    new ParameterizedClass(BUKKIT.rootClassNamed("SuperClass2"))
                                )
                            )),
                            random.nextBoolean()
                        ),
                        Optional.of(new RegistryData.Api.HolderClass(
                            Optional.of(BUKKIT.rootClassNamed("Attributes")), random.nextBoolean())
                        ),
                        random.nextBoolean(),
                        Optional.of("ATTRIBUTE")
                    ),
                    new RegistryData.Impl(CRAFT_BUKKIT.rootClassNamed("CraftAttribute"), "of", random.nextBoolean()),
                    Optional.of(
                        new RegistryData.Builder(
                            PAPER.rootClassNamed("AttributeRegistryEntry", "Builder"),
                            PAPER.rootClassNamed("PaperAttributeRegistryEntry", "PaperBuilder"),
                            List.of(),
                            Util.getRandom(RegistryData.Builder.RegisterCapability.values(), randomSource)
                        )
                    ),
                    Optional.of("ATTRIBUTE_RENAME"),
                    random.nextBoolean()
                )
            ));

            put(map, DataFiles.registry(RegistryEntry.Type.DATA_DRIVEN), Map.of(
                Registries.DATA_COMPONENT_PREDICATE_TYPE,
                new RegistryData(
                    new RegistryData.Api(PAPER.rootClassNamed("DataComponentPredicate", "Type")),
                    new RegistryData.Impl(PAPER.rootClassNamed("CraftDataComponentPredicate", "CraftType")),
                    Optional.empty(),
                    Optional.empty(),
                    false
                )
            ));

            put(map, DataFiles.ENTITY_TYPES, Map.of(
                BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityType.HUSK).orElseThrow(),
                new EntityTypeData(BUKKIT.rootClassNamed("Husk"))
            ));
            put(map, DataFiles.ENTITY_TYPES, Map.of(
                BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityType.ZOGLIN).orElseThrow(),
                new EntityTypeData(BUKKIT.rootClassNamed("Zoglin"), -1)
            ));
            put(map, DataFiles.ENTITY_TYPES, Map.of(
                BuiltInRegistries.ENTITY_TYPE.getResourceKey(EntityType.END_CRYSTAL).orElseThrow(),
                new EntityTypeData(BUKKIT.rootClassNamed("EndCrystal"), 5)
            ));
            put(map, DataFiles.ENTITY_TYPES, Map.of(
                BuiltInRegistries.ENTITY_TYPE.getRandom(randomSource).orElseThrow().key(),
                new EntityTypeData(BUKKIT.rootClassNamed(randomStr.nextAlphabetic(5)), randomSource.nextIntBetweenInclusive(-1, 100))
            ));

            put(map, DataFiles.ENTITY_CLASS_NAMES, Map.of(
                Zombie.class,
                new EntityClassData(BUKKIT.rootClass("Zombie"))
            ));
            put(map, DataFiles.ENTITY_CLASS_NAMES, Map.of(
                Skeleton.class,
                new EntityClassData(BUKKIT.rootClass("Skeleton"), true)
            ));
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <V> void put(Multimap map, ResourceKey<? extends DataFile<V, ?, ?>> key, V value) {
        map.put(key, value);
    }

    public static Stream<Arguments> data() {
        Set<DynamicOps<?>> ops = Set.of(
            JavaOps.INSTANCE,
            JsonOps.INSTANCE
        );
        Multimap<ResourceKey<? extends DataFile<?, ?, ?>>, ?> values = values();

        return DataFileLoader.DATA_FILES_VIEW.entrySet().stream()
            .flatMap(entry -> ops.stream().flatMap(op ->
                values.get(entry.getKey()).stream().map(v -> Arguments.of(entry.getValue().getRequiredOps(op), entry.getValue().codec(), v))));
    }

    @ParameterizedTest
    @MethodSource("data")
    public <T, V> void testCodec(DynamicOps<T> ops, Codec<V> codec, V value) {
        DataResult<T> encoded = codec.encodeStart(ops, value);
        DataResult<V> decoded = encoded.flatMap(r -> codec.parse(ops, r));
        assertEquals(DataResult.success(value), decoded, "read(write(x)) == x");

        DataResult<T> reEncoded = decoded.flatMap(r -> codec.encodeStart(ops, r));
        assertEquals(encoded, reEncoded, "write(read(x)) == x");
    }
}
