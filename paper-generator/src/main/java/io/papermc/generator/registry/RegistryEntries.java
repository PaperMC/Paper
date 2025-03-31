package io.papermc.generator.registry;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.papermc.generator.resources.DataFileLoader;
import io.papermc.generator.resources.RegistryData;
import io.papermc.generator.utils.ClassHelper;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.inventory.MenuType;
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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class RegistryEntries {

    private static <T> RegistryIntern<T> entry(ResourceKey<? extends Registry<T>> registryKey, Class<?> holderElementsClass) {
        return new RegistryIntern<>(registryKey, holderElementsClass);
    }

    public static final Map<ResourceKey<? extends Registry<?>>, RegistryKeyField<?>> REGISTRY_KEY_FIELDS;
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

    private static final Map<ResourceKey<? extends Registry<?>>, RegistryIntern<?>> EXPOSED_REGISTRIES = Stream.of(
        entry(Registries.GAME_EVENT, GameEvent.class),
        entry(Registries.STRUCTURE_TYPE, StructureType.class),
        entry(Registries.MOB_EFFECT, MobEffects.class),
        entry(Registries.BLOCK, Blocks.class),
        entry(Registries.ITEM, Items.class),
        entry(Registries.VILLAGER_PROFESSION, VillagerProfession.class),
        entry(Registries.VILLAGER_TYPE, VillagerType.class),
        entry(Registries.MAP_DECORATION_TYPE, MapDecorationTypes.class),
        entry(Registries.MENU, MenuType.class),
        entry(Registries.ATTRIBUTE, Attributes.class),
        entry(Registries.FLUID, Fluids.class),
        entry(Registries.SOUND_EVENT, SoundEvents.class),
        entry(Registries.DATA_COMPONENT_TYPE, DataComponents.class),
        entry(Registries.BIOME, Biomes.class),
        entry(Registries.STRUCTURE, BuiltinStructures.class),
        entry(Registries.TRIM_MATERIAL, TrimMaterials.class),
        entry(Registries.TRIM_PATTERN, TrimPatterns.class),
        entry(Registries.DAMAGE_TYPE, DamageTypes.class),
        entry(Registries.WOLF_VARIANT, WolfVariants.class),
        entry(Registries.WOLF_SOUND_VARIANT, WolfSoundVariants.class),
        entry(Registries.ENCHANTMENT, Enchantments.class),
        entry(Registries.JUKEBOX_SONG, JukeboxSongs.class),
        entry(Registries.BANNER_PATTERN, BannerPatterns.class),
        entry(Registries.PAINTING_VARIANT, PaintingVariants.class),
        entry(Registries.INSTRUMENT, Instruments.class),
        entry(Registries.CAT_VARIANT, CatVariants.class),
        entry(Registries.FROG_VARIANT, FrogVariants.class),
        entry(Registries.CHICKEN_VARIANT, ChickenVariants.class),
        entry(Registries.COW_VARIANT, CowVariants.class),
        entry(Registries.PIG_VARIANT, PigVariants.class),
        entry(Registries.ENTITY_TYPE, EntityType.class),
        entry(Registries.PARTICLE_TYPE, ParticleTypes.class),
        entry(Registries.POTION, Potions.class),
        entry(Registries.MEMORY_MODULE_TYPE, MemoryModuleType.class)
    ).collect(Collectors.toMap(RegistryIntern::getRegistryKey, entry -> entry));

    @Deprecated
    public static final List<RegistryEntry<?>> API_ONLY = new ArrayList<>();

    @Deprecated
    public static final List<ResourceKey<? extends Registry<?>>> API_ONLY_KEYS = List.of(
        Registries.ENTITY_TYPE, Registries.PARTICLE_TYPE, Registries.POTION, Registries.MEMORY_MODULE_TYPE
    );

    public static final Multimap<RegistryEntry.Type, RegistryEntry<?>> REGISTRIES = Util.make(MultimapBuilder.enumKeys(RegistryEntry.Type.class).arrayListValues().build(), map -> {
        List<ResourceKey<? extends Registry<?>>> remainingRegistries = new ArrayList<>(EXPOSED_REGISTRIES.keySet());
        DataFileLoader.REGISTRIES.forEach((type, file) -> {
            Map<ResourceKey<? extends Registry<?>>, RegistryData> registries = file.get();
            for (Map.Entry<ResourceKey<? extends Registry<?>>, RegistryData> registry : registries.entrySet()) {
                ResourceKey<? extends Registry<?>> registryKey = registry.getKey();
                RegistryIntern<?> intern = Objects.requireNonNull(EXPOSED_REGISTRIES.get(registryKey),
                    () -> "Registry '%s' found in registry/%s.json is not exposed to the api".formatted(registryKey.location(), type.getSerializedName())
                );
                RegistryEntry<?> entry = intern.bind(registry.getValue());
                entry.validate(type);
                if (remainingRegistries.remove(registryKey)) {
                    if (API_ONLY_KEYS.contains(registryKey)) {
                        API_ONLY.add(entry);
                    } else {
                        map.put(type, entry);
                    }
                }
            }
        });

        if (!remainingRegistries.isEmpty()) {
            throw new IllegalStateException("Registry not found in data files: " + remainingRegistries);
        }
    });

    public static Collection<RegistryEntry<?>> byType(RegistryEntry.Type type) {
        return REGISTRIES.get(type);
    }

    public static final Map<ResourceKey<? extends Registry<?>>, RegistryEntry<?>> BY_REGISTRY_KEY;
    static {
        Map<ResourceKey<? extends Registry<?>>, RegistryEntry<?>> byRegistryKey = new IdentityHashMap<>(REGISTRIES.size() + API_ONLY.size());
        forEach(entry -> {
            byRegistryKey.put(entry.getRegistryKey(), entry);
        }, REGISTRIES.values(), API_ONLY);
        BY_REGISTRY_KEY = Collections.unmodifiableMap(byRegistryKey);
    }

    @SuppressWarnings("unchecked")
    public static <T> RegistryEntry<T> byRegistryKey(ResourceKey<? extends Registry<T>> registryKey) {
        return (RegistryEntry<T>) Objects.requireNonNull(BY_REGISTRY_KEY.get(registryKey), "registry not found: " + registryKey);
    }

    // real registries
    public static void forEach(Consumer<RegistryEntry<?>> callback) {
        forEach(callback, REGISTRIES.values());
    }

    @SafeVarargs
    public static void forEach(Consumer<RegistryEntry<?>> callback, Collection<RegistryEntry<?>>... datas) {
        for (Collection<RegistryEntry<?>> data : datas) {
            for (RegistryEntry<?> entry : data) {
                callback.accept(entry);
            }
        }
    }

    private RegistryEntries() {
    }
}
