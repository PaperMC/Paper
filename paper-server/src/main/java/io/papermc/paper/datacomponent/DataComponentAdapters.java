package io.papermc.paper.datacomponent;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.datacomponent.item.PaperBannerPatternLayers;
import io.papermc.paper.datacomponent.item.PaperBlockItemDataProperties;
import io.papermc.paper.datacomponent.item.PaperBlocksAttacks;
import io.papermc.paper.datacomponent.item.PaperBundleContents;
import io.papermc.paper.datacomponent.item.PaperChargedProjectiles;
import io.papermc.paper.datacomponent.item.PaperConsumable;
import io.papermc.paper.datacomponent.item.PaperCustomModelData;
import io.papermc.paper.datacomponent.item.PaperDamageResistant;
import io.papermc.paper.datacomponent.item.PaperDeathProtection;
import io.papermc.paper.datacomponent.item.PaperDyedItemColor;
import io.papermc.paper.datacomponent.item.PaperEnchantable;
import io.papermc.paper.datacomponent.item.PaperEquippable;
import io.papermc.paper.datacomponent.item.PaperFireworks;
import io.papermc.paper.datacomponent.item.PaperFoodProperties;
import io.papermc.paper.datacomponent.item.PaperItemAdventurePredicate;
import io.papermc.paper.datacomponent.item.PaperItemArmorTrim;
import io.papermc.paper.datacomponent.item.PaperItemAttributeModifiers;
import io.papermc.paper.datacomponent.item.PaperItemContainerContents;
import io.papermc.paper.datacomponent.item.PaperItemEnchantments;
import io.papermc.paper.datacomponent.item.PaperItemLore;
import io.papermc.paper.datacomponent.item.PaperItemTool;
import io.papermc.paper.datacomponent.item.PaperJukeboxPlayable;
import io.papermc.paper.datacomponent.item.PaperLodestoneTracker;
import io.papermc.paper.datacomponent.item.PaperMapDecorations;
import io.papermc.paper.datacomponent.item.PaperMapId;
import io.papermc.paper.datacomponent.item.PaperMapItemColor;
import io.papermc.paper.datacomponent.item.PaperOminousBottleAmplifier;
import io.papermc.paper.datacomponent.item.PaperPotDecorations;
import io.papermc.paper.datacomponent.item.PaperPotionContents;
import io.papermc.paper.datacomponent.item.PaperRepairable;
import io.papermc.paper.datacomponent.item.PaperResolvableProfile;
import io.papermc.paper.datacomponent.item.PaperSeededContainerLoot;
import io.papermc.paper.datacomponent.item.PaperSuspiciousStewEffects;
import io.papermc.paper.datacomponent.item.PaperTooltipDisplay;
import io.papermc.paper.datacomponent.item.PaperUseCooldown;
import io.papermc.paper.datacomponent.item.PaperUseRemainder;
import io.papermc.paper.datacomponent.item.PaperWeapon;
import io.papermc.paper.datacomponent.item.PaperWritableBookContent;
import io.papermc.paper.datacomponent.item.PaperWrittenBookContent;
import io.papermc.paper.registry.PaperRegistries;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.InstrumentComponent;
import net.minecraft.world.item.component.MapPostProcessing;
import net.minecraft.world.item.component.ProvidesTrimMaterial;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.craftbukkit.CraftMusicInstrument;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.entity.CraftCat;
import org.bukkit.craftbukkit.entity.CraftChicken;
import org.bukkit.craftbukkit.entity.CraftCow;
import org.bukkit.craftbukkit.entity.CraftFrog;
import org.bukkit.craftbukkit.entity.CraftPig;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.craftbukkit.inventory.CraftMetaFirework;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimMaterial;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Salmon;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemRarity;

import static io.papermc.paper.util.MCUtil.transformUnmodifiable;

public final class DataComponentAdapters {

    static final Function<Unit, Void> UNIT_TO_API_CONVERTER = $ -> {
        throw new UnsupportedOperationException("Cannot convert the Unit type to an API value");
    };

    static final Function UNIMPLEMENTED_TO_API_CONVERTER = $ -> {
        throw new UnsupportedOperationException("Cannot convert the an unimplemented type to an API value");
    };

    static final Map<ResourceKey<DataComponentType<?>>, DataComponentAdapter<?, ?>> ADAPTERS = new HashMap<>();

    public static void bootstrap() {
        registerIdentity(DataComponents.MAX_STACK_SIZE);
        registerIdentity(DataComponents.MAX_DAMAGE);
        registerIdentity(DataComponents.DAMAGE);
        registerUntyped(DataComponents.UNBREAKABLE);
        registerIdentity(DataComponents.POTION_DURATION_SCALE);
        register(DataComponents.CUSTOM_NAME, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
        register(DataComponents.ITEM_NAME, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
        register(DataComponents.ITEM_MODEL, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
        register(DataComponents.LORE, PaperItemLore::new);
        register(DataComponents.RARITY, nms -> ItemRarity.valueOf(nms.name()), api -> Rarity.valueOf(api.name()));
        register(DataComponents.ENCHANTMENTS, PaperItemEnchantments::new);
        register(DataComponents.CAN_PLACE_ON, PaperItemAdventurePredicate::new);
        register(DataComponents.CAN_BREAK, PaperItemAdventurePredicate::new);
        register(DataComponents.ATTRIBUTE_MODIFIERS, PaperItemAttributeModifiers::new);
        register(DataComponents.CUSTOM_MODEL_DATA, PaperCustomModelData::new);
        registerIdentity(DataComponents.REPAIR_COST);
        // registerUntyped(DataComponents.CREATIVE_SLOT_LOCK);
        registerIdentity(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
        registerUntyped(DataComponents.INTANGIBLE_PROJECTILE);
        register(DataComponents.FOOD, PaperFoodProperties::new);
        register(DataComponents.CONSUMABLE, PaperConsumable::new);
        register(DataComponents.USE_REMAINDER, PaperUseRemainder::new);
        register(DataComponents.USE_COOLDOWN, PaperUseCooldown::new);
        register(DataComponents.DAMAGE_RESISTANT, PaperDamageResistant::new);
        register(DataComponents.TOOL, PaperItemTool::new);
        register(DataComponents.ENCHANTABLE, PaperEnchantable::new);
        register(DataComponents.EQUIPPABLE, PaperEquippable::new);
        register(DataComponents.REPAIRABLE, PaperRepairable::new);
        registerUntyped(DataComponents.GLIDER);
        register(DataComponents.TOOLTIP_STYLE, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
        register(DataComponents.DEATH_PROTECTION, PaperDeathProtection::new);
        register(DataComponents.STORED_ENCHANTMENTS, PaperItemEnchantments::new);
        register(DataComponents.DYED_COLOR, PaperDyedItemColor::new);
        register(DataComponents.MAP_COLOR, PaperMapItemColor::new);
        register(DataComponents.MAP_ID, PaperMapId::new);
        register(DataComponents.MAP_DECORATIONS, PaperMapDecorations::new);
        register(DataComponents.MAP_POST_PROCESSING, nms -> io.papermc.paper.item.MapPostProcessing.valueOf(nms.name()), api -> MapPostProcessing.valueOf(api.name()));
        register(DataComponents.CHARGED_PROJECTILES, PaperChargedProjectiles::new);
        register(DataComponents.BUNDLE_CONTENTS, PaperBundleContents::new);
        register(DataComponents.POTION_CONTENTS, PaperPotionContents::new);
        register(DataComponents.SUSPICIOUS_STEW_EFFECTS, PaperSuspiciousStewEffects::new);
        register(DataComponents.WRITTEN_BOOK_CONTENT, PaperWrittenBookContent::new);
        register(DataComponents.WRITABLE_BOOK_CONTENT, PaperWritableBookContent::new);
        register(DataComponents.TRIM, PaperItemArmorTrim::new);
        // debug stick state
        // entity data
        // bucket entity data
        // block entity data
        register(DataComponents.INSTRUMENT, nms -> CraftMusicInstrument.minecraftHolderToBukkit(nms.instrument().unwrap(CraftRegistry.getMinecraftRegistry()).orElseThrow()), api -> new InstrumentComponent(CraftMusicInstrument.bukkitToMinecraftHolder(api)));
        register(DataComponents.PROVIDES_TRIM_MATERIAL, nms -> CraftTrimMaterial.minecraftHolderToBukkit(nms.material().unwrap(CraftRegistry.getMinecraftRegistry()).orElseThrow()), api -> new ProvidesTrimMaterial(CraftTrimMaterial.bukkitToMinecraftHolder(api)));
        register(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, PaperOminousBottleAmplifier::new);
        register(DataComponents.JUKEBOX_PLAYABLE, PaperJukeboxPlayable::new);
        register(DataComponents.PROVIDES_BANNER_PATTERNS, PaperRegistries::fromNms, PaperRegistries::toNms);
        register(
            DataComponents.RECIPES,
            nms -> transformUnmodifiable(nms, PaperAdventure::asAdventureKey),
            api -> transformUnmodifiable(api, key -> PaperAdventure.asVanilla(Registries.RECIPE, key))
        );
        register(DataComponents.LODESTONE_TRACKER, PaperLodestoneTracker::new);
        register(DataComponents.FIREWORK_EXPLOSION, CraftMetaFirework::getEffect, CraftMetaFirework::getExplosion);
        register(DataComponents.FIREWORKS, PaperFireworks::new);
        register(DataComponents.PROFILE, PaperResolvableProfile::new);
        register(DataComponents.NOTE_BLOCK_SOUND, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
        register(DataComponents.BANNER_PATTERNS, PaperBannerPatternLayers::new);
        register(DataComponents.BASE_COLOR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));
        register(DataComponents.POT_DECORATIONS, PaperPotDecorations::new);
        register(DataComponents.CONTAINER, PaperItemContainerContents::new);
        register(DataComponents.BLOCK_STATE, PaperBlockItemDataProperties::new);
        // bees
        // register(DataComponents.LOCK, PaperLockCode::new);
        register(DataComponents.CONTAINER_LOOT, PaperSeededContainerLoot::new);
        register(DataComponents.BREAK_SOUND, nms -> PaperAdventure.asAdventure(nms.value().location()), PaperAdventure::resolveSound);
        register(DataComponents.TOOLTIP_DISPLAY, PaperTooltipDisplay::new);
        register(DataComponents.WEAPON, PaperWeapon::new);
        register(DataComponents.BLOCKS_ATTACKS, PaperBlocksAttacks::new);

        register(DataComponents.VILLAGER_VARIANT, CraftVillager.CraftType::minecraftHolderToBukkit, CraftVillager.CraftType::bukkitToMinecraftHolder);
        register(DataComponents.WOLF_VARIANT, CraftWolf.CraftVariant::minecraftHolderToBukkit, CraftWolf.CraftVariant::bukkitToMinecraftHolder);
        register(DataComponents.WOLF_COLLAR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));
        register(DataComponents.WOLF_SOUND_VARIANT, CraftWolf.CraftSoundVariant::minecraftHolderToBukkit, CraftWolf.CraftSoundVariant::bukkitToMinecraftHolder);
        register(DataComponents.FOX_VARIANT, nms -> org.bukkit.entity.Fox.Type.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.Fox.Variant.byId(api.ordinal()));
        register(DataComponents.SALMON_SIZE, nms -> Salmon.Variant.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.Salmon.Variant.values()[api.ordinal()]);
        register(DataComponents.PARROT_VARIANT, nms -> Parrot.Variant.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.Parrot.Variant.byId(api.ordinal()));
        register(DataComponents.TROPICAL_FISH_PATTERN, nms -> TropicalFish.Pattern.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.TropicalFish.Pattern.values()[api.ordinal()]);
        register(DataComponents.TROPICAL_FISH_BASE_COLOR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));
        register(DataComponents.TROPICAL_FISH_PATTERN_COLOR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));
        register(DataComponents.MOOSHROOM_VARIANT, nms -> MushroomCow.Variant.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.MushroomCow.Variant.values()[api.ordinal()]);
        register(DataComponents.RABBIT_VARIANT, nms -> Rabbit.Type.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.Rabbit.Variant.byId(api.ordinal()));
        register(DataComponents.PIG_VARIANT, CraftPig.CraftVariant::minecraftHolderToBukkit, CraftPig.CraftVariant::bukkitToMinecraftHolder);
        register(DataComponents.COW_VARIANT, CraftCow.CraftVariant::minecraftHolderToBukkit, CraftCow.CraftVariant::bukkitToMinecraftHolder);
        register(DataComponents.CHICKEN_VARIANT, nms -> CraftChicken.CraftVariant.minecraftHolderToBukkit(nms.unwrap(CraftRegistry.getMinecraftRegistry()).orElseThrow()), api -> new EitherHolder<>(CraftChicken.CraftVariant.bukkitToMinecraftHolder(api)));
        register(DataComponents.FROG_VARIANT, CraftFrog.CraftVariant::minecraftHolderToBukkit, CraftFrog.CraftVariant::bukkitToMinecraftHolder);
        register(DataComponents.HORSE_VARIANT, nms -> Horse.Color.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.horse.Variant.byId(api.ordinal()));
        register(DataComponents.PAINTING_VARIANT, CraftArt::minecraftHolderToBukkit, CraftArt::bukkitToMinecraftHolder);
        register(DataComponents.LLAMA_VARIANT, nms -> Llama.Color.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.horse.Llama.Variant.byId(api.ordinal()));
        register(DataComponents.AXOLOTL_VARIANT, nms -> Axolotl.Variant.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.axolotl.Axolotl.Variant.byId(api.ordinal()));
        register(DataComponents.CAT_VARIANT, CraftCat.CraftType::minecraftHolderToBukkit, CraftCat.CraftType::bukkitToMinecraftHolder);
        register(DataComponents.CAT_COLLAR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));
        register(DataComponents.SHEEP_COLOR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));
        register(DataComponents.SHULKER_COLOR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));

        for (final ResourceKey<DataComponentType<?>> key : BuiltInRegistries.DATA_COMPONENT_TYPE.registryKeySet()) {
            if (!ADAPTERS.containsKey(key)) {
                registerUnimplemented(key);
            }
        }
    }

    private static <NMS> ResourceKey<DataComponentType<?>> getKey(final DataComponentType<NMS> type) {
        return BuiltInRegistries.DATA_COMPONENT_TYPE.getResourceKey(type).orElseThrow();
    }

    public static void registerUntyped(final DataComponentType<Unit> type) {
        registerInternal(getKey(type), UNIT_TO_API_CONVERTER, DataComponentAdapter.API_TO_UNIT_CONVERTER, false);
    }

    private static <COMMON> void registerIdentity(final DataComponentType<COMMON> type) {
        registerInternal(getKey(type), Function.identity(), Function.identity(), true);
    }

    @SuppressWarnings("unchecked")
    public static void registerUnimplemented(final ResourceKey<DataComponentType<?>> key) {
        registerInternal(key, UNIMPLEMENTED_TO_API_CONVERTER, DataComponentAdapter.API_TO_UNIMPLEMENTED_CONVERTER, false);
    }

    private static <NMS, API extends Handleable<NMS>> void register(final DataComponentType<NMS> type, final Function<NMS, API> vanillaToApi) {
        registerInternal(getKey(type), vanillaToApi, Handleable::getHandle, false);
    }

    private static <NMS, API> void register(final DataComponentType<NMS> type, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla) {
        registerInternal(getKey(type), vanillaToApi, apiToVanilla, false);
    }

    private static <NMS, API> void registerInternal(final ResourceKey<DataComponentType<?>> key, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla, final boolean codecValidation) {
        if (ADAPTERS.containsKey(key)) {
            throw new IllegalStateException("Duplicate adapter registration for " + key);
        }
        ADAPTERS.put(key, new DataComponentAdapter<>(apiToVanilla, vanillaToApi, codecValidation));
    }
}
