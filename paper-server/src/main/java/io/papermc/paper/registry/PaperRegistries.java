package io.papermc.paper.registry;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.PaperDataComponentType;
import io.papermc.paper.registry.data.PaperBannerPatternRegistryEntry;
import io.papermc.paper.registry.data.PaperDamageTypeRegistryEntry;
import io.papermc.paper.registry.data.PaperEnchantmentRegistryEntry;
import io.papermc.paper.registry.data.PaperGameEventRegistryEntry;
import io.papermc.paper.registry.data.PaperPaintingVariantRegistryEntry;
import io.papermc.paper.registry.entry.RegistryEntry;
import io.papermc.paper.registry.entry.RegistryEntryMeta;
import io.papermc.paper.registry.tag.TagKey;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Art;
import org.bukkit.Fluid;
import org.bukkit.GameEvent;
import org.bukkit.JukeboxSong;
import org.bukkit.Keyed;
import org.bukkit.MusicInstrument;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockType;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.craftbukkit.CraftFluid;
import org.bukkit.craftbukkit.CraftGameEvent;
import org.bukkit.craftbukkit.CraftJukeboxSong;
import org.bukkit.craftbukkit.CraftMusicInstrument;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.block.banner.CraftPatternType;
import org.bukkit.craftbukkit.damage.CraftDamageType;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.entity.CraftCat;
import org.bukkit.craftbukkit.entity.CraftFrog;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.craftbukkit.generator.structure.CraftStructure;
import org.bukkit.craftbukkit.generator.structure.CraftStructureType;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.craftbukkit.inventory.CraftMenuType;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimMaterial;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimPattern;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.map.CraftMapCursor;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Frog;
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
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.entry.RegistryEntryBuilder.start;

public final class PaperRegistries {

    static final List<RegistryEntry<?, ?>> REGISTRY_ENTRIES;
    private static final Map<RegistryKey<?>, RegistryEntry<?, ?>> BY_REGISTRY_KEY;
    private static final Map<ResourceKey<?>, RegistryEntry<?, ?>> BY_RESOURCE_KEY;
    static {
        REGISTRY_ENTRIES = List.of(
            // built-ins
            start(Registries.GAME_EVENT, RegistryKey.GAME_EVENT).craft(GameEvent.class, CraftGameEvent::new).writable(PaperGameEventRegistryEntry.PaperBuilder::new),
            start(Registries.STRUCTURE_TYPE, RegistryKey.STRUCTURE_TYPE).craft(StructureType.class, CraftStructureType::new).build(),
            start(Registries.MOB_EFFECT, RegistryKey.MOB_EFFECT).craft(PotionEffectType.class, CraftPotionEffectType::new).build(),
            start(Registries.BLOCK, RegistryKey.BLOCK).craft(BlockType.class, CraftBlockType::new).build(),
            start(Registries.ITEM, RegistryKey.ITEM).craft(ItemType.class, CraftItemType::new).build(),
            start(Registries.CAT_VARIANT, RegistryKey.CAT_VARIANT).craft(Cat.Type.class, CraftCat.CraftType::new).build(),
            start(Registries.FROG_VARIANT, RegistryKey.FROG_VARIANT).craft(Frog.Variant.class, CraftFrog.CraftVariant::new).build(),
            start(Registries.VILLAGER_PROFESSION, RegistryKey.VILLAGER_PROFESSION).craft(Villager.Profession.class, CraftVillager.CraftProfession::new).build(),
            start(Registries.VILLAGER_TYPE, RegistryKey.VILLAGER_TYPE).craft(Villager.Type.class, CraftVillager.CraftType::new).build(),
            start(Registries.MAP_DECORATION_TYPE, RegistryKey.MAP_DECORATION_TYPE).craft(MapCursor.Type.class, CraftMapCursor.CraftType::new).build(),
            start(Registries.MENU, RegistryKey.MENU).craft(MenuType.class, CraftMenuType::new).build(),
            start(Registries.ATTRIBUTE, RegistryKey.ATTRIBUTE).craft(Attribute.class, CraftAttribute::new).build(),
            start(Registries.FLUID, RegistryKey.FLUID).craft(Fluid.class, CraftFluid::new).build(),
            start(Registries.SOUND_EVENT, RegistryKey.SOUND_EVENT).craft(Sound.class, CraftSound::new).build(),
            start(Registries.DATA_COMPONENT_TYPE, RegistryKey.DATA_COMPONENT_TYPE).craft(DataComponentTypes.class, PaperDataComponentType::of).build(),

            // data-drivens
            start(Registries.BIOME, RegistryKey.BIOME).craft(Biome.class, CraftBiome::new).build().delayed(),
            start(Registries.STRUCTURE, RegistryKey.STRUCTURE).craft(Structure.class, CraftStructure::new).build().delayed(),
            start(Registries.TRIM_MATERIAL, RegistryKey.TRIM_MATERIAL).craft(TrimMaterial.class, CraftTrimMaterial::new).build().delayed(),
            start(Registries.TRIM_PATTERN, RegistryKey.TRIM_PATTERN).craft(TrimPattern.class, CraftTrimPattern::new).build().delayed(),
            start(Registries.DAMAGE_TYPE, RegistryKey.DAMAGE_TYPE).craft(DamageType.class, CraftDamageType::new).writable(PaperDamageTypeRegistryEntry.PaperBuilder::new).delayed(),
            start(Registries.WOLF_VARIANT, RegistryKey.WOLF_VARIANT).craft(Wolf.Variant.class, CraftWolf.CraftVariant::new).build().delayed(),
            start(Registries.ENCHANTMENT, RegistryKey.ENCHANTMENT).craft(Enchantment.class, CraftEnchantment::new).serializationUpdater(FieldRename.ENCHANTMENT_RENAME).writable(PaperEnchantmentRegistryEntry.PaperBuilder::new).delayed(),
            start(Registries.JUKEBOX_SONG, RegistryKey.JUKEBOX_SONG).craft(JukeboxSong.class, CraftJukeboxSong::new).build().delayed(),
            start(Registries.BANNER_PATTERN, RegistryKey.BANNER_PATTERN).craft(PatternType.class, CraftPatternType::new).writable(PaperBannerPatternRegistryEntry.PaperBuilder::new).delayed(),
            start(Registries.PAINTING_VARIANT, RegistryKey.PAINTING_VARIANT).craft(Art.class, CraftArt::new).writable(PaperPaintingVariantRegistryEntry.PaperBuilder::new).delayed(),
            start(Registries.INSTRUMENT, RegistryKey.INSTRUMENT).craft(MusicInstrument.class, CraftMusicInstrument::new).build().delayed(),

            // api-only
            start(Registries.ENTITY_TYPE, RegistryKey.ENTITY_TYPE).apiOnly(PaperSimpleRegistry::entityType),
            start(Registries.PARTICLE_TYPE, RegistryKey.PARTICLE_TYPE).apiOnly(PaperSimpleRegistry::particleType),
            start(Registries.POTION, RegistryKey.POTION).apiOnly(PaperSimpleRegistry::potion),
            start(Registries.MEMORY_MODULE_TYPE, RegistryKey.MEMORY_MODULE_TYPE).apiOnly(() -> (org.bukkit.Registry<MemoryKey<?>>) (org.bukkit.Registry) org.bukkit.Registry.MEMORY_MODULE_TYPE)
        );
        final Map<RegistryKey<?>, RegistryEntry<?, ?>> byRegistryKey = new IdentityHashMap<>(REGISTRY_ENTRIES.size());
        final Map<ResourceKey<?>, RegistryEntry<?, ?>> byResourceKey = new IdentityHashMap<>(REGISTRY_ENTRIES.size());
        for (final RegistryEntry<?, ?> entry : REGISTRY_ENTRIES) {
            Preconditions.checkState(byRegistryKey.put(entry.apiKey(), entry) == null, "Duplicate api registry key: %s", entry.apiKey());
            Preconditions.checkState(byResourceKey.put(entry.mcKey(), entry) == null, "Duplicate mc registry key: %s", entry.mcKey());
        }
        BY_REGISTRY_KEY = Collections.unmodifiableMap(byRegistryKey);
        BY_RESOURCE_KEY = Collections.unmodifiableMap(byResourceKey);
    }

    @SuppressWarnings("unchecked")
    public static <M, T extends Keyed> @Nullable RegistryEntry<M, T> getEntry(final ResourceKey<? extends Registry<M>> resourceKey) {
        return (RegistryEntry<M, T>) BY_RESOURCE_KEY.get(resourceKey);
    }

    @SuppressWarnings("unchecked")
    public static <M, T extends Keyed> @Nullable RegistryEntry<M, T> getEntry(final RegistryKey<? super T> registryKey) {
        return (RegistryEntry<M, T>) BY_REGISTRY_KEY.get(registryKey);
    }

    @SuppressWarnings("unchecked")
    public static <M, T extends Keyed, B extends PaperRegistryBuilder<M, T>> RegistryEntryMeta.Buildable<M, T, B> getBuildableMeta(final ResourceKey<? extends Registry<M>> resourceKey) {
        final RegistryEntry<M, T> entry = getEntry(resourceKey);
        if (entry == null) {
            throw new IllegalArgumentException("No registry entry for " + resourceKey);
        }
        if (!(entry.meta() instanceof final RegistryEntryMeta.Buildable<M, T, ?> buildableMeta)) {
            throw new IllegalArgumentException("Registry entry for " + resourceKey + " is not buildable");
        }
        return (RegistryEntryMeta.Buildable<M, T, B>) buildableMeta;
    }

    @SuppressWarnings("unchecked")
    public static <M, T> RegistryKey<T> registryFromNms(final ResourceKey<? extends Registry<M>> registryResourceKey) {
        return (RegistryKey<T>) Objects.requireNonNull(BY_RESOURCE_KEY.get(registryResourceKey), registryResourceKey + " doesn't have an api RegistryKey").apiKey();
    }

    @SuppressWarnings("unchecked")
    public static <M, T> ResourceKey<? extends Registry<M>> registryToNms(final RegistryKey<T> registryKey) {
        return (ResourceKey<? extends Registry<M>>) Objects.requireNonNull(BY_REGISTRY_KEY.get(registryKey), registryKey + " doesn't have an mc registry ResourceKey").mcKey();
    }

    public static <M, T> TypedKey<T> fromNms(final ResourceKey<M> resourceKey) {
        return TypedKey.create(registryFromNms(resourceKey.registryKey()), CraftNamespacedKey.fromMinecraft(resourceKey.location()));
    }

    @SuppressWarnings({"unchecked", "RedundantCast"})
    public static <M, T> ResourceKey<M> toNms(final TypedKey<T> typedKey) {
        return ResourceKey.create((ResourceKey<? extends Registry<M>>) PaperRegistries.registryToNms(typedKey.registryKey()), PaperAdventure.asVanilla(typedKey.key()));
    }

    public static <M, T> TagKey<T> fromNms(final net.minecraft.tags.TagKey<M> tagKey) {
        return TagKey.create(registryFromNms(tagKey.registry()), CraftNamespacedKey.fromMinecraft(tagKey.location()));
    }

    @SuppressWarnings({"unchecked", "RedundantCast"})
    public static <M, T> net.minecraft.tags.TagKey<M> toNms(final TagKey<T> tagKey) {
        return net.minecraft.tags.TagKey.create((ResourceKey<? extends Registry<M>>) registryToNms(tagKey.registryKey()), PaperAdventure.asVanilla(tagKey.key()));
    }

    private PaperRegistries() {
    }
}
