package io.papermc.paper.datacomponent;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.datacomponent.item.PaperBannerPatternLayers;
import io.papermc.paper.datacomponent.item.PaperBlockItemDataProperties;
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
import io.papermc.paper.datacomponent.item.PaperUnbreakable;
import io.papermc.paper.datacomponent.item.PaperUseCooldown;
import io.papermc.paper.datacomponent.item.PaperUseRemainder;
import io.papermc.paper.datacomponent.item.PaperWritableBookContent;
import io.papermc.paper.datacomponent.item.PaperWrittenBookContent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.MapPostProcessing;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftMusicInstrument;
import org.bukkit.craftbukkit.inventory.CraftMetaFirework;
import org.bukkit.craftbukkit.util.Handleable;
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
        register(DataComponents.UNBREAKABLE, PaperUnbreakable::new);
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
        registerUntyped(DataComponents.HIDE_ADDITIONAL_TOOLTIP);
        registerUntyped(DataComponents.HIDE_TOOLTIP);
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
        register(DataComponents.INSTRUMENT, CraftMusicInstrument::minecraftHolderToBukkit, CraftMusicInstrument::bukkitToMinecraftHolder);
        register(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, PaperOminousBottleAmplifier::new);
        register(DataComponents.JUKEBOX_PLAYABLE, PaperJukeboxPlayable::new);
        register(DataComponents.RECIPES,
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

        for (final Map.Entry<ResourceKey<DataComponentType<?>>, DataComponentType<?>> componentType : BuiltInRegistries.DATA_COMPONENT_TYPE.entrySet()) {
            if (!ADAPTERS.containsKey(componentType.getKey())) {
                registerUnimplemented(componentType.getValue());
            }
        }
    }

    public static void registerUntyped(final DataComponentType<Unit> type) {
        registerInternal(type, UNIT_TO_API_CONVERTER, DataComponentAdapter.API_TO_UNIT_CONVERTER, false);
    }

    private static <COMMON> void registerIdentity(final DataComponentType<COMMON> type) {
        registerInternal(type, Function.identity(), Function.identity(), true);
    }

    public static <NMS> void registerUnimplemented(final DataComponentType<NMS> type) {
        registerInternal(type, UNIMPLEMENTED_TO_API_CONVERTER, DataComponentAdapter.API_TO_UNIMPLEMENTED_CONVERTER, false);
    }

    private static <NMS, API extends Handleable<NMS>> void register(final DataComponentType<NMS> type, final Function<NMS, API> vanillaToApi) {
        registerInternal(type, vanillaToApi, Handleable::getHandle, false);
    }

    private static <NMS, API> void register(final DataComponentType<NMS> type, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla) {
        registerInternal(type, vanillaToApi, apiToVanilla, false);
    }

    private static <NMS, API> void registerInternal(final DataComponentType<NMS> type, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla, final boolean codecValidation) {
        final ResourceKey<DataComponentType<?>> key = BuiltInRegistries.DATA_COMPONENT_TYPE.getResourceKey(type).orElseThrow();
        if (ADAPTERS.containsKey(key)) {
            throw new IllegalStateException("Duplicate adapter registration for " + key);
        }
        ADAPTERS.put(key, new DataComponentAdapter<>(type, apiToVanilla, vanillaToApi, codecValidation && !type.isTransient()));
    }
}
