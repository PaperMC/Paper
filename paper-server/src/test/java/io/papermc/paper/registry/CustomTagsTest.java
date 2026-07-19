package io.papermc.paper.registry;

import com.destroystokyo.paper.MaterialTags;
import com.google.common.collect.Sets;
import io.papermc.paper.generator.BlockItemTags;
import io.papermc.paper.generator.BlockTags;
import io.papermc.paper.generator.EntityTypeTags;
import io.papermc.paper.generator.ItemTags;
import io.papermc.paper.registry.keys.tags.PaperBlockTypeTagKeys;
import io.papermc.paper.registry.keys.tags.PaperEntityTypeTagKeys;
import io.papermc.paper.registry.keys.tags.PaperItemTypeTagKeys;
import io.papermc.paper.tag.BaseTag;
import io.papermc.paper.tag.EntityTags;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.BaseCoralFanBlock;
import net.minecraft.world.level.block.BaseCoralPlantBlock;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.BaseTorchBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.CoralPlantBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.jetbrains.annotations.ApiStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@AllFeatures
public class CustomTagsTest {

    //<editor-fold desc="Legacy tags" defaultstate="collapsed">
    @BeforeAll
    public static void testInitialize() {
        assertDoesNotThrow(() -> Class.forName(MaterialTags.class.getName()));
        assertDoesNotThrow(() -> Class.forName(EntityTags.class.getName()));
    }

    @ParameterizedTest
    @MethodSource("tags")
    public void testLocked(BaseTag<?, ?> tag) {
        assertTrue(tag.isLocked(), "Tag " + tag.key() + " is not locked");
    }

    public static Set<BaseTag<?, ?>> tags() {
        Set<BaseTag<?, ?>> tags = new HashSet<>();
        collectTags(tags, MaterialTags.class);
        collectTags(tags, EntityTags.class);
        return tags;
    }

    private static void collectTags(Set<BaseTag<?, ?>> into, Class<?> clazz) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && BaseTag.class.isAssignableFrom(field.getType())) {
                    into.add((BaseTag<?, ?>) field.get(null));
                }
            }
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }
    //</editor-fold>

    // registry tags
    public record TagPredicate<E>(TagKey<E> tagKey, Predicate<Holder.Reference<E>> filter) {
    }

    private static <E> TagPredicate<E> tagPredicate(TagKey<E> tagKey, Predicate<E> filter) {
        return new TagPredicate<>(tagKey, reference -> filter.test(reference.value()));
    }

    @ApiStatus.Obsolete
    private static <E> TagPredicate<E> legacyPredicate(TagKey<E> tagKey, Predicate<String> filter) {
        return new TagPredicate<>(tagKey, reference -> filter.test(reference.key().identifier().getPath()));
    }

    @ApiStatus.Obsolete
    private static <E> TagPredicate<E> legacyPredicate(TagKey<E> tagKey, BiPredicate<String, E> filter) {
        return new TagPredicate<>(tagKey, reference -> filter.test(reference.key().identifier().getPath(), reference.value()));
    }

    public static Set<TagPredicate<?>> tagPredicates() {
        Predicate<Block> torchFilter = block -> block instanceof BaseTorchBlock;
        Predicate<Block> pistonFilter = block -> block instanceof PistonBaseBlock || block instanceof PistonHeadBlock || block instanceof MovingPistonBlock;
        Predicate<Block> coralFanFilter = block -> block instanceof BaseCoralFanBlock;
        return Set.of(
            tagPredicate(BlockItemTags.FURNACES.block(), block -> block instanceof AbstractFurnaceBlock),
            tagPredicate(BlockItemTags.STAINED_GLASS.block(), block -> block instanceof StainedGlassBlock),
            tagPredicate(BlockItemTags.STAINED_GLASS_PANE.block(), block -> block instanceof StainedGlassPaneBlock),
            tagPredicate(BlockItemTags.COMMAND_BLOCKS.block(), block -> block instanceof CommandBlock),
            tagPredicate(BlockItemTags.MUSHROOM_BLOCKS.block(), block -> block instanceof HugeMushroomBlock),
            tagPredicate(BlockItemTags.MUSHROOMS.block(), block -> block instanceof MushroomBlock),
            tagPredicate(BlockItemTags.PISTONS.block(), pistonFilter),
            tagPredicate(BlockItemTags.CORAL.block(), block -> block instanceof CoralPlantBlock || block instanceof BaseCoralPlantBlock),
            tagPredicate(BlockItemTags.CORAL_FAN.block(), coralFanFilter),
            tagPredicate(BlockTags.SKULLS, block -> block instanceof AbstractSkullBlock),
            tagPredicate(BlockTags.TORCHES, torchFilter),
            tagPredicate(BlockTags.REDSTONE_TORCH, block -> block instanceof RedstoneTorchBlock),

            tagPredicate(ItemTags.HORSE_ARMORS,
                item -> item.components().has(DataComponents.EQUIPPABLE) && item.components().get(DataComponents.EQUIPPABLE).equipSound().equals(SoundEvents.HORSE_ARMOR)
            ),
            tagPredicate(ItemTags.MUSIC_DISCS, item -> item.components().has(DataComponents.JUKEBOX_PLAYABLE)),
            tagPredicate(ItemTags.SPAWN_EGGS, item -> item instanceof SpawnEggItem),
            tagPredicate(ItemTags.TORCHES, item -> item instanceof BlockItem blockItem && torchFilter.test(blockItem.getBlock())),
            tagPredicate(ItemTags.PRESSURE_PLATES, item -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof BasePressurePlateBlock),
            tagPredicate(BlockItemTags.PISTONS.item(), item -> item instanceof BlockItem blockItem && pistonFilter.test(blockItem.getBlock())),
            tagPredicate(BlockItemTags.CORAL_FAN.item(), item -> item instanceof BlockItem blockItem && coralFanFilter.test(blockItem.getBlock())),

            // legacy test (taken from MaterialTags/EntityTags), should be replaced by more robust check if possible
            legacyPredicate(BlockItemTags.COBBLESTONE_WALL.block(), key -> key.endsWith("cobblestone_wall")),
            legacyPredicate(BlockItemTags.GLASS.block(), (key, block) -> key.endsWith("_glass") || block == Blocks.GLASS),
            legacyPredicate(BlockItemTags.GLASS_PANE.block(), key -> key.endsWith("glass_pane")),
            legacyPredicate(BlockItemTags.STAINED_TERRACOTTA.block(), (key, block) -> {
                return key.endsWith("terracotta") && block != Blocks.TERRACOTTA && !key.endsWith("glazed_terracotta");
            }),
            legacyPredicate(BlockItemTags.TERRACOTTA.block(), key -> key.endsWith("terracotta")),
            legacyPredicate(BlockItemTags.INFESTED_BLOCKS.block(), key -> key.startsWith("infested_")),
            legacyPredicate(BlockItemTags.RED_SANDSTONES.block(), key -> key.endsWith("red_sandstone")),
            legacyPredicate(BlockItemTags.SANDSTONES.block(), key -> key.endsWith("sandstone") && !key.endsWith("red_sandstone")),
            legacyPredicate(BlockItemTags.SPONGE.block(), key -> key.endsWith("sponge")),
            legacyPredicate(BlockItemTags.PURPUR.block(), key -> key.startsWith("purpur_")),
            legacyPredicate(BlockItemTags.CORAL_BLOCKS.block(), key -> key.endsWith("_coral_block")), // could be done if CoralBlock#deadBlock was exposed
            legacyPredicate(BlockItemTags.DEEPSLATE_ORES.block(), key -> key.startsWith("deepslate_") && key.endsWith("_ore")),
            legacyPredicate(BlockItemTags.OXIDIZED_COPPER_BLOCKS.block(), key -> key.startsWith("oxidized_") || key.startsWith("waxed_oxidized_")),
            legacyPredicate(BlockItemTags.WEATHERED_COPPER_BLOCKS.block(), key -> key.startsWith("weathered_") || key.startsWith("waxed_weathered_")),
            legacyPredicate(BlockItemTags.EXPOSED_COPPER_BLOCKS.block(), key -> key.startsWith("exposed_") || key.startsWith("waxed_exposed_")),
            legacyPredicate(BlockItemTags.UNAFFECTED_COPPER_BLOCKS.block(), (key, block) -> {
                return (key.startsWith("cut_copper") || key.startsWith("waxed_cut_copper") ||
                    key.startsWith("waxed_copper_") || key.startsWith("copper_") ||
                    block == Blocks.CHISELED_COPPER.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED) ||
                    block == Blocks.CHISELED_COPPER.waxed().pick(WeatheringCopper.WeatherState.UNAFFECTED)) &&
                    block != Blocks.COPPER_ORE;
            }),
            legacyPredicate(BlockItemTags.WAXED_COPPER_BLOCKS.block(), key -> key.startsWith("waxed_") && key.contains("copper")),
            legacyPredicate(BlockItemTags.UNWAXED_COPPER_BLOCKS.block(), (key, block) -> {
                return key.startsWith("exposed_") || key.startsWith("weathered_") ||
                    key.startsWith("oxidized_") || key.startsWith("cut_copper") ||
                    block == Blocks.COPPER_BLOCK.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED) ||
                    block == Blocks.CHISELED_COPPER.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED) ||
                    block == Blocks.COPPER_DOOR.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED) ||
                    block == Blocks.COPPER_TRAPDOOR.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED) ||
                    block == Blocks.COPPER_GRATE.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED) ||
                    block == Blocks.COPPER_BULB.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED) ||
                    block == Blocks.COPPER_BARS.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED) ||
                    block == Blocks.COPPER_TORCH ||
                    block == Blocks.COPPER_CHEST.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED) ||
                    block == Blocks.COPPER_CHAIN.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED) ||
                    block == Blocks.COPPER_WALL_TORCH ||
                    block == Blocks.COPPER_LANTERN.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED) ||
                    block == Blocks.COPPER_GOLEM_STATUE.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED);
            }),
            legacyPredicate(BlockItemTags.FULL_COPPER_BLOCKS.block(), (key, block) -> {
                return key.endsWith("oxidized_copper") || key.endsWith("weathered_copper") ||
                    key.endsWith("exposed_copper") || key.endsWith("copper_block") &&
                    block != Blocks.RAW_COPPER_BLOCK;
            }),
            legacyPredicate(BlockItemTags.CUT_COPPER_BLOCKS.block(), key -> key.endsWith("cut_copper")),
            legacyPredicate(BlockItemTags.CUT_COPPER_STAIRS.block(), key -> key.endsWith("cut_copper_stairs")),
            legacyPredicate(BlockItemTags.CUT_COPPER_SLABS.block(), key -> key.endsWith("cut_copper_slab")),

            legacyPredicate(ItemTags.BUCKET, key -> key.endsWith("bucket")),
            legacyPredicate(ItemTags.GOLDEN_APPLE, key -> key.endsWith("golden_apple")),
            legacyPredicate(ItemTags.POTATO, key -> key.endsWith("potato")),
            legacyPredicate(BlockItemTags.UNAFFECTED_COPPER_BLOCKS.item(), (key, item) -> {
                return (key.startsWith("cut_copper") || key.startsWith("waxed_cut_copper") ||
                    key.startsWith("waxed_copper_") || key.startsWith("copper_") ||
                    item == Items.CHISELED_COPPER.weathering().pick(WeatheringCopper.WeatherState.UNAFFECTED) ||
                    item == Items.CHISELED_COPPER.waxed().pick(WeatheringCopper.WeatherState.UNAFFECTED)
                ) && item != Items.COPPER_INGOT && item != Items.COPPER_ORE &&
                    item != Items.COPPER_NUGGET && item != Items.COPPER_HORSE_ARMOR &&
                    item != Items.COPPER_GOLEM_SPAWN_EGG && item != Items.COPPER_HELMET &&
                    item != Items.COPPER_CHESTPLATE && item != Items.COPPER_LEGGINGS &&
                    item != Items.COPPER_BOOTS && item != Items.COPPER_AXE &&
                    item != Items.COPPER_HOE && item != Items.COPPER_PICKAXE &&
                    item != Items.COPPER_SHOVEL && item != Items.COPPER_SWORD &&
                    item != Items.COPPER_NAUTILUS_ARMOR && item != Items.COPPER_SPEAR;
            }),

            legacyPredicate(EntityTypeTags.HORSES, key -> key.endsWith("horse")),
            legacyPredicate(EntityTypeTags.MINECART, key -> key.endsWith("minecart"))
        );
    }

    @ParameterizedTest
    @MethodSource("tagPredicates")
    public <E> void testRegistryTag(TagPredicate<E> predicate) {
        net.minecraft.core.Registry<E> registry = RegistryHelper.registryAccess().lookupOrThrow(predicate.tagKey().registry());
        Set<Identifier> expectedEntries = registry.listElements()
            .filter(predicate.filter())
            .map(reference -> reference.key().identifier())
            .collect(Collectors.toUnmodifiableSet());

        Optional<HolderSet.Named<E>> tag = registry.get(predicate.tagKey());
        assumeTrue(tag.isPresent());
        Set<Identifier> currentEntries = tag.get().stream().map(holder -> holder.unwrapKey().orElseThrow().identifier()).collect(Collectors.toUnmodifiableSet());

        if (!expectedEntries.equals(currentEntries)) {
            Set<Identifier> missedEntries = Sets.difference(expectedEntries, currentEntries);
            Set<Identifier> extraEntries = Sets.difference(currentEntries, expectedEntries);
            assertEquals(Set.of(), missedEntries, "Tag '%s' is outdated, missed content".formatted(predicate.tagKey()));
            assertEquals(Set.of(), extraEntries, "Tag '%s' is outdated, extra content".formatted(predicate.tagKey()));
        }
    }

    public static Set<Arguments> fieldHolders() {
        return Set.of(
            arguments(PaperBlockTypeTagKeys.class, BlockTags.class),
            arguments(PaperItemTypeTagKeys.class, ItemTags.class),
            arguments(PaperEntityTypeTagKeys.class, EntityTypeTags.class)
        );
    }

    @ParameterizedTest
    @MethodSource("fieldHolders")
    public void testExposedFields(Class<?> holder, Class<?> internalHolder) {
        Set<String> fields = new HashSet<>();
        Set<String> internalFields = new HashSet<>();
        fetchFields(fields, holder);
        fetchFields(internalFields, internalHolder);

        Set<String> extraFields = Sets.difference(fields, internalFields);
        Set<String> missingFields = Sets.difference(internalFields, fields);
        assertTrue(missingFields.isEmpty(), "Some fields are missing in %s: %s".formatted(holder.getCanonicalName(), missingFields));
        assertTrue(extraFields.isEmpty(), "Found some extra fields in %s: %s".formatted(holder.getCanonicalName(), extraFields));
    }

    private static void fetchFields(Set<String> output, Class<?> holder) {
        for (Field field : holder.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                output.add(field.getName());
            }
        }
        Class<?> parent = holder.getSuperclass();
        if (parent != null) {
            fetchFields(output, parent);
        }
    }
}
