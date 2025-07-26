package io.papermc.paper.registry;

import com.destroystokyo.paper.MaterialTags;
import com.google.common.collect.Sets;
import io.papermc.paper.generator.BlockTags;
import io.papermc.paper.generator.ItemTags;
import io.papermc.paper.tag.BaseTag;
import io.papermc.paper.tag.EntityTags;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.BaseTorchBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AllFeatures
public class CustomTagsTest {

    // legacy
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

    // registry tags
    public static Set<TagKey<Block>> parentBlockTagKeyNonSymKey() {
        return Set.of(
            BlockTags.PISTONS,
            BlockTags.CORAL_FAN
        );
    }

    @ParameterizedTest
    @MethodSource("parentBlockTagKeyNonSymKey")
    public void testInheritTags(TagKey<Block> parentTagKey) {
        TagKey<Item> childKey = TagKey.create(Registries.ITEM, parentTagKey.location());
        HolderSet.Named<Block> parentTag = BuiltInRegistries.BLOCK.get(parentTagKey).orElseThrow();
        HolderSet.Named<Item> childTag = BuiltInRegistries.ITEM.get(childKey).orElseThrow();

        List<ResourceLocation> rawValues = parentTag.stream().map(holder -> holder.unwrapKey().orElseThrow().location()).toList();
        List<ResourceLocation> rawChildValues = childTag.stream().map(holder -> holder.unwrapKey().orElseThrow().location()).toList();
        assertTrue(rawValues.containsAll(rawChildValues), "Tag '%s' doesn't inherit all the values from %s".formatted(parentTagKey, childKey));
    }

    public record TagPredicate<E>(net.minecraft.tags.TagKey<E> tagKey, Predicate<E> filter) {
    }

    private static <E> TagPredicate<E> tagPredicate(net.minecraft.tags.TagKey<E> tagKey, Predicate<E> filter) {
        return new TagPredicate<>(tagKey, filter);
    }

    public static Set<TagPredicate<?>> tagPredicates() {
        return Set.of(
            tagPredicate(BlockTags.FURNACES, block -> block instanceof AbstractFurnaceBlock),
            tagPredicate(BlockTags.STAINED_GLASS, block -> block instanceof StainedGlassBlock),
            tagPredicate(BlockTags.STAINED_GLASS_PANE, block -> block instanceof StainedGlassPaneBlock),
            tagPredicate(BlockTags.TORCHES, block -> block instanceof BaseTorchBlock),
            tagPredicate(BlockTags.REDSTONE_TORCH, block -> block instanceof RedstoneTorchBlock),
            tagPredicate(BlockTags.COMMAND_BLOCKS, block -> block instanceof CommandBlock),
            tagPredicate(BlockTags.SKULLS, block -> block instanceof AbstractSkullBlock),

            tagPredicate(ItemTags.CONCRETE_POWDER, item -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ConcretePowderBlock),
            tagPredicate(ItemTags.DYE, item -> item instanceof DyeItem),
            tagPredicate(ItemTags.HORSE_ARMORS,
                item -> item.components().has(DataComponents.EQUIPPABLE) && item.components().get(DataComponents.EQUIPPABLE).equipSound().equals(SoundEvents.HORSE_ARMOR)
            ),
            tagPredicate(ItemTags.MUSIC_DISCS, item -> item.components().has(DataComponents.JUKEBOX_PLAYABLE)),
            tagPredicate(ItemTags.SPAWN_EGGS, item -> item instanceof SpawnEggItem),
            tagPredicate(ItemTags.TORCHES, item -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof BaseTorchBlock),
            tagPredicate(ItemTags.PRESSURE_PLATES, item -> item instanceof BlockItem blockItem && blockItem.getBlock() instanceof BasePressurePlateBlock)
        );
    }

    @ParameterizedTest
    @MethodSource("tagPredicates")
    public <E> void testRegistryTag(TagPredicate<E> predicate) {
        net.minecraft.core.Registry<E> registry = RegistryHelper.getRegistry().lookupOrThrow(predicate.tagKey().registry());
        Set<ResourceLocation> expectedEntries = registry.listElements()
            .filter(reference -> predicate.filter().test(reference.value()))
            .map(reference -> reference.key().location())
            .collect(Collectors.toUnmodifiableSet());

        HolderSet.Named<E> tag = registry.get(predicate.tagKey()).orElseThrow();
        Set<ResourceLocation> currentEntries = tag.stream().map(holder -> holder.unwrapKey().orElseThrow().location()).collect(Collectors.toUnmodifiableSet());

        if (!expectedEntries.equals(currentEntries)) {
            Set<ResourceLocation> missedEntries = Sets.difference(expectedEntries, currentEntries);
            Set<ResourceLocation> extraEntries = Sets.difference(currentEntries, expectedEntries);
            assertEquals(Set.of(), missedEntries, "Tag '%s' is outdated, missed content".formatted(predicate.tagKey()));
            assertEquals(Set.of(), extraEntries, "Tag '%s' is outdated, extra content".formatted(predicate.tagKey()));
        }
    }
}
