package org.bukkit.support.provider;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectList;
import net.minecraft.world.entity.ai.attributes.AttributeBase;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.inventory.Containers;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.level.block.entity.EnumBannerPatternType;
import net.minecraft.world.level.material.FluidType;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.bukkit.Fluid;
import org.bukkit.GameEvent;
import org.bukkit.JukeboxSong;
import org.bukkit.MusicInstrument;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockType;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.CraftFluid;
import org.bukkit.craftbukkit.CraftGameEvent;
import org.bukkit.craftbukkit.CraftJukeboxSong;
import org.bukkit.craftbukkit.CraftMusicInstrument;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
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
import org.bukkit.craftbukkit.map.CraftMapCursor;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
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
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class RegistriesArgumentProvider implements ArgumentsProvider {

    private static final List<Arguments> DATA = Lists.newArrayList();

    static {
        // Order: Bukkit class, Minecraft Registry key, CraftBukkit class, Minecraft class
        register(Attribute.class, Registries.ATTRIBUTE, CraftAttribute.class, AttributeBase.class);
        register(Enchantment.class, Registries.ENCHANTMENT, CraftEnchantment.class, net.minecraft.world.item.enchantment.Enchantment.class);
        register(Fluid.class, Registries.FLUID, CraftFluid.class, FluidType.class);
        register(GameEvent.class, Registries.GAME_EVENT, CraftGameEvent.class, net.minecraft.world.level.gameevent.GameEvent.class);
        register(MusicInstrument.class, Registries.INSTRUMENT, CraftMusicInstrument.class, Instrument.class);
        register(MenuType.class, Registries.MENU, CraftMenuType.class, Containers.class);
        register(PotionEffectType.class, Registries.MOB_EFFECT, CraftPotionEffectType.class, MobEffectList.class);
        register(Structure.class, Registries.STRUCTURE, CraftStructure.class, net.minecraft.world.level.levelgen.structure.Structure.class);
        register(StructureType.class, Registries.STRUCTURE_TYPE, CraftStructureType.class, net.minecraft.world.level.levelgen.structure.StructureType.class);
        register(Villager.Type.class, Registries.VILLAGER_TYPE, CraftVillager.CraftType.class, VillagerType.class);
        register(Villager.Profession.class, Registries.VILLAGER_PROFESSION, CraftVillager.CraftProfession.class, VillagerProfession.class);
        register(TrimMaterial.class, Registries.TRIM_MATERIAL, CraftTrimMaterial.class, net.minecraft.world.item.equipment.trim.TrimMaterial.class);
        register(TrimPattern.class, Registries.TRIM_PATTERN, CraftTrimPattern.class, net.minecraft.world.item.equipment.trim.TrimPattern.class);
        register(DamageType.class, Registries.DAMAGE_TYPE, CraftDamageType.class, net.minecraft.world.damagesource.DamageType.class);
        register(JukeboxSong.class, Registries.JUKEBOX_SONG, CraftJukeboxSong.class, net.minecraft.world.item.JukeboxSong.class);
        register(Wolf.Variant.class, Registries.WOLF_VARIANT, CraftWolf.CraftVariant.class, WolfVariant.class);
        register(ItemType.class, Registries.ITEM, CraftItemType.class, net.minecraft.world.item.Item.class, true);
        register(BlockType.class, Registries.BLOCK, CraftBlockType.class, net.minecraft.world.level.block.Block.class, true);
        register(Frog.Variant.class, Registries.FROG_VARIANT, CraftFrog.CraftVariant.class, FrogVariant.class);
        register(Cat.Type.class, Registries.CAT_VARIANT, CraftCat.CraftType.class, CatVariant.class);
        register(MapCursor.Type.class, Registries.MAP_DECORATION_TYPE, CraftMapCursor.CraftType.class, MapDecorationType.class);
        register(PatternType.class, Registries.BANNER_PATTERN, CraftPatternType.class, EnumBannerPatternType.class);

    }

    private static void register(Class bukkit, ResourceKey registry, Class craft, Class minecraft) {
        register(bukkit, registry, craft, minecraft, false);
    }

    private static void register(Class bukkit, ResourceKey registry, Class craft, Class minecraft, boolean newClass) {
        DATA.add(Arguments.of(bukkit, registry, craft, minecraft, newClass));
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return getData();
    }

    public static Stream<? extends Arguments> getData() {
        return DATA.stream();
    }
}
