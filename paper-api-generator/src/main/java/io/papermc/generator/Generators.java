package io.papermc.generator;

import io.papermc.generator.types.GeneratedKeyType;
import io.papermc.generator.types.GeneratedTagKeyType;
import io.papermc.generator.types.SourceGenerator;
import io.papermc.generator.types.goal.MobGoalGenerator;
import io.papermc.paper.registry.RegistryKey;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Art;
import org.bukkit.Fluid;
import org.bukkit.GameEvent;
import org.bukkit.JukeboxSong;
import org.bukkit.MusicInstrument;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockType;
import org.bukkit.block.banner.PatternType;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.map.MapCursor;
import org.bukkit.potion.PotionEffectType;

public interface Generators {

    SourceGenerator[] API = {
        // built-ins
        simpleKey("GameEventKeys", GameEvent.class, Registries.GAME_EVENT, RegistryKey.GAME_EVENT, true),
        simpleKey("StructureTypeKeys", StructureType.class, Registries.STRUCTURE_TYPE, RegistryKey.STRUCTURE_TYPE, false),
        simpleKey("MobEffectKeys", PotionEffectType.class, Registries.MOB_EFFECT, RegistryKey.MOB_EFFECT, false),
        simpleKey("BlockTypeKeys", BlockType.class, Registries.BLOCK, RegistryKey.BLOCK, false),
        simpleKey("ItemTypeKeys", ItemType.class, Registries.ITEM, RegistryKey.ITEM, false),
        simpleKey("CatVariantKeys", Cat.Type.class, Registries.CAT_VARIANT, RegistryKey.CAT_VARIANT, false),
        simpleKey("FrogVariantKeys", Frog.Variant.class, Registries.FROG_VARIANT, RegistryKey.FROG_VARIANT, false),
        simpleKey("VillagerProfessionKeys", Villager.Profession.class, Registries.VILLAGER_PROFESSION, RegistryKey.VILLAGER_PROFESSION, false),
        simpleKey("VillagerTypeKeys", Villager.Type.class, Registries.VILLAGER_TYPE, RegistryKey.VILLAGER_TYPE, false),
        simpleKey("MapDecorationTypeKeys", MapCursor.Type.class, Registries.MAP_DECORATION_TYPE, RegistryKey.MAP_DECORATION_TYPE, false),
        simpleKey("MenuTypeKeys", MenuType.class, Registries.MENU, RegistryKey.MENU, false),
        simpleKey("AttributeKeys", Attribute.class, Registries.ATTRIBUTE, RegistryKey.ATTRIBUTE, false),
        simpleKey("FluidKeys", Fluid.class, Registries.FLUID, RegistryKey.FLUID, false),
        simpleKey("SoundEventKeys", Sound.class, Registries.SOUND_EVENT, RegistryKey.SOUND_EVENT, false),

        // data-driven
        simpleKey("BiomeKeys", Biome.class, Registries.BIOME, RegistryKey.BIOME, true),
        simpleKey("StructureKeys", Structure.class, Registries.STRUCTURE, RegistryKey.STRUCTURE, true),
        simpleKey("TrimMaterialKeys", TrimMaterial.class, Registries.TRIM_MATERIAL, RegistryKey.TRIM_MATERIAL, true),
        simpleKey("TrimPatternKeys", TrimPattern.class, Registries.TRIM_PATTERN, RegistryKey.TRIM_PATTERN, true),
        simpleKey("DamageTypeKeys", DamageType.class, Registries.DAMAGE_TYPE, RegistryKey.DAMAGE_TYPE, true),
        simpleKey("WolfVariantKeys", Wolf.Variant.class, Registries.WOLF_VARIANT, RegistryKey.WOLF_VARIANT, true),
        simpleKey("EnchantmentKeys", Enchantment.class, Registries.ENCHANTMENT, RegistryKey.ENCHANTMENT, true),
        simpleKey("JukeboxSongKeys", JukeboxSong.class, Registries.JUKEBOX_SONG, RegistryKey.JUKEBOX_SONG, true),
        simpleKey("BannerPatternKeys", PatternType.class, Registries.BANNER_PATTERN, RegistryKey.BANNER_PATTERN, true),
        simpleKey("PaintingVariantKeys", Art.class, Registries.PAINTING_VARIANT, RegistryKey.PAINTING_VARIANT, true),
        simpleKey("InstrumentKeys", MusicInstrument.class, Registries.INSTRUMENT, RegistryKey.INSTRUMENT, true),

        // tags
        simpleTagKey("GameEventTagKeys", GameEvent.class, Registries.GAME_EVENT, RegistryKey.GAME_EVENT),
        simpleTagKey("BlockTypeTagKeys", BlockType.class, Registries.BLOCK, RegistryKey.BLOCK),
        simpleTagKey("ItemTypeTagKeys", ItemType.class, Registries.ITEM, RegistryKey.ITEM),
        simpleTagKey("CatVariantTagKeys", Cat.Type.class, Registries.CAT_VARIANT, RegistryKey.CAT_VARIANT),
        simpleTagKey("FluidTagKeys", Fluid.class, Registries.FLUID, RegistryKey.FLUID),

        simpleTagKey("BiomeTagKeys", Biome.class, Registries.BIOME, RegistryKey.BIOME),
        simpleTagKey("StructureTagKeys", Structure.class, Registries.STRUCTURE, RegistryKey.STRUCTURE),
        simpleTagKey("DamageTypeTagKeys", DamageType.class, Registries.DAMAGE_TYPE, RegistryKey.DAMAGE_TYPE),
        simpleTagKey("EnchantmentTagKeys", Enchantment.class, Registries.ENCHANTMENT, RegistryKey.ENCHANTMENT),
        simpleTagKey("BannerPatternTagKeys", PatternType.class, Registries.BANNER_PATTERN, RegistryKey.BANNER_PATTERN),
        simpleTagKey("PaintingVariantTagKeys", Art.class, Registries.PAINTING_VARIANT, RegistryKey.PAINTING_VARIANT),
        simpleTagKey("InstrumentTagKeys", MusicInstrument.class, Registries.INSTRUMENT, RegistryKey.INSTRUMENT),
        new MobGoalGenerator("VanillaGoal", "com.destroystokyo.paper.entity.ai")
    };

    private static <T, A> SourceGenerator simpleKey(final String className, final Class<A> apiType, final ResourceKey<? extends Registry<T>> registryKey, final RegistryKey<A> apiRegistryKey, final boolean publicCreateKeyMethod) {
        return new GeneratedKeyType<>(className, apiType, "io.papermc.paper.registry.keys", registryKey, apiRegistryKey, publicCreateKeyMethod);
    }

    private static <T, A> SourceGenerator simpleTagKey(final String className, final Class<A> apiType, final ResourceKey<? extends Registry<T>> registryKey, final RegistryKey<A> apiRegistryKey) {
        return new GeneratedTagKeyType<>(className, apiType, "io.papermc.paper.registry.keys.tags", registryKey, apiRegistryKey, true);
    }
}
