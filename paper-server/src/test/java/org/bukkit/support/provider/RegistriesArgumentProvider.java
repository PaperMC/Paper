package org.bukkit.support.provider;

import com.google.common.collect.Lists;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.PaperDialog;
import io.papermc.paper.registry.RegistryKey;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.ChickenVariant;
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.animal.PigVariant;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
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
import org.bukkit.craftbukkit.entity.CraftChicken;
import org.bukkit.craftbukkit.entity.CraftCow;
import org.bukkit.craftbukkit.entity.CraftFrog;
import org.bukkit.craftbukkit.entity.CraftPig;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.craftbukkit.generator.structure.CraftStructure;
import org.bukkit.craftbukkit.generator.structure.CraftStructureType;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.craftbukkit.inventory.CraftMenuType;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimMaterial;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimPattern;
import org.bukkit.craftbukkit.map.CraftMapCursor;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Pig;
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
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class RegistriesArgumentProvider implements ArgumentsProvider {

    private static final List<Arguments> DATA = Lists.newArrayList();

    static {
        // Order: RegistryKey, Bukkit class, Minecraft Registry key, CraftBukkit class, Minecraft class
        register(RegistryKey.PAINTING_VARIANT, Art.class, Registries.PAINTING_VARIANT, CraftArt.class, PaintingVariant.class);
        register(RegistryKey.ATTRIBUTE, Attribute.class, Registries.ATTRIBUTE, CraftAttribute.class, net.minecraft.world.entity.ai.attributes.Attribute.class);
        register(RegistryKey.BIOME, Biome.class, Registries.BIOME, CraftBiome.class, net.minecraft.world.level.biome.Biome.class);
        register(RegistryKey.ENCHANTMENT, Enchantment.class, Registries.ENCHANTMENT, CraftEnchantment.class, net.minecraft.world.item.enchantment.Enchantment.class);
        register(RegistryKey.FLUID, Fluid.class, Registries.FLUID, CraftFluid.class, net.minecraft.world.level.material.Fluid.class);
        register(RegistryKey.GAME_EVENT, GameEvent.class, Registries.GAME_EVENT, CraftGameEvent.class, net.minecraft.world.level.gameevent.GameEvent.class);
        register(RegistryKey.INSTRUMENT, MusicInstrument.class, Registries.INSTRUMENT, CraftMusicInstrument.class, Instrument.class);
        register(RegistryKey.MOB_EFFECT, PotionEffectType.class, Registries.MOB_EFFECT, CraftPotionEffectType.class, MobEffect.class);
        register(RegistryKey.SOUND_EVENT, Sound.class, Registries.SOUND_EVENT, CraftSound.class, SoundEvent.class);
        register(RegistryKey.STRUCTURE, Structure.class, Registries.STRUCTURE, CraftStructure.class, net.minecraft.world.level.levelgen.structure.Structure.class);
        register(RegistryKey.STRUCTURE_TYPE, StructureType.class, Registries.STRUCTURE_TYPE, CraftStructureType.class, net.minecraft.world.level.levelgen.structure.StructureType.class);
        register(RegistryKey.VILLAGER_TYPE, Villager.Type.class, Registries.VILLAGER_TYPE, CraftVillager.CraftType.class, VillagerType.class);
        register(RegistryKey.VILLAGER_PROFESSION, Villager.Profession.class, Registries.VILLAGER_PROFESSION, CraftVillager.CraftProfession.class, VillagerProfession.class);
        register(RegistryKey.TRIM_MATERIAL, TrimMaterial.class, Registries.TRIM_MATERIAL, CraftTrimMaterial.class, net.minecraft.world.item.equipment.trim.TrimMaterial.class);
        register(RegistryKey.TRIM_PATTERN, TrimPattern.class, Registries.TRIM_PATTERN, CraftTrimPattern.class, net.minecraft.world.item.equipment.trim.TrimPattern.class);
        register(RegistryKey.DAMAGE_TYPE, DamageType.class, Registries.DAMAGE_TYPE, CraftDamageType.class, net.minecraft.world.damagesource.DamageType.class);
        register(RegistryKey.JUKEBOX_SONG, JukeboxSong.class, Registries.JUKEBOX_SONG, CraftJukeboxSong.class, net.minecraft.world.item.JukeboxSong.class);
        register(RegistryKey.WOLF_VARIANT, Wolf.Variant.class, Registries.WOLF_VARIANT, CraftWolf.CraftVariant.class, WolfVariant.class);
        register(RegistryKey.WOLF_SOUND_VARIANT, Wolf.SoundVariant.class, Registries.WOLF_SOUND_VARIANT, CraftWolf.CraftSoundVariant.class, WolfSoundVariant.class);
        register(RegistryKey.ITEM, ItemType.class, Registries.ITEM, CraftItemType.class, net.minecraft.world.item.Item.class, true);
        register(RegistryKey.BLOCK, BlockType.class, Registries.BLOCK, CraftBlockType.class, net.minecraft.world.level.block.Block.class, true);
        register(RegistryKey.FROG_VARIANT, Frog.Variant.class, Registries.FROG_VARIANT, CraftFrog.CraftVariant.class, FrogVariant.class);
        register(RegistryKey.CAT_VARIANT, Cat.Type.class, Registries.CAT_VARIANT, CraftCat.CraftType.class, CatVariant.class);
        register(RegistryKey.MAP_DECORATION_TYPE, MapCursor.Type.class, Registries.MAP_DECORATION_TYPE, CraftMapCursor.CraftType.class, MapDecorationType.class);
        register(RegistryKey.BANNER_PATTERN, PatternType.class, Registries.BANNER_PATTERN, CraftPatternType.class, BannerPattern.class);
        register(RegistryKey.MENU, MenuType.class, Registries.MENU, CraftMenuType.class, net.minecraft.world.inventory.MenuType.class);
        register(RegistryKey.DATA_COMPONENT_TYPE, io.papermc.paper.datacomponent.DataComponentType.class, Registries.DATA_COMPONENT_TYPE, io.papermc.paper.datacomponent.PaperDataComponentType.class, net.minecraft.core.component.DataComponentType.class);
        register(RegistryKey.CHICKEN_VARIANT, Chicken.Variant.class, Registries.CHICKEN_VARIANT, CraftChicken.CraftVariant.class, ChickenVariant.class);
        register(RegistryKey.COW_VARIANT, Cow.Variant.class, Registries.COW_VARIANT, CraftCow.CraftVariant.class, CowVariant.class);
        register(RegistryKey.PIG_VARIANT, Pig.Variant.class, Registries.PIG_VARIANT, CraftPig.CraftVariant.class, PigVariant.class);
        register(RegistryKey.DIALOG, Dialog.class, Registries.DIALOG, PaperDialog.class, net.minecraft.server.dialog.Dialog.class);
    }

    private static void register(RegistryKey registryKey, Class bukkit, ResourceKey registry, Class craft, Class minecraft) { // Paper
        RegistriesArgumentProvider.register(registryKey, bukkit, registry, craft, minecraft, false);
    }

    private static void register(RegistryKey registryKey, Class bukkit, ResourceKey registry, Class craft, Class minecraft, boolean newClass) { // Paper
        RegistriesArgumentProvider.DATA.add(Arguments.of(registryKey, bukkit, registry, craft, minecraft, newClass));
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return RegistriesArgumentProvider.getData();
    }

    public static Stream<? extends Arguments> getData() {
        return RegistriesArgumentProvider.DATA.stream();
    }
}
