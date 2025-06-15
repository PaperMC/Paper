package io.papermc.generator.registry;

import io.papermc.generator.utils.ClassHelper;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.BannerPatternRegistryEntry;
import io.papermc.paper.registry.data.CatTypeRegistryEntry;
import io.papermc.paper.registry.data.ChickenVariantRegistryEntry;
import io.papermc.paper.registry.data.CowVariantRegistryEntry;
import io.papermc.paper.registry.data.DamageTypeRegistryEntry;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.data.FrogVariantRegistryEntry;
import io.papermc.paper.registry.data.GameEventRegistryEntry;
import io.papermc.paper.registry.data.JukeboxSongRegistryEntry;
import io.papermc.paper.registry.data.PaintingVariantRegistryEntry;
import io.papermc.paper.registry.data.PigVariantRegistryEntry;
import io.papermc.paper.registry.data.SoundEventRegistryEntry;
import io.papermc.paper.registry.data.WolfVariantRegistryEntry;
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dialog.Dialogs;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.CatVariants;
import net.minecraft.world.entity.animal.ChickenVariants;
import net.minecraft.world.entity.animal.CowVariants;
import net.minecraft.world.entity.animal.PigVariants;
import net.minecraft.world.entity.animal.frog.FrogVariants;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariants;
import net.minecraft.world.entity.animal.wolf.WolfVariants;
import net.minecraft.world.entity.decoration.PaintingVariants;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Instruments;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.JukeboxSongs;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import org.bukkit.Art;
import org.bukkit.Fluid;
import org.bukkit.GameEvent;
import org.bukkit.JukeboxSong;
import org.bukkit.Keyed;
import org.bukkit.MusicInstrument;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockType;
import org.bukkit.block.banner.PatternType;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.map.MapCursor;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class RegistryEntries {

    // CraftBukkit entry where implementation start by "Craft"
    private static <T> RegistryEntry<T> entry(ResourceKey<? extends Registry<T>> registryKey, Class<?> holderElementsClass, Class<? extends Keyed> apiClass) {
        return entry(registryKey, holderElementsClass, apiClass, "Craft");
    }

    private static <T> RegistryEntry<T> entry(ResourceKey<? extends Registry<T>> registryKey, Class<?> holderElementsClass, Class<? extends Keyed> apiClass, String implPrefix) {
        String name = io.papermc.typewriter.util.ClassHelper.retrieveFullNestedName(apiClass);
        RegistryKeyField<T> registryKeyField = (RegistryKeyField<T>) REGISTRY_KEY_FIELDS.get(registryKey);
        String[] classes = name.split("\\.");
        if (classes.length == 0) {
            return new RegistryEntry<>(registryKey, registryKeyField, holderElementsClass, apiClass, implPrefix.concat(apiClass.getSimpleName()));
        }

        String implName = Arrays.stream(classes).map(implPrefix::concat).collect(Collectors.joining("."));
        return new RegistryEntry<>(registryKey, registryKeyField, holderElementsClass, apiClass, implName);
    }

    @Deprecated
    private static <T> RegistryEntry<T> inconsistentEntry(ResourceKey<? extends Registry<T>> registryKey, Class<?> holderElementsClass, Class<? extends Keyed> apiClass, String implClass) {
        return new RegistryEntry<>(registryKey, (RegistryKeyField<T>) REGISTRY_KEY_FIELDS.get(registryKey), holderElementsClass, apiClass, implClass);
    }

    private static final Map<ResourceKey<? extends Registry<?>>, RegistryKeyField<?>> REGISTRY_KEY_FIELDS;
    static {
        Map<ResourceKey<? extends Registry<?>>, RegistryKeyField<?>> registryKeyFields = new IdentityHashMap<>();
        try {
            for (Field field : Registries.class.getDeclaredFields()) {
                if (!ResourceKey.class.isAssignableFrom(field.getType())) {
                    continue;
                }

                if (ClassHelper.isStaticConstant(field, Modifier.PUBLIC)) {
                    Type elementType = ClassHelper.getNestedTypeParameter(field.getGenericType(), ResourceKey.class, Registry.class, null);
                    if (elementType != null) {
                        registryKeyFields.put(((ResourceKey<? extends Registry<?>>) field.get(null)), new RegistryKeyField<>(ClassHelper.eraseType(elementType), field.getName()));
                    }
                }
            }
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
        REGISTRY_KEY_FIELDS = Collections.unmodifiableMap(registryKeyFields);
    }

    public static final Set<Class<?>> REGISTRY_CLASS_NAME_BASED_ON_API = Set.of(
        BlockType.class,
        ItemType.class
    );

    public static final List<RegistryEntry<?>> BUILT_IN = List.of(
        entry(Registries.GAME_EVENT, net.minecraft.world.level.gameevent.GameEvent.class, GameEvent.class).writableApiRegistryBuilder(GameEventRegistryEntry.Builder.class, "PaperGameEventRegistryEntry.PaperBuilder"),
        entry(Registries.STRUCTURE_TYPE, net.minecraft.world.level.levelgen.structure.StructureType.class, StructureType.class),
        entry(Registries.MOB_EFFECT, MobEffects.class, PotionEffectType.class),
        entry(Registries.BLOCK, Blocks.class, BlockType.class),
        entry(Registries.ITEM, Items.class, ItemType.class),
        entry(Registries.VILLAGER_PROFESSION, VillagerProfession.class, Villager.Profession.class),
        entry(Registries.VILLAGER_TYPE, VillagerType.class, Villager.Type.class),
        entry(Registries.MAP_DECORATION_TYPE, MapDecorationTypes.class, MapCursor.Type.class),
        entry(Registries.MENU, net.minecraft.world.inventory.MenuType.class, MenuType.class),
        entry(Registries.ATTRIBUTE, Attributes.class, Attribute.class),
        entry(Registries.FLUID, Fluids.class, Fluid.class),
        entry(Registries.SOUND_EVENT, SoundEvents.class, Sound.class).allowDirect().apiRegistryField("SOUNDS").apiRegistryBuilder(SoundEventRegistryEntry.Builder.class, "PaperSoundEventRegistryEntry.PaperBuilder", RegistryEntry.RegistryModificationApiSupport.NONE),
        entry(Registries.DATA_COMPONENT_TYPE, DataComponents.class, DataComponentType.class, "Paper").preload(DataComponentTypes.class).apiAccessName("of")
    );

    public static final List<RegistryEntry<?>> DATA_DRIVEN = List.of(
        entry(Registries.BIOME, Biomes.class, Biome.class).delayed(),
        entry(Registries.STRUCTURE, BuiltinStructures.class, Structure.class).delayed(),
        entry(Registries.TRIM_MATERIAL, TrimMaterials.class, TrimMaterial.class).allowDirect().delayed(),
        entry(Registries.TRIM_PATTERN, TrimPatterns.class, TrimPattern.class).allowDirect().delayed(),
        entry(Registries.DAMAGE_TYPE, DamageTypes.class, DamageType.class).writableApiRegistryBuilder(DamageTypeRegistryEntry.Builder.class, "PaperDamageTypeRegistryEntry.PaperBuilder").delayed(),
        entry(Registries.WOLF_VARIANT, WolfVariants.class, Wolf.Variant.class).writableApiRegistryBuilder(WolfVariantRegistryEntry.Builder.class, "PaperWolfVariantRegistryEntry.PaperBuilder").delayed(),
        entry(Registries.WOLF_SOUND_VARIANT, WolfSoundVariants.class, Wolf.SoundVariant.class),
        entry(Registries.ENCHANTMENT, Enchantments.class, Enchantment.class).writableApiRegistryBuilder(EnchantmentRegistryEntry.Builder.class, "PaperEnchantmentRegistryEntry.PaperBuilder").serializationUpdater("ENCHANTMENT_RENAME").delayed(),
        entry(Registries.JUKEBOX_SONG, JukeboxSongs.class, JukeboxSong.class).writableApiRegistryBuilder(JukeboxSongRegistryEntry.Builder.class, "PaperJukeboxSongRegistryEntry.PaperBuilder").delayed(),
        entry(Registries.BANNER_PATTERN, BannerPatterns.class, PatternType.class).allowDirect().writableApiRegistryBuilder(BannerPatternRegistryEntry.Builder.class, "PaperBannerPatternRegistryEntry.PaperBuilder").delayed(),
        entry(Registries.PAINTING_VARIANT, PaintingVariants.class, Art.class).writableApiRegistryBuilder(PaintingVariantRegistryEntry.Builder.class, "PaperPaintingVariantRegistryEntry.PaperBuilder").apiRegistryField("ART").delayed(),
        entry(Registries.INSTRUMENT, Instruments.class, MusicInstrument.class).allowDirect().delayed(),
        entry(Registries.CAT_VARIANT, CatVariants.class, Cat.Type.class).writableApiRegistryBuilder(CatTypeRegistryEntry.Builder.class, "PaperCatTypeRegistryEntry.PaperBuilder").delayed(),
        entry(Registries.FROG_VARIANT, FrogVariants.class, Frog.Variant.class).writableApiRegistryBuilder(FrogVariantRegistryEntry.Builder.class, "PaperFrogVariantRegistryEntry.PaperBuilder").delayed(),
        entry(Registries.CHICKEN_VARIANT, ChickenVariants.class, Chicken.Variant.class).writableApiRegistryBuilder(ChickenVariantRegistryEntry.Builder.class, "PaperChickenVariantRegistryEntry.PaperBuilder"),
        entry(Registries.COW_VARIANT, CowVariants.class, Cow.Variant.class).writableApiRegistryBuilder(CowVariantRegistryEntry.Builder.class, "PaperCowVariantRegistryEntry.PaperBuilder"),
        entry(Registries.PIG_VARIANT, PigVariants.class, Pig.Variant.class).writableApiRegistryBuilder(PigVariantRegistryEntry.Builder.class, "PaperPigVariantRegistryEntry.PaperBuilder"),
        entry(Registries.DIALOG, Dialogs.class, Dialog.class, "Paper").allowDirect().writableApiRegistryBuilder(DialogRegistryEntry.Builder.class, "PaperDialogRegistryEntry.PaperBuilder")
    );

    public static final List<RegistryEntry<?>> API_ONLY = List.of(
        entry(Registries.ENTITY_TYPE, net.minecraft.world.entity.EntityType.class, EntityType.class),
        entry(Registries.PARTICLE_TYPE, ParticleTypes.class, Particle.class),
        entry(Registries.POTION, Potions.class, PotionType.class),
        entry(Registries.MEMORY_MODULE_TYPE, MemoryModuleType.class, MemoryKey.class)
    );

    public static final Map<ResourceKey<? extends Registry<?>>, RegistryEntry<?>> BY_REGISTRY_KEY;
    static {
        Map<ResourceKey<? extends Registry<?>>, RegistryEntry<?>> byRegistryKey = new IdentityHashMap<>(BUILT_IN.size() + DATA_DRIVEN.size() + API_ONLY.size());
        forEach(entry -> {
            byRegistryKey.put(entry.registryKey(), entry);
        }, RegistryEntries.BUILT_IN, RegistryEntries.DATA_DRIVEN, RegistryEntries.API_ONLY);
        BY_REGISTRY_KEY = Collections.unmodifiableMap(byRegistryKey);
    }

    @SuppressWarnings("unchecked")
    public static <T> RegistryEntry<T> byRegistryKey(ResourceKey<? extends Registry<T>> registryKey) {
        return (RegistryEntry<T>) Objects.requireNonNull(BY_REGISTRY_KEY.get(registryKey));
    }

    // real registries
    public static void forEach(Consumer<RegistryEntry<?>> callback) {
        forEach(callback, RegistryEntries.BUILT_IN, RegistryEntries.DATA_DRIVEN);
    }

    @SafeVarargs
    public static void forEach(Consumer<RegistryEntry<?>> callback, List<RegistryEntry<?>>... datas) {
        for (List<RegistryEntry<?>> data : datas) {
            for (RegistryEntry<?> entry : data) {
                callback.accept(entry);
            }
        }
    }

    private RegistryEntries() {
    }
}
