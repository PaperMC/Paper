package io.papermc.paper.registry;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Art;
import org.bukkit.Fluid;
import org.bukkit.GameEvent;
import org.bukkit.JukeboxSong;
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
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import static io.papermc.paper.registry.RegistryKeyImpl.create;

/**
 * Identifier for a specific registry. For use with
 * {@link TypedKey} and the registry modification API.
 * <p>
 * There are 2 types of registries, identified as "built-in"
 * or "data-driven". The former are not changeable by datapacks (which
 * doesn't necessarily mean they aren't changeable in the API) and
 * are loaded first. "Data-driven" registries are all created by
 * reading in data from the vanilla and other datapacks.
 *
 * @param <T> the value type
 */
@SuppressWarnings("unused")
@NullMarked
public sealed interface RegistryKey<T> extends Keyed permits RegistryKeyImpl {

    /* ******************* *
     * Built-in Registries *
     * ******************* */
    /**
     * Built-in registry for game events
     * @see io.papermc.paper.registry.keys.GameEventKeys
     */
    RegistryKey<GameEvent> GAME_EVENT = create("game_event");
    /**
     * Built-in registry for structure types.
     * @see io.papermc.paper.registry.keys.StructureTypeKeys
     */
    RegistryKey<StructureType> STRUCTURE_TYPE = create("worldgen/structure_type");
    /**
     * Built-in registry for potion effect types (mob effects).
     * @see io.papermc.paper.registry.keys.MobEffectKeys
     */
    RegistryKey<PotionEffectType> MOB_EFFECT = create("mob_effect");
    /**
     * @apiNote DO NOT USE
     */
    @ApiStatus.Internal
    RegistryKey<BlockType> BLOCK = create("block");
    /**
     * @apiNote use preferably only in the context of registry entries.
     * @see io.papermc.paper.registry.data
     * @see io.papermc.paper.registry.keys.ItemTypeKeys
     */
    @ApiStatus.Experimental // Paper - already required for registry builders
    RegistryKey<ItemType> ITEM = create("item");
    /**
     * Built-in registry for villager professions.
     * @see io.papermc.paper.registry.keys.VillagerProfessionKeys
     */
    RegistryKey<Villager.Profession> VILLAGER_PROFESSION = create("villager_profession");
    /**
     * Built-in registry for villager types.
     * @see io.papermc.paper.registry.keys.VillagerTypeKeys
     */
    RegistryKey<Villager.Type> VILLAGER_TYPE = create("villager_type");
    /**
     * Built-in registry for map decoration types.
     * @see io.papermc.paper.registry.keys.MapDecorationTypeKeys
     */
    RegistryKey<MapCursor.Type> MAP_DECORATION_TYPE = create("map_decoration_type");
    /**
     * Built-in registry for menu types.
     * @see io.papermc.paper.registry.keys.MenuTypeKeys
     */
    RegistryKey<MenuType> MENU = create("menu");
    /**
     * Built-in registry for attributes.
     * @see io.papermc.paper.registry.keys.AttributeKeys
     */
    RegistryKey<Attribute> ATTRIBUTE = create("attribute");
    /**
     * Built-in registry for fluids.
     * @see io.papermc.paper.registry.keys.FluidKeys
     */
    RegistryKey<Fluid> FLUID = create("fluid");
    /**
     * Built-in registry for sound events.
     * @see io.papermc.paper.registry.keys.SoundEventKeys
     */
    RegistryKey<Sound> SOUND_EVENT = create("sound_event");
    /**
     * Built-in registry for data component types.
     * @see io.papermc.paper.registry.keys.DataComponentTypeKeys
     */
    RegistryKey<DataComponentType> DATA_COMPONENT_TYPE = create("data_component_type");



    /* ********************** *
     * Data-driven Registries *
     * ********************** */
    /**
     * Data-driven registry for biomes.
     * @see io.papermc.paper.registry.keys.BiomeKeys
     */
    RegistryKey<Biome> BIOME = create("worldgen/biome");
    /**
     * Data-driven registry for structures.
     * @see io.papermc.paper.registry.keys.StructureKeys
     */
    RegistryKey<Structure> STRUCTURE = create("worldgen/structure");
    /**
     * Data-driven registry for trim materials.
     * @see io.papermc.paper.registry.keys.TrimMaterialKeys
     */
    RegistryKey<TrimMaterial> TRIM_MATERIAL = create("trim_material");
    /**
     * Data-driven registry for trim patterns.
     * @see io.papermc.paper.registry.keys.TrimPatternKeys
     */
    RegistryKey<TrimPattern> TRIM_PATTERN = create("trim_pattern");
    /**
     * Data-driven registry for damage types.
     * @see io.papermc.paper.registry.keys.DamageTypeKeys
     */
    RegistryKey<DamageType> DAMAGE_TYPE = create("damage_type");
    /**
     * Data-driven registry for wolf variants.
     * @see io.papermc.paper.registry.keys.WolfVariantKeys
     */
    RegistryKey<Wolf.Variant> WOLF_VARIANT = create("wolf_variant");
    /**
     * Data-driven registry for wolf sound variants.
     * @see io.papermc.paper.registry.keys.WolfSoundVariantKeys
     */
    RegistryKey<Wolf.SoundVariant> WOLF_SOUND_VARIANT = create("wolf_sound_variant");
    /**
     * Data-driven registry for enchantments.
     * @see io.papermc.paper.registry.keys.EnchantmentKeys
     */
    RegistryKey<Enchantment> ENCHANTMENT = create("enchantment");
    /**
     * Data-driven registry for jukebox songs.
     * @see io.papermc.paper.registry.keys.JukeboxSongKeys
     */
    RegistryKey<JukeboxSong> JUKEBOX_SONG = create("jukebox_song");
    /**
     * Data-driven registry for banner patterns.
     * @see io.papermc.paper.registry.keys.BannerPatternKeys
     */
    RegistryKey<PatternType> BANNER_PATTERN = create("banner_pattern");
    /**
     * Data-driven registry for painting variants.
     * @see io.papermc.paper.registry.keys.PaintingVariantKeys
     */
    RegistryKey<Art> PAINTING_VARIANT = create("painting_variant");
    /**
     * Data-driven registry for instruments.
     * @see io.papermc.paper.registry.keys.InstrumentKeys
     */
    RegistryKey<MusicInstrument> INSTRUMENT = create("instrument");
    /**
     * Data-driven registry for cat variants.
     * @see io.papermc.paper.registry.keys.CatVariantKeys
     */
    RegistryKey<Cat.Type> CAT_VARIANT = create("cat_variant");
    /**
     * Data-driven registry for frog variants.
     * @see io.papermc.paper.registry.keys.FrogVariantKeys
     */
    RegistryKey<Frog.Variant> FROG_VARIANT = create("frog_variant");
    /**
     * Data-driven registry for chicken variants.
     * @see io.papermc.paper.registry.keys.ChickenVariantKeys
     */
    RegistryKey<Chicken.Variant> CHICKEN_VARIANT = create("chicken_variant");
    /**
     * Data-driven registry for cow variants.
     * @see io.papermc.paper.registry.keys.CowVariantKeys
     */
    RegistryKey<Cow.Variant> COW_VARIANT = create("cow_variant");
    /**
     * Data-driven registry for pig variants.
     * @see io.papermc.paper.registry.keys.PigVariantKeys
     */
    RegistryKey<Pig.Variant> PIG_VARIANT = create("pig_variant");

    /**
     * Data-driven registry for dialogs.
     * @see io.papermc.paper.registry.keys.DialogKeys
     */
    RegistryKey<Dialog> DIALOG = create("dialog");


    /* ******************* *
     * API-only Registries *
     * ******************* */
    RegistryKey<EntityType> ENTITY_TYPE = create("entity_type");
    RegistryKey<Particle> PARTICLE_TYPE = create("particle_type");
    RegistryKey<PotionType> POTION = create("potion");
    RegistryKey<MemoryKey<?>> MEMORY_MODULE_TYPE = create("memory_module_type");

    /**
     * Constructs a new {@link TypedKey} for this registry given the typed key's key.
     *
     * @param key the key of the typed key.
     * @return the constructed typed key.
     */
    default TypedKey<T> typedKey(final Key key) {
        return TypedKey.create(this, key);
    }

    /**
     * Constructs a new {@link TypedKey} for this registry given the typed key's key.
     *
     * @param key the string representation of the key that will be passed to {@link Key#key(String)}.
     * @return the constructed typed key.
     */
    default TypedKey<T> typedKey(@KeyPattern final String key) {
        return TypedKey.create(this, key);
    }

    /**
     * Constructs a new {@link TagKey} for this registry given the tag key's key.
     *
     * @param key the key of the typed key.
     * @return the constructed tag key.
     */
    @ApiStatus.Experimental
    default TagKey<T> tagKey(final Key key) {
        return TagKey.create(this, key);
    }

    /**
     * Constructs a new {@link TagKey} for this registry given the tag key's key.
     *
     * @param key the string representation of the key that will be passed to {@link Key#key(String)}.
     * @return the constructed tag key.
     */
    @ApiStatus.Experimental
    default TagKey<T> tagKey(@KeyPattern final String key) {
        return TagKey.create(this, key);
    }
}
