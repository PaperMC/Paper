package org.bukkit.support.provider;

import com.google.common.collect.Lists;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.PaperDialog;
import io.papermc.paper.inventory.CreativeModeTab;
import io.papermc.paper.inventory.CreativeModeTabs;
import io.papermc.paper.inventory.PaperCreativeModeTab;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryKey;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.minecraft.world.entity.animal.pig.PigVariant;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.bukkit.Art;
import org.bukkit.Fluid;
import org.bukkit.GameEvent;
import org.bukkit.GameRule;
import org.bukkit.GameRules;
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
import org.bukkit.craftbukkit.CraftGameRule;
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
import org.bukkit.craftbukkit.entity.CraftZombieNautilus;
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
import org.bukkit.entity.ZombieNautilus;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.map.MapCursor;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

@NullMarked
public class RegistriesArgumentProvider implements ArgumentsProvider {

    public record RegistryArgument<M, B extends Keyed>(ResourceKey<? extends Registry<? extends M>> registryKey,
                                                       RegistryKey<B> apiRegistryKey,
                                                       Class<B> api, Class<?> apiHolder,
                                                       Class<? extends B> impl,
                                                       Class<M> internal) implements Arguments {
        @Override
        public Object[] get() {
            return new Object[]{
                this.apiRegistryKey,
                this.api,
                this.registryKey,
                this.impl,
                this.internal,
                this.apiHolder
            };
        }
    }

    private static final List<RegistryArgument<?, ?>> DATA = Lists.newArrayList();

    static {
        register(Registries.PAINTING_VARIANT, Art.class, CraftArt.class, PaintingVariant.class);
        register(Registries.ATTRIBUTE, Attribute.class, CraftAttribute.class, net.minecraft.world.entity.ai.attributes.Attribute.class);
        register(Registries.BIOME, Biome.class, CraftBiome.class, net.minecraft.world.level.biome.Biome.class);
        register(Registries.ENCHANTMENT, Enchantment.class, CraftEnchantment.class, net.minecraft.world.item.enchantment.Enchantment.class);
        register(Registries.FLUID, Fluid.class, CraftFluid.class, net.minecraft.world.level.material.Fluid.class);
        register(Registries.GAME_EVENT, GameEvent.class, CraftGameEvent.class, net.minecraft.world.level.gameevent.GameEvent.class);
        register(Registries.INSTRUMENT, MusicInstrument.class, CraftMusicInstrument.class, Instrument.class);
        register(Registries.MOB_EFFECT, PotionEffectType.class, CraftPotionEffectType.class, MobEffect.class);
        register(Registries.SOUND_EVENT, Sound.class, CraftSound.class, SoundEvent.class);
        register(Registries.STRUCTURE, Structure.class, CraftStructure.class, net.minecraft.world.level.levelgen.structure.Structure.class);
        register(Registries.STRUCTURE_TYPE, StructureType.class, CraftStructureType.class, net.minecraft.world.level.levelgen.structure.StructureType.class);
        register(Registries.VILLAGER_TYPE, Villager.Type.class, CraftVillager.CraftType.class, VillagerType.class);
        register(Registries.VILLAGER_PROFESSION, Villager.Profession.class, CraftVillager.CraftProfession.class, VillagerProfession.class);
        register(Registries.TRIM_MATERIAL, TrimMaterial.class, CraftTrimMaterial.class, net.minecraft.world.item.equipment.trim.TrimMaterial.class);
        register(Registries.TRIM_PATTERN, TrimPattern.class, CraftTrimPattern.class, net.minecraft.world.item.equipment.trim.TrimPattern.class);
        register(Registries.DAMAGE_TYPE, DamageType.class, CraftDamageType.class, net.minecraft.world.damagesource.DamageType.class);
        register(Registries.JUKEBOX_SONG, JukeboxSong.class, CraftJukeboxSong.class, net.minecraft.world.item.JukeboxSong.class);
        register(Registries.WOLF_VARIANT, Wolf.Variant.class, CraftWolf.CraftVariant.class, WolfVariant.class);
        register(Registries.WOLF_SOUND_VARIANT, Wolf.SoundVariant.class, CraftWolf.CraftSoundVariant.class, WolfSoundVariant.class);
        register(Registries.ITEM, ItemType.class, CraftItemType.class, net.minecraft.world.item.Item.class);
        register(Registries.BLOCK, BlockType.class, CraftBlockType.class, net.minecraft.world.level.block.Block.class);
        register(Registries.FROG_VARIANT, Frog.Variant.class, CraftFrog.CraftVariant.class, FrogVariant.class);
        register(Registries.CAT_VARIANT, Cat.Type.class, CraftCat.CraftType.class, CatVariant.class);
        register(Registries.MAP_DECORATION_TYPE, MapCursor.Type.class, CraftMapCursor.CraftType.class, MapDecorationType.class);
        register(Registries.BANNER_PATTERN, PatternType.class, CraftPatternType.class, BannerPattern.class);
        register(Registries.MENU, MenuType.class, CraftMenuType.class, net.minecraft.world.inventory.MenuType.class);
        register(Registries.DATA_COMPONENT_TYPE, io.papermc.paper.datacomponent.DataComponentType.class, DataComponentTypes.class, io.papermc.paper.datacomponent.PaperDataComponentType.class, net.minecraft.core.component.DataComponentType.class);
        register(Registries.CHICKEN_VARIANT, Chicken.Variant.class, CraftChicken.CraftVariant.class, ChickenVariant.class);
        register(Registries.COW_VARIANT, Cow.Variant.class, CraftCow.CraftVariant.class, CowVariant.class);
        register(Registries.PIG_VARIANT, Pig.Variant.class, CraftPig.CraftVariant.class, PigVariant.class);
        register(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilus.Variant.class, CraftZombieNautilus.CraftVariant.class, ZombieNautilusVariant.class);
        register(Registries.DIALOG, Dialog.class, PaperDialog.class, net.minecraft.server.dialog.Dialog.class);
        register(Registries.GAME_RULE, GameRule.class, GameRules.class, CraftGameRule.class, net.minecraft.world.level.gamerules.GameRule.class);
        register(Registries.CREATIVE_MODE_TAB, CreativeModeTab.class, CreativeModeTabs.class, PaperCreativeModeTab.class, net.minecraft.world.item.CreativeModeTab.class);
    }

    private static <M, B extends Keyed> void register(ResourceKey<? extends Registry<? extends M>> registryKey, Class<B> api, Class<? extends B> impl, Class<M> internal) {
        register(registryKey, api, api, impl, internal);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <M, B extends Keyed> void register(ResourceKey<? extends Registry<? extends M>> registryKey, Class<B> api, Class<?> apiHolder, Class<? extends B> impl, Class<M> internal) {
        DATA.add(new RegistryArgument<>(registryKey, PaperRegistries.registryFromNms((ResourceKey) registryKey), api, apiHolder, impl, internal));
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return getData();
    }

    public static Stream<? extends RegistryArgument<?, ?>> getData() {
        return DATA.stream();
    }
}
